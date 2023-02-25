package ru.DmN.llml.parser;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Парсер
 */
public class Parser {
    /**
     * Лексер
     */
    public final @NotNull Lexer lexer;
    /**
     * Контекст
     */
    public final @NotNull AstContext context;

    /**
     * @param lexer Лексер
     */
    public Parser(@NotNull Lexer lexer) {
        this.lexer = lexer;
        this.context = new AstContext();
    }

    /**
     * Парсинг контекста
     * @return Контекст
     */
    public @NotNull AstContext parse() {
        var token = this.next();
        while (token.type != Token.Type.EOF) {
            if (Objects.requireNonNull(token.type) == Token.Type.NAMING) {
                var name = token.str;
                var arguments = new ArrayList<AstArgument>();
                Type ret = Type.UNKNOWN;
                // обработка аргументов
                token = this.next(Token.Type.OPEN_BRACKET, Token.Type.COLON);
                if (token.type == Token.Type.COLON) {
                    var type = Type.valueOf(this.next(Token.Type.TYPE).str.toUpperCase());
                    AstConstant value = new AstConstant(0);
                    token = this.next();
                    if (token.type == Token.Type.ASSIGN) {
                        token = this.next(Token.Type.NAMING, Token.Type.NUMBER);
                        if (token.type == Token.Type.NAMING) {
                            if (token.str.equals("ext")) {
                                value = null;
                            } else throw InvalidTokenException.create(this.lexer.src, token);
                        } else value = new AstConstant(token.str);
                    } else this.lexer.ptr--;
                    this.context.variables.add(new AstVariable(name, type, value == null, value));
                } else {
                    token = this.next(Token.Type.NAMING, Token.Type.CLOSE_BRACKET);
                    cycle:
                    while (true) {
                        switch (token.type) {
                            case COMMA -> {
                            }
                            case NAMING -> {
                                var argument = new AstArgument(token.str);
                                arguments.add(argument);
                                token = next(Token.Type.COLON, Token.Type.COMMA, Token.Type.CLOSE_BRACKET);
                                if (Objects.requireNonNull(token.type) == Token.Type.COLON) {
                                    argument.type = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
                                } else continue;
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
                        token = this.next(Token.Type.COLON, Token.Type.ASSIGN, Token.Type.NL);
                        if (token.type == Token.Type.COLON) {
                            ret = Type.valueOf(this.next(Token.Type.TYPE).str.toUpperCase());
                        }
                    } while (token.type == Token.Type.COLON);
                    // добавляем функцию в список функций
                    var ret$ = ret;
                    var function = this.context.functions.stream().filter(it -> it.name.equals(name)).findFirst().orElseGet(() -> {
                        var fun = new AstFunction(name, arguments, ret$);
                        this.context.functions.add(fun);
                        return fun;
                    });
                    // парсим тело функции
                    if (token.type == Token.Type.ASSIGN) {
                        token = this.next(Token.Type.OPEN_FBRACKET, Token.Type.NAMING);
                        if (token.type == Token.Type.NAMING) {
                            if (token.str.equals("ext")) {
                                function.expressions = null;
                            } else throw InvalidTokenException.create(this.lexer.src, token);
                        } else {
                            this.lexer.ptr--;
                            function.expressions = new ArrayList<>();
                            function.expressions.addAll(this.parseBody(function).actions);
                        }
                    }
                }
            } else {
                throw InvalidTokenException.create(this.lexer.src, token);
            }
            token = this.next();
        }
        return this.context;
    }

    /**
     * Парсинг тела функции
     * @param function Функция
     * @return Тело функции
     */
    protected @NotNull AstActions parseBody(@NotNull AstFunction function) {
        var expressions = new ArrayList<AstExpression>();
        Token token;
        //
        this.next(Token.Type.OPEN_FBRACKET);
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
                                if (context.variable(function, vname) == null)
                                    function.variables.add(new AstVariable(vname));
                                expressions.add(new AstVariableSet(vname, actions.actions.get(i++)));
                            } while (true);
                        } else {
                            expressions.add(actions);
                            this.lexer.ptr--;
                        }
                        tmp = 0;
                    } else tmp--;
                }
                // конец тела функции
                case CLOSE_FBRACKET -> {
                    break cycle$bodyparse;
                }
                default -> {
                }
            }
            token = this.lexer.next();
        }
        return new AstActions(expressions);
    }

    /**
     * Парсинг аннотации
     * @param function Функция
     * @return Аннотация
     */
    protected @NotNull AstExpression parseAnnotation(@NotNull AstFunction function) {
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
                        default -> {
                        }
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
                this.next(Token.Type.ASSIGN);
                return new AstNamedActions(name, this.parseBody(function));
            }
            default -> throw InvalidTokenException.create(lexer.src, token);
        }
        this.next(Token.Type.CLOSE_BRACKET);
        return result;
    }

    /**
     * Парсинг действий
     * @param function Фукнция
     * @param endptr Указатель конца действий
     * @return Действия
     */
    protected @NotNull AstActions parseActions(@NotNull AstFunction function, int endptr) {
        var actions = new ArrayList<AstExpression>();
        while (true) {
            this.next();
            if (this.lexer.ptr <= endptr) {
                this.lexer.ptr--;
                actions.add(this.parseExpression(function, endptr));
            } else {
                break;
            }
        }
        return new AstActions(actions);
    }

    /**
     * Парсинг выражения
     * @param function Функция
     * @param endptr Указатель конца выражения
     * @return Выражение
     */
    protected @NotNull AstExpression parseExpression(@NotNull AstFunction function, int endptr) {
        var token = this.next();
        if (this.lexer.ptr <= endptr) {
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
                default -> throw InvalidTokenException.create(this.lexer.src, token);
            }
        }
        throw new RuntimeException("Jepa");
    }

    /**
     * Возвращает токен, сдвигает указатель
     * @return Токен
     */
    protected Token next() {
        var token = this.lexer.next();
        while (token.type == Token.Type.NL)
            token = this.lexer.next();
        return token;
    }

    /**
     * Возвращает токен, сдвигает указатель
     * @param type Требуемые типы для токена
     * @return Токен
     * @throws InvalidTokenException Токен не соотвествует возможному типу
     */
    protected Token next(@NotNull Token.Type... type) {
        var types = Arrays.stream(type).collect(Collectors.toList());
        if (types.contains(Token.Type.NAMING))
            types.add(Token.Type.TYPE);
        var token = this.lexer.next();
        if (token.type == Token.Type.NL) {
            if (types.contains(Token.Type.NL)) {
                return token;
            } else {
                token = this.next();
            }
        }
        return this.check(token, types);
    }

    /**
     * Проверяет токен на соотвествие типу
     * @param token Токен
     * @param types Возможные типы
     * @return Токен
     * @throws InvalidTokenException Токен не соотвествует возможному типу
     */
    protected Token check(@NotNull Token token, @NotNull List<Token.Type> types) {
        if (types.stream().anyMatch(t -> t == token.type)) {
            return token;
        } else {
            throw InvalidTokenException.create(this.lexer.src, token);
        }
    }
}
