package ru.DmN.llml.llvm;

public class Value {
    public Variable variable;
    public int constant;

    public Value(Variable variable) {
        this.variable = variable;
    }

    public Value(int constant) {
        this.constant = constant;
    }

    public Type type() {
        return this.variable == null ? Type.I32 : this.variable.type;
    }
}
