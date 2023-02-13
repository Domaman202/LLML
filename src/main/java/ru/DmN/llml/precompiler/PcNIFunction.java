package ru.DmN.llml.precompiler;

import ru.DmN.llml.utils.Argument;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.VariableMap;

public class PcNIFunction extends PcElement {
    public String name;
    public Type ret;
    public VariableMap<Argument> arguments;

    public PcNIFunction(String name, Type ret, VariableMap<Argument> arguments) {
        this.name = name;
        this.ret = ret;
        this.arguments = arguments;
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset).append("[fun ").append(name).append('(');
        for (int i = 0; i < arguments.size(); i++) {
            var arg = arguments.list.get(i);
            out.append(arg.name).append(": ").append(arg.type.name);
            if (i + 1 < arguments.size()) {
                out.append(", ");
            }
        }
        return out.append(')').append(": ").append(ret.name);
    }
}
