package ru.DmN.llml.parser.ast;

import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.util.VariableMap;

import java.util.List;

public abstract class SyAbstractFunction extends SyElement {
    public String name;
    public Type ret;
    public VariableMap<Argument> arguments;

    public SyAbstractFunction(String name, Type ret, List<Argument> arguments) {
        this.name = name;
        this.ret = ret;
        this.arguments = new VariableMap<>(arguments);
    }

    public String getName() {
        return '@' + name;
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
