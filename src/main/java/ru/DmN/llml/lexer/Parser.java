package ru.DmN.llml.lexer;

import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Context;
import ru.DmN.llml.llvm.Function;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.expr.Math2Expr;

import java.util.ArrayList;

public class Parser {
    protected final Lexer lexer;

    public Parser(String str) {
        this.lexer = new Lexer(str);
    }

    public Context parse() {
        var ctx = new Context();
        while (!lexer.str.isEmpty()) {
            ctx.functions.add(parseFunction());
            lexer.skipNLSpaces();
        }
        return ctx;
    }

    public Function parseFunction() {
        next(Token.Type.FUN);
        var name = next(Token.Type.NAMING).str;
        next(Token.Type.OPEN_BRACKET);
        var args = new ArrayList<Argument>();
        var token = next();
        while (token.type != Token.Type.CLOSE_BRACKET) {
            if (token.type == Token.Type.COMMA)
                token = next();
            check(token, Token.Type.NAMING);
            var argName = token.str;
            next(Token.Type.COLON);
            var argType = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
            args.add(new Argument(argName, argType));
            token = next();
        }
        Type ret;
        token = next();
        if (token.type == Token.Type.COLON) {
            ret = Type.valueOf(next(Token.Type.TYPE).str.toUpperCase());
            token = next();
        } else ret = Type.UNKNOWN;
        check(token, Token.Type.OPEN_FBRACKET);
        var function = new Function(name, ret, args);
        while (parseExpression(function)) ;
        return function;
    }

    public boolean parseExpression(Function function) {
        Token token = next();
        if (token.type == Token.Type.CLOSE_FBRACKET)
            return false;
        check(token, Token.Type.OPEN_CBRACKET);
        var expression = function.expression();
        while (true) {
            token = next();
            switch (token.type) {
                case NL, COMMA -> {}
                case CLOSE_BRACKET -> {
                    next(Token.Type.PTR);
                    token = next();
                    switch (token.type) {
                        case PILLAR -> expression.ret();
                        case NAMING -> expression.save(token.str);
                        default -> throw new RuntimeException("(" + token.line + ',' + token.symbol + ") \"" + token.type + "\" != PILLAR|NAMING");
                    }

                    return true;
                }
                case CLOSE_CBRACKET -> {
                    return true;
                }
                case NAMING -> {
                    if (!expression.insert(token.str))
                        throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Неизвестная переменная \"" + token.str + "\"");
                }
                case NUMBER -> expression.insert(Integer.parseInt(token.str));
                case OPERATION -> {
                    switch (token.str) {
                        case "+" -> expression.operation(Math2Expr.Type.ADD);
                        case "-" -> expression.operation(Math2Expr.Type.SUB);
                        case "*" -> expression.operation(Math2Expr.Type.MUL);
                        case "/" -> expression.operation(Math2Expr.Type.DIV);
                        default -> throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Операция \"" + token.str + "\" ещё не реализована!");
                    }
                }
                default -> throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Неверный токен \"" + token.type + "\"");
            }
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
}
