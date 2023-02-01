package ru.DmN.llml.parser.action;

public class ActSetVariable extends Action {
    public String name;

    public ActSetVariable(String name) {
        this.name = name;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Set Variable): [").append(name).append(']');
    }
}
