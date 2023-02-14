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
    }

    public Type getType() {
        return this.out.type;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Math): [").append(oper).append("][").append(getType().name).append("] "), a), b).append(' '), out);
    }

    public enum Operation {
        ADD("add"),
        SUB("sub"),
        MUL("mul"),
        DIV("sdiv");

        public final String ir;

        Operation(String ir) {
            this.ir = ir;
        }
    }
}
