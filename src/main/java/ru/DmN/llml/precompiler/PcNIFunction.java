package ru.DmN.llml.precompiler;

import ru.DmN.llml.utils.Argument;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.VariableMap;

public class PcNIFunction extends PcElement {
    public String name;
    public Type ret;
    public VariableMap<Argument> args;

    public PcNIFunction(String name, Type ret, VariableMap<Argument> arguments) {
        this.name = name;
        this.ret = ret;
        this.args = arguments;
    }

    public String getName() {
        return "@" + name;
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset).append("[fun ").append(name).append('(');
        for (int i = 0; i < args.size(); i++) {
            var arg = args.list.get(i);
            out.append(arg.name).append(": ").append(arg.type);
            if (i + 1 < args.size()) {
                out.append(", ");
            }
        }
        return out.append(')').append(": ").append(ret);
    }
}
