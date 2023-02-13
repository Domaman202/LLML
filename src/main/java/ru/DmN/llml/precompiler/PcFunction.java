package ru.DmN.llml.precompiler;

import ru.DmN.llml.utils.Argument;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Variable;
import ru.DmN.llml.utils.VariableMap;
import ru.DmN.llml.precompiler.action.PrecompiledAction;

import java.util.ArrayList;
import java.util.List;

public class PcFunction extends PcNIFunction {
    public VariableMap<Variable> locals = new VariableMap<>();
    public List<PrecompiledAction> actions = new ArrayList<>();

    public PcFunction(String name, Type ret, VariableMap<Argument> arguments) {
        super(name, ret, arguments);
        this.locals.addAll(arguments.list);
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset);
        for (var action : actions)
            out.append("\n|").append("\t".repeat(offset)).append(action.toString(offset));
        return out.append("\n|").append("\t".repeat(offset)).append(']');
    }
}
