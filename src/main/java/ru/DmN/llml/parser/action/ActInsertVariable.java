package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Variable;
import ru.DmN.llml.parser.ast.SyFunction;

public class ActInsertVariable extends Action {
    public Variable variable;

    public ActInsertVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return variable.type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Insert Variable): [").append(variable.name).append("][").append(variable.type.name).append(']');
    }
}
