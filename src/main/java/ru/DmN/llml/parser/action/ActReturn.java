package ru.DmN.llml.parser.action;

public class ActReturn extends Action {
    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Return)");
    }
}
