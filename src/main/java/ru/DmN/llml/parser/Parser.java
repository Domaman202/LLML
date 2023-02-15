package ru.DmN.llml.parser;

import org.jetbrains.annotations.Contract;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.lexer.Token;
import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.parser.ast.SyContext;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.utils.*;

import java.util.ArrayList;

public class Parser {
    protected final String src;
    protected final Lexer lexer;

    public Parser(String src) {
        this.src = src;
        this.lexer = new Lexer(src);
    }

    public SyContext parse() {
        var ctx = new SyContext();
        while (!lexer.str.isEmpty()) {
            var name = next(Token.Type.NAMING).str;
            var token = next();
            if (token.type == Token.Type.OPEN_BRACKET) {
                ctx.functions.add(parseFunction(ctx, name));
            } else {
                Variable var = null;
                if (token.type == Token.Type.COLON)
                    var = new Variable(name, Type.valueOf(next(Token.Type.TYPE).str.toUpperCase()));
                else if (token.type == Token.Type.ASSIGN)
                    var = new InitializedGlobalVariable(name, new Constant(next(Token.Type.NUMBER).str));
                else throwBadToken(token);
                ctx.variables.add(var);
            }
            lexer.skipNLSpaces();
        }
        return ctx;
    }

    public SyFunction parseFunction(SyContext ctx, String name) {
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
        var function = new SyFunction(ctx.variables, name, ret, args);
        while (parseExpression(function)) ;
        return function;
    }

    protected boolean parseExpression(SyFunction function) {
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
                            case NAMING -> {
                                var var = function.locals.getOrAdd(token.str, Type.UNKNOWN);
                                expression.actions.add(var instanceof GlobalVariable ? new ActSetGlobalVariable(var) : new ActSetVariable(var));
                            }
                            case OPEN_BRACKET -> {
                                while (token.type != Token.Type.CLOSE_CBRACKET) {
                                    var var = function.locals.getOrAdd(next(Token.Type.NAMING).str, Type.UNKNOWN);
                                    expression.actions.add(var instanceof GlobalVariable ? new ActSetGlobalVariable(var) : new ActSetVariable(var));
                                    token = next();
                                    if (token.type != Token.Type.COMMA) {
                                        check(token, Token.Type.CLOSE_CBRACKET);
                                    }
                                }
                            }
                            default -> throwBadToken(token);
                        }

                        return true;
                    }
                    case CLOSE_CBRACKET -> {
                        return true;
                    }
                    case NAMING -> {
                        var var = function.locals.getOrAdd(token.str, Type.UNKNOWN);
                        expression.actions.add(var instanceof GlobalVariable ? new ActInsertGlobalVariable(var) : new ActInsertVariable(var));
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
                    case DOG -> {
                        var annotation = parseAnnotation();
                        switch (annotation.name) {
                            case "call" -> expression.actions.add(new ActCall(annotation.args.get(0)));
                        }
                    }
                    default -> throwBadToken(token);
                }
            }
        } else {
            Value value = null;
            if (token.type == Token.Type.NAMING)
                value = new Value(function.locals.getOrAdd(token.str, Type.UNKNOWN));
            else if (token.type == Token.Type.NUMBER)
                value = new Value(new Constant(token.str));
            else throwBadToken(token);
            next(Token.Type.PTR);
            token = next();
            var expr = function.expression();
            expr.actions.add(value.constant == null ? (value.variable instanceof GlobalVariable ? new ActInsertGlobalVariable(value.variable) : new ActInsertVariable(value.variable)) : new ActInsertInteger((int) value.constant.value));
            if (token.type == Token.Type.NAMING) {
                var var = function.locals.getOrAdd(token.str, value.type());
                expr.actions.add(var instanceof GlobalVariable ? new ActSetGlobalVariable(var) : new ActSetVariable(var));
            } else if (token.type == Token.Type.PILLAR) {
                expr.actions.add(new ActReturn(function.ret));
            } else throwBadToken(token);
            return true;
        }
    }

    protected Annotation parseAnnotation() {
        var name = next(Token.Type.NAMING).str;
        var args = new ArrayList<String>();
        next(Token.Type.OPEN_BRACKET);
        var token = next();
        if (token.type != Token.Type.CLOSE_BRACKET) {
            do {
                args.add(token.str);
                token = next();
            } while (token.type == Token.Type.COMMA);
            check(token, Token.Type.CLOSE_BRACKET);
        }
        return new Annotation(name, args);
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
            throwBadToken(token);
        }
    }

    @Contract("_ -> fail")
    protected void throwBadToken(Token token) throws InvalidTokenException {
        throw InvalidTokenException.create(src, token);
    }
}
