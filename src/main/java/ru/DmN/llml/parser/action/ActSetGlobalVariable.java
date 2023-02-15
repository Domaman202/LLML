package ru.DmN.llml.parser.action;

import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Variable;

public class ActSetGlobalVariable extends Action {
    public Variable variable;

    public ActSetGlobalVariable(Variable variable) {
        this.variable = variable;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return fun.locals.get(variable.name).type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Set Global Variable): [").append(variable.name).append("][").append(variable.type).append(']');
    }
}
