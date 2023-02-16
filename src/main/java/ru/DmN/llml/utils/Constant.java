package ru.DmN.llml.utils;

public class Constant {
    public double value;
    public Type type;

    public Constant(String value) {
        if (value.indexOf('.') == -1) {
            this.value = Integer.parseInt(value);
            this.type = Type.I32;
        } else {
            this.value = Double.parseDouble(value);
            this.type = Type.F32;
        }
    }

    public Constant(int value) {
        this.value = value;
        this.type = Type.I32;
    }

    public Constant(double value) {
        this.value = value;
        this.type = Type.F32;
    }

    @Override
    public String toString() {
        return type.name.startsWith("F") ? String.valueOf(value) : String.valueOf((int) value);
    }
}
