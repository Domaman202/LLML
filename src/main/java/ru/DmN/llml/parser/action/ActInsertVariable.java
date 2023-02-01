package ru.DmN.llml.parser.action;

import ru.DmN.llml.llvm.Variable;

public class ActInsertVariable extends Action {
    public Variable variable;

    public ActInsertVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Insert Variable): [").append(variable.name).append("][").append(variable.type.name).append(']');
    }
}
