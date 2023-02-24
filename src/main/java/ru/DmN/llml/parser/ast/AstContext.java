package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AstContext {
    public final List<AstVariable> variables = new ArrayList<>();
    public final List<AstFunction> functions = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public AstAbstractVariable variable(AstFunction function, String name) {
        var var = Utils.findVariable((List<AstAbstractVariable>) (List<?>) this.variables, name);
        return var.orElseGet(() -> function.variable(name));
    }
}
