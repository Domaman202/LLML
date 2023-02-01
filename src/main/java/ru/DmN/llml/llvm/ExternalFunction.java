package ru.DmN.llml.llvm;

import ru.DmN.llml.llvm.util.VariableMap;

import java.util.List;

public class ExternalFunction {
    public String name;
    public Type ret;
    public VariableMap<Argument> arguments;

    public ExternalFunction(String name, Type ret, List<Argument> arguments) {
        this.name = name;
        this.ret = ret;
        this.arguments = new VariableMap<>(arguments);
    }

    public String getName() {
        return '@' + name;
    }
}
