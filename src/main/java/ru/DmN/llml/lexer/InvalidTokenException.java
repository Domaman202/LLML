package ru.DmN.llml.lexer;

public class InvalidTokenException extends RuntimeException {
    protected InvalidTokenException(String msg) {
        super(msg);
    }

    public static InvalidTokenException create(String src, Token token) {
        for (int i = 1; i < token.line; i++)
            src = src.substring(src.indexOf('\n') + 1);
        var lei = src.indexOf('\n');
        if (lei > -1)
            src = src.substring(0, lei);
        var out = new StringBuilder("(");
        out.append(token.line).append(", ").append(token.symbol).append(") Неверный токен");
        if (token.type != Token.Type.ERROR)
            out.append(" \"").append(token.type).append("\"");
        out.append(":\n").append(src).append('\n').append(" ".repeat(Math.max(0, token.symbol - token.str.length()))).append('^').append("~".repeat(Math.max(0, token.str.length() - 1)));
        return new InvalidTokenException(out.toString());
    }
}
