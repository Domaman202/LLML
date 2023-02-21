package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.List;

public class AstFunction {
    public final String name;
    public final List<AstArgument> arguments;
    public Type ret;
    public final List<AstExpression> expressions = new ArrayList<>();
    public final List<AstVariable> variables = new ArrayList<>();
    public int tmpVarCount = 0;

    public AstFunction(String name, List<AstArgument> arguments, Type ret) {
        this.name = name;
        this.arguments = arguments;
        this.ret = ret;
    }

    public AstVariable variable(String name) {
        return ((List<AstVariable>) (List<?>) this.arguments).stream().filter(it -> it.name.equals(name)).findFirst().orElseGet(() -> variables.stream().filter(it -> it.name.equals(name)).findFirst().orElse(null));
    }

    public AstVariable createTmpVariable() {
        var var = new AstVariable(String.valueOf(++this.tmpVarCount ));
        this.variables.add(var);
        return var;
    }
}
