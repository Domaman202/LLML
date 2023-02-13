package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.ast.SyFunction;

public class ActReturn extends Action {
    public Type type;

    public ActReturn(Type type) {
        this.type = type;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Return): [").append(type.name).append(']');
    }
}
