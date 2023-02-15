package ru.DmN.llml.utils;

public class GlobalVariable extends Variable {
    public GlobalVariable(String name, Type type) {
        super(name, type);
    }

    @Override
    public String getName() {
        return "@" + name;
    }
}
