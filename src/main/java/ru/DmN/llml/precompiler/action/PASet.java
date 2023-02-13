package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.StringBuilderUtils;
import ru.DmN.llml.utils.Value;
import ru.DmN.llml.utils.Variable;

public class PASet extends PrecompiledAction {
    public Value value;
    public Variable var;

    public PASet(Value value, Variable var) {
        this.value = value;
        this.var = var;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Set): "), value).append(' '), var);
    }
}
