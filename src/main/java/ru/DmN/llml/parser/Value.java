package ru.DmN.llml.parser;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.Variable;

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
