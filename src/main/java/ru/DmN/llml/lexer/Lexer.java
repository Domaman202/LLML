package ru.DmN.llml.lexer;

import ru.DmN.llml.parser.InvalidTokenException;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public final String src;
    public final List<Token> tokens;
    public int ptr;

    public Lexer(String str) {
        this.src = str;
        this.tokens = this.parseTokens();
    }

    public Token next() {
        var token = this.tokens.get(this.ptr);
        this.ptr++;
        return token;
    }

    protected List<Token> parseTokens() {
        var tokens = new ArrayList<Token>();
        var parser = new TokenParser(src);
        var token = parser.parseToken();
        while (token.type != Token.Type.EOF) {
            tokens.add(token);
            token = parser.parseToken();
        }
        tokens.add(token);
        return tokens;
    }

    public class TokenParser {
        public String str;
        public int line;
        public int symbol;

        public TokenParser(String str) {
            this.str = str;
        }

        protected Token parseToken() {
            skipNLSpaces();
            if (str.length() > 0) {
                var c = str.charAt(0);
                if (Character.isDigit(c))
                    return parseNumber();
                if (Character.isLetter(c))
                    return parseNaming();
                switch (c) {
                    case '\n' -> {
                        int old = symbol;
                        delete(1);
                        line++;
                        symbol = 0;
                        return new Token("\n", Token.Type.NL, line - 1, old);
                    }
                    case '(' -> {
                        return new Token("(", Token.Type.OPEN_BRACKET, line, delete(1));
                    }
                    case '[' -> {
                        return new Token("[", Token.Type.OPEN_CBRACKET, line, delete(1));
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
                    case '=' -> {
                        return new Token("=", Token.Type.ASSIGN, line, delete(1));
                    }
                    case '@' -> {
                        return new Token("@", Token.Type.ANNOTATION, line, delete(1));
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
                    case '+', '/', '*', '&', '!' -> {
                        delete(1);
                        return new Token(String.valueOf(c), Token.Type.OPERATION, line, symbol);
                    }
                    case '>', '<' -> {
                        var token = String.valueOf(c);
                        if (str.charAt(1) == '=') {
                            token += '=';
                            delete(1);
                        }
                        return new Token(token, Token.Type.OPERATION, line, delete(1));
                    }
                    default -> {
                        delete(1);
                        throw InvalidTokenException.create(src, new Token(String.valueOf(c), Token.Type.ERROR, line, symbol));
                    }
                }
            } else return new Token("", Token.Type.EOF, line, symbol);
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
                return new Token(naming.toString(), Token.Type.TYPE, line, symbol); // todo: regex "(i|f)(\d)+"
            }
            var name = naming.toString();
            return new Token(name, name.equals("void") ? Token.Type.TYPE : Token.Type.NAMING, line, symbol);
        }

        protected Token parseNumber() {
            var num = new StringBuilder();
            cycle:
            while (true) {
                var c = str.charAt(0);
                switch (c) {
                    case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
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

        public void skipNLSpaces() {
            while (true) {
                skipSpaces();
                if (!str.isEmpty() && str.charAt(0) == '\n') {
                    delete(1);
                    line++;
                    symbol = 0;
                } else break;
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
}
