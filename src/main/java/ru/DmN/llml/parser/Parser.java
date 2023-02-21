package ru.DmN.llml.parser;

import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Parser {
    public final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public AstContext parse() {
        var context = new AstContext();
        var token = this.lexer.next();
        while (token.type != Token.Type.EOF) {
            switch (token.type) {
                case NAMING -> {
                    var name = token.str;
                    var arguments = new ArrayList<AstArgument>();
                    Type ret = Type.UNKNOWN;
                    // обработка аргументов
                    this.next(Token.Type.OPEN_BRACKET);
                    token = this.next(Token.Type.NAMING, Token.Type.CLOSE_BRACKET);
                    cycle:
                    while (true) {
                        switch (token.type) {
                            case COMMA -> {}
                            case NAMING -> {
                                var argument = new AstArgument(token.str);
                                arguments.add(argument);
                                token = next(Token.Type.COLON, Token.Type.COMMA, Token.Type.CLOSE_BRACKET);
                                switch (token.type) {
                                    case COLON -> argument.type = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
                                    default -> {
                                        continue;
                                    }
                                }
                            }
                            case CLOSE_BRACKET -> {
                                break cycle;
                            }
                            default -> throw InvalidTokenException.create(this.lexer.src, token);
                        }
                        token = this.next(Token.Type.NAMING, Token.Type.COMMA, Token.Type.CLOSE_BRACKET);
                    }
                    // обработка возвращаемого значения
                    do {
                        token = next(Token.Type.COLON, Token.Type.PTR);
                        if (Objects.requireNonNull(token.type) == Token.Type.COLON) ret = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
                    } while(token.type != Token.Type.PTR);
                    // добавляем функцию в список функций
                    var function = new AstFunction(name, arguments, ret);
                    context.functions.add(function);
                    // парсим тело функции
                    function.expressions.addAll(this.parseBody(function).actions);
                }
                default -> throw InvalidTokenException.create(this.lexer.src, token);
            }
            token = this.lexer.next();
        }
        return context;
    }

    protected AstActions parseBody(AstFunction function) {
        var expressions = new ArrayList<AstExpression>();
        Token token;
        //
        next(Token.Type.OPEN_FBRACKET);
        token = this.next(Token.Type.ANNOTATION, Token.Type.OPEN_CBRACKET, Token.Type.CLOSE_FBRACKET);
        // указатель старта выражения
        int start = -1;
        // указатель конца выражение
        int end = -1;
        //
        cycle$bodyparse:
        while (true) {
            switch (token.type) {
                // аннотация
                case ANNOTATION -> function.expressions.add(this.parseAnnotation(function));
                // начало выражения
                case OPEN_CBRACKET -> start = this.lexer.ptr;
                // выход из функции
                case CLOSE_CBRACKET -> {
                    end = this.lexer.ptr - 1;
                    this.lexer.ptr = start;
                    expressions.add(new AstReturn(this.parseExpression(end)));
                    this.lexer.ptr = end + 1;
                }
                // конец выражения/присваивание
                case CLOSE_BRACKET -> {
                    end = this.lexer.ptr - 1;
                    this.lexer.ptr = start;
                    var actions = this.parseActions(end);
                    this.lexer.ptr = end + 1;
                    token = this.lexer.next();
                    if (token.type == Token.Type.PTR) {
                        next(Token.Type.OPEN_BRACKET);
                        int i = 0;
                        do {
                            token = next(Token.Type.NAMING, Token.Type.CLOSE_CBRACKET);
                            if (token.type == Token.Type.CLOSE_CBRACKET)
                                break;
                            var vname = token.str;
                            function.variables.add(new AstVariable(vname));
                            expressions.add(new AstVariableSet(vname, actions.actions.get(i++)));
                        } while (token.type != Token.Type.CLOSE_CBRACKET);
                    } else {
                        expressions.add(actions);
                        this.lexer.ptr--;
                    }
                }
                // конец тела функции
                case CLOSE_FBRACKET -> {
                    break cycle$bodyparse;
                }
                default -> {}
            }
            token = this.lexer.next();
        }
        return new AstActions(expressions);
    }

    protected AstExpression parseAnnotation(AstFunction function) {
        var token = this.next(Token.Type.NAMING);
        var annotation = new AstAnnotation(token.str);
        this.next(Token.Type.OPEN_BRACKET);
        switch (annotation.name) {
            case "if" -> {
                annotation.arguments.add(this.parseExpression(Integer.MAX_VALUE));
                annotation.arguments.add(new AstNamedActionsReference(this.next(Token.Type.NAMING).str));
                annotation.arguments.add(new AstNamedActionsReference(this.next(Token.Type.NAMING).str));
            }
            case "label" -> {
                var name = this.next(Token.Type.NAMING).str;
                this.next(Token.Type.CLOSE_BRACKET);
                this.next(Token.Type.PTR);
                return new AstNamedActions(name, this.parseBody(function));
            }
            default -> throw InvalidTokenException.create(lexer.src, token);
        }
        this.next(Token.Type.CLOSE_BRACKET);
        return annotation;
    }

    protected AstActions parseActions(int endptr) {
        var actions = new ArrayList<AstExpression>();
        while (true) {
            this.lexer.next();
            if (this.lexer.ptr <= endptr) {
                this.lexer.ptr--;
                actions.add(parseExpression(endptr));
            } else {
                break;
            }
        }
        return new AstActions(actions);
    }

    protected AstExpression parseExpression(int endptr) {
        var token = this.lexer.next();
        while (this.lexer.ptr <= endptr) {
            switch (token.type) {
                case NUMBER -> {
                    return new AstConstant(token.str.contains(".") ? (Object) Double.parseDouble(token.str) : (Object) Integer.parseInt(token.str));
                }
                case NAMING -> {
                    return new AstVariableGet(token.str);
                }
                case OPERATION -> {
                    switch (token.str) {
                        case "!" -> {
                            return new AstMath1Arg(AstMath1Arg.Operation.of(token.str), this.parseExpression(endptr));
                        }
                        case "+", "-", "*", "/", "&", "|", "=", "!=", ">", ">=", "<", "<=" -> {
                            return new AstMath2Arg(AstMath2Arg.Operation.of(token.str), this.parseExpression(endptr), this.parseExpression(endptr));
                        }
                        default -> throw InvalidTokenException.create(this.lexer.src, token);
                    }
                }
                default -> throw InvalidTokenException.create(this.lexer.src, token); // todo:
            }
        }
        throw new RuntimeException("Jepa");
    }

    protected Token next(Token.Type...type) {
        return this.check(this.lexer.next(), type);
    }

    protected Token check(Token token, Token.Type...type) {
        if (Arrays.stream(type).anyMatch(t -> t == token.type)) {
            return token;
        } else {
            throw InvalidTokenException.create(this.lexer.src, token);
        }
    }
}
