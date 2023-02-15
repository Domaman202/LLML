package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.StringBuilderUtils;
import ru.DmN.llml.utils.Variable;

public class PACast extends PrecompiledAction {
    public Variable of;
    public Variable to;

    public PACast(Variable of, Variable to) {
        this.of = of;
        this.to = to;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Cast): "), of).append(' '), to);
    }
}
