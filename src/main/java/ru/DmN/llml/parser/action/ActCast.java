package ru.DmN.llml.parser.action;

import ru.DmN.llml.llvm.Variable;

public class ActCast extends Action {
    public Variable of;
    public Variable to;

    public ActCast(Variable of, Variable to) {
        this.of = of;
        this.to = to;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Cast): [").append(of.name).append('(').append(of.type.name).append(")][").append(to.name).append('(').append(to.type.name).append(")]");
    }
}
