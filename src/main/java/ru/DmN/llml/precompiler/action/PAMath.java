package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.Value;
import ru.DmN.llml.llvm.Variable;
import ru.DmN.llml.parser.action.ActMath;
import ru.DmN.llml.utils.StringBuilderUtils;

public class PAMath extends PrecompiledAction {
    public ActMath.Operation operation;
    public Value a, b;
    public Variable out;

    public PAMath(Value a, Value b, Variable out, ActMath.Operation operation) {
        this.a = a;
        this.b = b;
        this.out = out;
        this.operation = operation;
    }

    public Type getType() {
        return this.out.type;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Math Operation): [").append(operation).append("][").append(getType().name).append("] "), a), b).append(' '), out);
    }
}
