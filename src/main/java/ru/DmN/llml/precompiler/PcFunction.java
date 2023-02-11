package ru.DmN.llml.precompiler;

import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.Variable;
import ru.DmN.llml.llvm.VariableMap;
import ru.DmN.llml.parser.action.Action;

import java.util.ArrayList;
import java.util.List;

public class PcFunction extends PcNIFunction {
    public VariableMap<Variable> locals = new VariableMap<>();
    public List<Action> actions = new ArrayList<>();

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
