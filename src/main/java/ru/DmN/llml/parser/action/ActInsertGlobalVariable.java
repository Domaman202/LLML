package ru.DmN.llml.parser.action;

import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Variable;

public class ActInsertGlobalVariable extends Action {
    public Variable variable;

    public ActInsertGlobalVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return variable.type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Insert Global Variable): [").append(variable.name).append("][").append(variable.type).append(']');
    }
}
