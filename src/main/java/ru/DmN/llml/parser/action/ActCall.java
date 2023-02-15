package ru.DmN.llml.parser.action;

public class ActCall extends Action {
    public String fun;

    public ActCall(String function) {
        this.fun = function;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Call): [").append(fun).append(']');
    }
}
