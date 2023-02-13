package ru.DmN.llml.precompiler.action;

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
        return super.toString(offset).append("(Cast): [").append(of.name).append('(').append(of.type.name).append(")][").append(to.name).append('(').append(to.type.name).append(")]");
    }
}
