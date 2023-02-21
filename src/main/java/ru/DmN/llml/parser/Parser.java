package ru.DmN.llml.parser;

import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Parser {
    public final Lexer lexer;
    public final AstContext context;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.context = new AstContext();
    }

    public AstContext parse() {
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
                    this.context.functions.add(function);
                    // парсим тело функции
                    function.expressions.addAll(this.parseBody(function).actions);
                }
                default -> throw InvalidTokenException.create(this.lexer.src, token);
            }
            token = this.lexer.next();
        }
        return this.context;
    }

    protected AstActions parseBody(AstFunction function) {
        var expressions = new ArrayList<AstExpression>();
        Token token;
        //
        next(Token.Type.OPEN_FBRACKET);
        token = this.next(Token.Type.ANNOTATION, Token.Type.OPEN_CBRACKET, Token.Type.CLOSE_FBRACKET);
        // указатель старта выражения
        var start = -1;
        // указатель конца выражение
        var end = -1;
        //
        var tmp = 0;
        //
        cycle$bodyparse:
        while (true) {
            switch (token.type) {
                // аннотация
                case ANNOTATION -> {
                    if (tmp == 0) {
                        function.expressions.add(this.parseAnnotation(function));
                    }
                }
                // начало выражения
                case OPEN_CBRACKET -> {
                    start = this.lexer.ptr;
                    tmp++;
                }
                // выход из функции
                case CLOSE_CBRACKET -> {
                    end = this.lexer.ptr - 1;
                    this.lexer.ptr = start;
                    expressions.add(new AstReturn(this.parseExpression(function, end)));
                    this.lexer.ptr = end + 1;
                }
                case OPEN_BRACKET -> tmp++;
                // конец выражения/присваивание
                case CLOSE_BRACKET -> {
                    if (tmp == 1) {
                        end = this.lexer.ptr - 1;
                        this.lexer.ptr = start;
                        var actions = this.parseActions(function, end);
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
                    } else tmp--;
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
        AstExpression result;
        var token = this.next(Token.Type.NAMING);
        this.next(Token.Type.OPEN_BRACKET);
        switch (token.str) {
            case "call" -> {
                var name = this.next(Token.Type.NAMING).str;
                var fun = this.context.functions.stream().filter(it -> it.name.equals(name)).findFirst().orElseThrow(() -> new RuntimeException("Функция \"" + name + "\" не определена!"));
                List<AstExpression> arguments;
                // указатель старта выражения
                int start = this.lexer.ptr;
                // указатель конца выражение
                int end = -1;
                //
                int tmp = 0;
                //
                cycle:
                while (true) {
                    token = this.lexer.next();
                    switch (token.type) {
                        // аннотация
                        case ANNOTATION -> {
                            if (tmp == 1) {
                                function.expressions.add(this.parseAnnotation(function));
                            }
                        }
                        case OPEN_BRACKET -> tmp++;
                        // конец аннотации
                        case CLOSE_BRACKET -> {
                            if (tmp == 0) {
                                end = this.lexer.ptr - 1;
                                this.lexer.ptr = start;
                                arguments = this.parseActions(function, end).actions;
                                this.lexer.ptr = end + 1;
                                break cycle;
                            } else tmp--;
                        }
                        default -> {}
                    }
                }
                //
                return new AstCall(fun, arguments);
            }
            case "if" -> {
                var value = this.parseExpression(function, Integer.MAX_VALUE);
                var a = new AstNamedActionsReference(this.next(Token.Type.NAMING).str);
                var b = new AstNamedActionsReference(this.next(Token.Type.NAMING).str);
                result = new AstIf(value, a, b);
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
        return result;
    }

    protected AstActions parseActions(AstFunction function, int endptr) {
        var actions = new ArrayList<AstExpression>();
        while (true) {
            this.lexer.next();
            if (this.lexer.ptr <= endptr) {
                this.lexer.ptr--;
                actions.add(this.parseExpression(function, endptr));
            } else {
                break;
            }
        }
        return new AstActions(actions);
    }

    protected AstExpression parseExpression(AstFunction function, int endptr) {
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
                            return new AstMath1Arg(AstMath1Arg.Operation.of(token.str), this.parseExpression(function, endptr));
                        }
                        case "+", "-", "*", "/", "&", "|", "=", "!=", ">", ">=", "<", "<=" -> {
                            return new AstMath2Arg(AstMath2Arg.Operation.of(token.str), this.parseExpression(function, endptr), this.parseExpression(function, endptr));
                        }
                        default -> throw InvalidTokenException.create(this.lexer.src, token);
                    }
                }
                case ANNOTATION -> {
                    return this.parseAnnotation(function);
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
