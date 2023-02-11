package ru.DmN.llml.parser.action;

public class ActInsertInteger extends Action {
    public int value;

    public ActInsertInteger(int value) {
        this.value = value;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Insert Int): [").append(value).append(']');
    }
}
