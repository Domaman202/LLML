package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Variable;

public class PLabel extends PrecompiledAction {
    public Variable label;

    public PLabel(Variable label) {
        this.label = label;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Label): [").append(label.getName()).append(']');
    }

    @Override
    public String toString() {
        return label == null ? "#" : label.getName();
    }
}
