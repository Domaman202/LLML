package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Value;
import ru.DmN.llml.utils.Variable;

public class PAStore extends PrecompiledAction {
    public Value value;
    public Variable to;

    public PAStore(Value value, Variable to) {
        this.value = value;
        this.to = to;
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append("(Load): [").append(value).append("] [").append(to.getName()).append("] (").append(to).append(')');
    }
}
