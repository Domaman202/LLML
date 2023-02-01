package ru.DmN.llml.parser.action;

public class ActMathOperation extends Action {
    public Operation operation;

    public ActMathOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Math Operation): [").append(operation).append(']');
    }

    public enum Operation {
        ADD,
        SUB,
        MUL,
        DIV
    }
}
