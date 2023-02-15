package ru.DmN.llml.precompiler;

import ru.DmN.llml.utils.InitializedGlobalVariable;
import ru.DmN.llml.utils.Variable;
import ru.DmN.llml.utils.VariableMap;

import java.util.ArrayList;
import java.util.List;

public class PcContext {
    public List<PcNIFunction> functions = new ArrayList<>();
    public VariableMap<Variable> variables = new VariableMap<>();

    @Override
    public String toString() {
        var out = new StringBuilder("[Context");
        for (var variable : variables.list) {
            out.append('\n').append("|\t[var ").append(variable.name).append(": ").append(variable.type);
            if (variable instanceof InitializedGlobalVariable var) out.append(" = ").append(var.constant);
            out.append(']');
        }
        out.append("\n|");
        for (var function : functions)
            out.append('\n').append(function.toString(1));
        return out.append("\n]").toString();
    }
}
