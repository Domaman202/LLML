package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

import java.util.Arrays;

public class AstMath1Arg extends AstExpression {
    public final AstMath1Arg.Operation operation;
    public AstExpression a;
    public Type rettype;

    public AstMath1Arg(AstMath1Arg.Operation operation, AstExpression a) {
        this.operation = operation;
        this.a = a;
        this.rettype = Type.UNKNOWN;
    }

    public enum Operation {
        NOT("!", false);

        public final String symbol;
        public final boolean logicOutput;

        Operation(String symbol, boolean logicOutput) {
            this.symbol = symbol;
            this.logicOutput = logicOutput;
        }

        public static AstMath1Arg.Operation of(String symbol) {
            return Arrays.stream(values()).filter(it -> it.symbol.equals(symbol)).findFirst().orElseThrow();
        }
    }
}
