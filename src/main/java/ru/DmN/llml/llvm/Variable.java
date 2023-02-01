package ru.DmN.llml.llvm;

public class Variable {
    public String name;
    public Type type;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return "%" + name;
    }
}
