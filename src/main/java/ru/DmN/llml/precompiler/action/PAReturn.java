package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.utils.Value;
import ru.DmN.llml.utils.StringBuilderUtils;

public class PAReturn extends PrecompiledAction {
    public Value value;

    public PAReturn(Value value) {
        this.value = value;
    }

    @Override
    public StringBuilder toString(int offset) {
        var sb = super.toString(offset).append("(Return)");
        if (value != null) StringBuilderUtils.append(sb.append(": "), value);
        return sb;
    }
}
