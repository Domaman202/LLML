package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.ast.SyFunction;

public class ActMath extends Action {
    public Operation operation;
    public Type type;

    public ActMath(Operation operation, Type type) {
        this.operation = operation;
        this.type = type;
    }

    @Override
    public boolean isNeedCalc(SyFunction fun) {
        return type == Type.UNKNOWN;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Math Operation): [").append(operation).append("][").append(type.name).append(']');
    }

    public enum Operation {
        ADD,
        SUB,
        MUL,
        DIV
    }
}
