package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AstFunction {
    public final String name;
    public final List<AstArgument> arguments;
    public Type ret;
    public List<AstExpression> expressions;
    public final List<AstAbstractVariable> variables = new ArrayList<>();
    public int tmpVarCount = 0;

    public AstFunction(String name, List<AstArgument> arguments, Type ret) {
        this.name = name;
        this.arguments = arguments;
        this.ret = ret;
    }

    @SuppressWarnings("unchecked")
    public AstAbstractVariable variable(String name) {
        return Utils.findVariable((List<AstAbstractVariable>) (List<?>) this.arguments, name).orElseGet(() -> Utils.findVariable(this.variables, name).orElse(null));
    }

    public AstTmpVariable createTmpVariable() {
        return this.createTmpVariable(Type.UNKNOWN);
    }

    public AstTmpVariable createTmpVariable(Type type) {
        var var = new AstTmpVariable(++this.tmpVarCount);
        var.type = type;
        this.variables.add(var);
        return var;
    }
}
