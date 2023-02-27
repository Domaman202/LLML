package ru.DmN.llml.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Лексер
 */
public class Lexer {
    /**
     * Исходный код
     */
    public final String src;
    /**
     * Токены
     */
    public final List<Token> tokens;
    /**
     * Указатель на токен
     */
    public int ptr;

    /**
     * @param str Код для обработки
     */
    public Lexer(String str) {
        this.src = str;
        this.tokens = this.parseTokens();
    }

    /**
     * Сдвигает "ptr" и возвращает текущий токен
     *
     * @return Токен
     */
    public Token next() {
        var token = this.tokens.get(this.ptr);
        this.ptr++;
        return token;
    }

    /**
     * Парсит все токены (вызывается в конструкторе)
     *
     * @return Список токенов
     */
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
        /**
         * Обрабатываемая строка
         */
        public String str;
        /**
         * Текущая линия
         */
        public int line = 1;
        /**
         * Текущий символ
         */
        public int symbol = 0;

        /**
         *
         * @param str Строка для обработки
         */
        public TokenParser(String str) {
            this.str = str;
        }

        /**
         * Обрабатывает токен
         * @return Токен
         */
        protected Token parseToken() {
            skipSpaces();
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

        /**
         * Парсинг токена названия
         * @return Токен
         */
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

        /**
         * Парсит токен числа
         * @return Токен
         */
        protected Token parseNumber() {
            var num = new StringBuilder();
            cycle:
            while (true) {
                var c = str.charAt(0);
                switch (c) {
                    case '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
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

        /**
         * Пропуск пробелов
         */
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

        /**
         * Удаляет один символ из "str"
         * @param count Кол-во символов
         * @return Номер символа
         */
        protected int delete(int count) {
            str = str.substring(count);
            symbol += count;
            return symbol;
        }
    }
}
