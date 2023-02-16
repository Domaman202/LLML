package ru.DmN.llml.parser.action;

import ru.DmN.llml.utils.Type;

public class ActInsertInteger extends Action {
    public int value;
    public Type type;

    public ActInsertInteger(int value) {
        this.value = value;
    }

    public ActInsertInteger(int value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Insert Int): [").append(value).append(']');
    }
}
