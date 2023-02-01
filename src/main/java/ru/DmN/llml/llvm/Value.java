package ru.DmN.llml.llvm;

public class Value {
    public int number;
    public Variable variable;

    public Value(int value) {
        this.number = value;
    }

    public Value(Variable value) {
        this.variable = value;
    }

    public Type type() {
        if (variable == null)
            return Type.I32;
        return variable.type;
    }

    @Override
    public String toString() {
        if (variable == null)
            return String.valueOf(number);
        return variable.getName();
    }
}
