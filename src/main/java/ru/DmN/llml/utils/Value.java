package ru.DmN.llml.utils;

public class Value {
    public Constant constant;
    public Variable variable;

    public Value(Constant constant) {
        this.constant = constant;
    }

    public Value(Variable variable) {
        this.variable = variable;
    }

    public Type type() {
        return this.variable == null ? Type.I32 : this.variable.type;
    }

    @Override
    public String toString() {
        return constant == null ? variable.getName() : String.valueOf(constant.value);
    }
}
