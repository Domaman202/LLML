package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.ast.SyFunction;

public class ActMath extends Action {
    public Operation oper;
    public Type inputType;
    public Type outputType;

    public ActMath(Operation operation) {
        this.oper = operation;
        this.inputType = oper.logicInput ? Type.I1 : Type.UNKNOWN;
        this.outputType = operation.loginOutput ? Type.I1 : Type.UNKNOWN;
    }

    public ActMath(Operation operation, Type type) {
        this.oper = operation;
        this.inputType = this.outputType = type;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return this.inputType == Type.UNKNOWN || this.outputType == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Math Operation): [").append(oper).append("] [").append(this.inputType).append("][").append(this.outputType).append(']');
    }

    public enum Operation {
        ADD(false, false),
        SUB(false, false),
        MUL(false, false),
        DIV(false, false),

        GREAT(false, true),
        LESS(false, true),
        EQ(false, true),

        OR(true, true),
        AND(true, true),
        NOT(true, true);

        public final boolean logicInput;
        public final boolean loginOutput;

        Operation(boolean logicInput, boolean loginOutput) {
            this.logicInput = logicInput;
            this.loginOutput = loginOutput;
        }
    }
}
