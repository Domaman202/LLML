package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Value;

public class PAJmp extends PrecompiledAction {
    public Value condition;
    public PLabel labelA, labelB;

    public PAJmp(Value condition, PLabel labelA, PLabel labelB) {
        this.condition = condition;
        this.labelA = labelA;
        this.labelB = labelB;
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset).append("(Jump): [");
        if (this.condition != null) out.append(this.condition).append("] [");
        return out.append(this.labelA).append("][").append(this.labelB).append(']');
    }
}
