package ru.DmN.llml.parser;

import org.jetbrains.annotations.Contract;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.parser.ast.SyExpression;
import ru.DmN.llml.utils.*;
import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.parser.ast.SyContext;
import ru.DmN.llml.parser.ast.SyFunction;

import java.util.ArrayList;

public class Parser {
    protected final Lexer lexer;

    public Parser(String str) {
        this.lexer = new Lexer(str);
    }

    public SyContext parse() {
        var ctx = new SyContext();
        while (!lexer.str.isEmpty()) {
            var name = next(Token.Type.NAMING).str;
            var token = next();
            if (token.type == Token.Type.OPEN_BRACKET) {
                ctx.functions.add(parseFunction(name));
            } else {
                // todo: переменные
            }
            lexer.skipNLSpaces();
        }
        return ctx;
    }

    public SyFunction parseFunction(String name) {
        var args = new ArrayList<Argument>();
        var token = next();
        while (token.type != Token.Type.CLOSE_BRACKET) {
            if (token.type == Token.Type.COMMA)
                token = next();
            check(token, Token.Type.NAMING);
            var argName = token.str;
            Type argType;
            token = next();
            if (token.type == Token.Type.COLON) {
                argType = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
                token = next();
            } else argType = Type.UNKNOWN;
            args.add(new Argument(argName, argType));
        }
        Type ret;
        token = next();
        if (token.type == Token.Type.COLON) {
            ret = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
            token = next();
        } else ret = Type.UNKNOWN;
        check(token, Token.Type.ASSIGN);
        next(Token.Type.OPEN_FBRACKET);
        var function = new SyFunction(name, ret, args);
        while (parseExpression(function)) ;
        return function;
    }

    public boolean parseExpression(SyFunction function) {
        Token token = next();
        if (token.type == Token.Type.CLOSE_FBRACKET)
            return false;
        if (token.type == Token.Type.OPEN_CBRACKET) {
            var expression = function.expression();
            while (true) {
                token = next();
                switch (token.type) {
                    case NL, COMMA -> {
                    }
                    case CLOSE_BRACKET -> {
                        next(Token.Type.PTR);
                        token = next();
                        switch (token.type) {
                            case PILLAR -> expression.actions.add(new ActReturn(function.ret));
                            case NAMING -> expression.actions.add(new ActSetVariable(function.locals.getOrAdd(token.str, Type.UNKNOWN)));
                            default ->
                                    throw new RuntimeException("(" + token.line + ',' + token.symbol + ") \"" + token.type + "\" != PILLAR|NAMING");
                        }

                        return true;
                    }
                    case CLOSE_CBRACKET -> {
                        return true;
                    }
                    case NAMING -> {
                        var var = function.locals.get(token.str);
                        if (var == null)
                            throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Неизвестная переменная \"" + token.str + "\"");
                        expression.actions.add(new ActInsertVariable(var));
                    }
                    case NUMBER -> expression.actions.add(new ActInsertInteger(Integer.parseInt(token.str)));
                    case OPERATION -> {
                        ActMath.Operation oper;
                        switch (token.str) {
                            case "+" -> oper = ActMath.Operation.ADD;
                            case "-" -> oper = ActMath.Operation.SUB;
                            case "*" -> oper = ActMath.Operation.MUL;
                            case "/" -> oper = ActMath.Operation.DIV;
                            default ->
                                    throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Операция \"" + token.str + "\" ещё не реализована!");
                        }
                        expression.actions.add(new ActMath(oper, Type.UNKNOWN));
                    }
                    default -> throwBadToken(token);
                }
            }
        } else {
            Value value = null;
            if (token.type == Token.Type.NAMING)
                value = new Value(function.locals.get(token.str));
            else if (token.type == Token.Type.NUMBER)
                value = new Value(new Constant(token.str));
            else throwBadToken(token);
            check(token, Token.Type.NAMING);
            next(Token.Type.PTR);
            token = next();
            var expr = new SyExpression();
            expr.actions.add(value.constant == null ? new ActInsertVariable(value.variable) : new ActInsertInteger((int) value.constant.value));
            if (token.type == Token.Type.NAMING)
                expr.actions.add(new ActSetVariable(function.locals.getOrAdd(token.str, value.type())));
            else if (token.type == Token.Type.PILLAR)
                expr.actions.add(new ActReturn(function.ret));
            else throwBadToken(token);
            function.expressions.add(expr);
            return true;
        }
    }

    protected Token next(Token.Type needed) {
        var token = next();
        check(token, needed);
        return token;
    }

    protected Token next() {
        Token token;
        do {
            token = lexer.next();
        } while (token.type == Token.Type.NL);
        return token;
    }

    protected void check(Token token, Token.Type needed) {
        if (token.type != needed) {
            throw new RuntimeException("(" + token.line + ',' + token.symbol + ") \"" + token.type + "\" != \"" + needed + "\"");
        }
    }

    @Contract("_ -> fail")
    protected void throwBadToken(Token token) throws RuntimeException {
        throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Неверный токен \"" + token.type + "\"");
    }
}
