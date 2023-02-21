package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

import java.util.Arrays;

public class AstMath2Arg extends AstExpression {
    public final Operation operation;
    public AstExpression a, b;
    public Type rettype;

    public AstMath2Arg(Operation operation, AstExpression a, AstExpression b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
        this.rettype = Type.UNKNOWN;
    }

    public enum Operation {
        ADD("+", "add", "fadd", false),
        SUB("-", "sub", "fsub", false),
        MUL("*", "mul", "fmul", false),
        DIV("/", "sdiv", "fdiv", false),
        AND("&", "and", "and", false),
        OR("|", "or", "or", false),
        EQ("=", "eq", "oeq", true),
        NOT_EQ("!=", "ne", "une", true),
        GREAT(">", "gt", "ogt", true),
        GREAT_EQ(">=", "ge", "oge", true),
        LESS("<", "lt", "olt", true),
        LESS_EQ("<=", "le", "ole", true);

        public final String symbol;
        public final String iir, fir;
        public final boolean logicOutput;

        Operation(String symbol, String iir, String fir, boolean logicOutput) {
            this.symbol = symbol;
            this.iir = iir;
            this.fir = fir;
            this.logicOutput = logicOutput;
        }

        public static Operation of(String symbol) {
            return Arrays.stream(values()).filter(it -> it.symbol.equals(symbol)).findFirst().orElseThrow();
        }
    }
}
