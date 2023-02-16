package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Value;
import ru.DmN.llml.utils.Variable;
import ru.DmN.llml.parser.action.ActMath;
import ru.DmN.llml.utils.StringBuilderUtils;

import java.util.Arrays;

public class PAMath extends PrecompiledAction {
    public Operation oper;
    public Value a, b;
    public Variable out;

    public PAMath(Value a, Value b, Variable out, ActMath.Operation operation) {
        this.a = a;
        this.b = b;
        this.out = out;
        this.oper = Arrays.stream(Operation.values()).filter(it -> it.name().equals(operation.name())).findFirst().get();
        if (this.oper.logicOutput) {
            this.out.type = Type.I1;
        }
    }

    public Type getCalcType() {
        return this.a.type();
    }

    public Type getOutputType() {
        return this.out.type;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Math): [").append(oper).append("][").append(getOutputType()).append("] "), a), b).append(' '), out);
    }

    public enum Operation {
        ADD("add", "fadd", false, false),
        SUB("sub", "fsub", false, false),
        MUL("mul", "fmul", false, false),
        DIV("sdiv", "fdiv", false, false),
        GREAT("icmp ugt", "fcmp ugt", false, true),
        LESS("icmp ugt", "fcmp ugt", false, true),
        EQ("icmp eq", "fcmp eq", false, true),
        AND("and", null, true, true),
        OR("or", null, true, true),
        NOT("xor", null, true, true);

        public final String ir;
        public final String fir;
        public final boolean logicInput;
        public final boolean logicOutput;

        Operation(String ir, String fir, boolean logicInput, boolean logicOutput) {
            this.ir = ir;
            this.fir = fir;
            this.logicInput = logicInput;
            this.logicOutput = logicOutput;
        }

        public String getIr(boolean isFloat) {
            return isFloat ? fir : ir;
        }
    }
}
