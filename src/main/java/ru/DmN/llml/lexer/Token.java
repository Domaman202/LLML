package ru.DmN.llml.lexer;

public class Token {
    public final String str;
    public final Type type;
    public final int line;
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

        PTR,

        NAMING,
        TYPE,
        NUMBER,

        OPERATION
    }
}
