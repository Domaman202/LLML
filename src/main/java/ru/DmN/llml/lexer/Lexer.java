package ru.DmN.llml.lexer;

public class Lexer {
    protected String str;
    protected int line;
    protected int symbol;

    public Lexer(String str) {
        this.str = str;
    }

    public Token next() {
        skipSpaces();
        if (str.length() > 0) {
            var c = str.charAt(0);
            if (Character.isDigit(c))
                return parseNumber();
            if (Character.isLetter(c)) {
                if (c == 'f' && str.charAt(1) == 'u' && str.charAt(2) == 'n') {
                    return new Token("fun", Token.Type.FUN, line, delete(3));
                }
                return parseNaming();
            }
            switch (c) {
                case '\n' -> {
                    int old = symbol;
                    delete(1);
                    this.line++;
                    this.symbol = 0;
                    return new Token("\n", Token.Type.NL, line - 1, old);
                }
                case '(' -> {
                    return new Token("(", Token.Type.OPEN_BRACKET, line, delete(1));
                }
                case '[' -> {
                    return new Token("[", Token.Type.OPEN_CBRACKET, line ,delete(1));
                }
                case '{' -> {
                    return new Token("{", Token.Type.OPEN_FBRACKET, line, delete(1));
                }
                case ')' -> {
                    return new Token(")", Token.Type.CLOSE_BRACKET, line, delete(1));
                }
                case ']' -> {
                    return new Token("]", Token.Type.CLOSE_CBRACKET, line, delete(1));
                }
                case '}' -> {
                    return new Token("}", Token.Type.CLOSE_FBRACKET, line, delete(1));
                }
                case ':' -> {
                    return new Token(":", Token.Type.COLON, line, delete(1));
                }
                case ',' -> {
                    return new Token(",", Token.Type.COMMA, line, delete(1));
                }
                case '|' -> {
                    return new Token("|", Token.Type.PILLAR, line, delete(1));
                }
                case '-' -> {
                    var $ = str.charAt(1);
                    switch ($) {
                        case '>' -> {
                            return new Token("->", Token.Type.PTR, line, delete(2));
                        }
                        case ' ', '\t', ')' -> {
                            return new Token("-", Token.Type.OPERATION, line, delete(1));
                        }
                        default -> {
                            return parseNumber();
                        }
                    }
                }
                case '+', '/', '*' -> {
                    str = str.substring(1);
                    return new Token(String.valueOf(c), Token.Type.OPERATION, line, symbol);
                }
                default -> throw new RuntimeException("Error Token: " + c);
            }
        }
        return null;
    }

    protected Token parseNaming() {
        var naming = new StringBuilder();
        var c = str.charAt(0);
        while (Character.isLetter(c)) {
            naming.append(c);
            delete(1);
            c = str.charAt(0);
        }
        if (Character.isDigit(c)) {
            do {
                naming.append(c);
                delete(1);
                c = str.charAt(0);
            } while (Character.isDigit(c));
            return new Token(naming.toString(), Token.Type.TYPE, line, symbol);
        }
        return new Token(naming.toString(), Token.Type.NAMING, line, symbol);
    }

    protected Token parseNumber() {
        var num = new StringBuilder();
        cycle:
        while (true) {
            var c = str.charAt(0);
            switch (c) {
                case '-','0','1','2','3','4','5','6','7','8','9' -> {
                    num.append(c);
                    delete(1);
                }
                default -> {
                    break cycle;
                }
            }
        }
        return new Token(num.toString(), Token.Type.NUMBER, line, symbol);
    }

    protected void skipNLSpaces() {
        while (true) {
            skipSpaces();
            if (!str.isEmpty() && str.charAt(0) == '\n')
                delete(1);
            else break;
        }
    }

    protected void skipSpaces() {
        cycle:
        while (!str.isEmpty()) {
            switch (str.charAt(0)) {
                case ' ', '\t' -> delete(1);
                default -> {
                    break cycle;
                }
            }
        }
    }

    protected int delete(int count) {
        str = str.substring(count);
        symbol += count;
        return symbol;
    }
}
