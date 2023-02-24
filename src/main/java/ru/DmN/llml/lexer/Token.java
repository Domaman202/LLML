package ru.DmN.llml.lexer;

/**
 * Токен
 */
public class Token {
    /**
     * Строка токена
     */
    public final String str;
    /**
     * Тип токена
     */
    public final Type type;
    /**
     * Линия токена
     */
    public final int line;
    /**
     * Символ начала токена
     */
    public final int symbol;

    public Token(String str, Type type, int line, int symbol) {
        this.str = str;
        this.type = type;
        this.line = line;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "["+this.type+"]("+this.line+','+this.symbol+')'+ str;
    }

    public enum Type {
        NL,
        OPEN_BRACKET,
        OPEN_CBRACKET,
        OPEN_FBRACKET,
        CLOSE_BRACKET,
        CLOSE_CBRACKET,
        CLOSE_FBRACKET,
        COLON,
        COMMA,
        PILLAR,
        ASSIGN,
        ANNOTATION,
        PTR,
        NAMING,
        TYPE,
        NUMBER,
        OPERATION,
        EOF,
        ERROR
    }
}
