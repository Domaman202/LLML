package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Variable;

public class PALoad extends PrecompiledAction {
    public Variable of;
    public Variable to;

    public PALoad(Variable of, Variable to) {
        this.of = of;
        this.to = to;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Load): [").append(of.getName()).append("] [").append(to.getName()).append("] (").append(of.type).append(')');
    }
}
