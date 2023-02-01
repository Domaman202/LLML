package ru.DmN.llml.parser;

import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.Variable;
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
            ctx.functions.add(parseFunction());
            lexer.skipNLSpaces();
        }
        return ctx;
    }

    public SyFunction parseFunction() {
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
        var function = new SyFunction(name, ret, args);
        while (parseExpression(function)) ;
        return function;
    }

    public boolean parseExpression(SyFunction function) {
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
                        case PILLAR -> expression.actions.add(new ActReturn(function.ret));
                        case NAMING -> {
                            var var = function.locals.get(token.str);
                            if (var == null) {
                                var = new Variable(token.str, Type.UNKNOWN);
                                function.locals.add(var);
                            }
                            expression.actions.add(new ActSetVariable(var));
                        }
                        default -> throw new RuntimeException("(" + token.line + ',' + token.symbol + ") \"" + token.type + "\" != PILLAR|NAMING");
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
                    ActMathOperation.Operation oper;
                    switch (token.str) {
                        case "+" -> oper = ActMathOperation.Operation.ADD;
                        case "-" -> oper = ActMathOperation.Operation.SUB;
                        case "*" -> oper = ActMathOperation.Operation.MUL;
                        case "/" -> oper = ActMathOperation.Operation.DIV;
                        default -> throw new RuntimeException("(" + token.line + ',' + token.symbol + ") Операция \"" + token.str + "\" ещё не реализована!");
                    }
                    expression.actions.add(new ActMathOperation(oper, Type.UNKNOWN));
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
