package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Variable;
import ru.DmN.llml.parser.ast.SyFunction;

public class ActSetVariable extends Action {
    public Variable variable;

    public ActSetVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return fun.locals.get(variable.name).type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Set Variable): [").append(variable.name).append("][").append(variable.type.name).append(']');
    }
}
