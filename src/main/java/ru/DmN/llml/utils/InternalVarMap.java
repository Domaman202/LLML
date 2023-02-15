package ru.DmN.llml.utils;

import java.util.List;

public class InternalVarMap extends VariableMap<Variable> {
    public VariableMap<Variable> parent;

    public InternalVarMap(VariableMap<Variable> parent, List<Argument> args) {
        this.parent = parent;
        this.addAll(args);
    }

    public Variable create(Type type) {
        var var = new Variable(String.valueOf(this.internalCount()), type);
        this.list.add(var);
        return var;
    }

    protected int internalCount() {
        int i = 1;
        for (var var : this.list) {
            if (Character.isDigit(var.name.charAt(0))) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void add(Variable var) {
        var.name = String.valueOf(1 + this.list.size());
    }

    @Override
    public Variable get(Object key) {
        if (super.containsKey(key))
            return super.get(key);
        return this.parent.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key) || this.parent.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value) || this.parent.containsValue(value);
    }
}
