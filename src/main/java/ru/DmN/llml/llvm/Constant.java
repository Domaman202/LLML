package ru.DmN.llml.llvm;

public class Constant {
    public double value;
    public Type type;

    public Constant(int value) {
        this.value = value;
        this.type = Type.I32;
    }

    public Constant(double value) {
        this.value = value;
        this.type = Type.FLOAT;
    }
}
