package ru.DmN.llml.utils;

public class InternalVarMap extends VariableMap<Variable> {
    public Variable create(Type type) {
        var var = new Variable(String.valueOf(1 + this.list.size()), type);
        this.list.add(var);
        return var;
    }

    public void add(Variable var) {
        var.name = String.valueOf(1 + this.list.size());
    }
}
