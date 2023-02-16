package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.*;

import java.util.ArrayList;
import java.util.List;

public class SyFunction extends SyAbstractFunction {
    public VariableMap<Variable> locals;
    public List<SyExpression> expressions = new ArrayList<>();

    public SyFunction(VariableMap<Variable> variables, String name, Type ret, List<Argument> arguments) {
        super(name, ret, arguments);
        this.locals = new InternalVarMap(variables, arguments);
    }

    public SyExpression expression() {
        var expr = new SyExpression();
        expressions.add(expr);
        return expr;
    }

    public SyIfExpression ifExpression(Variable condition) {
        var expr = new SyIfExpression(condition);
        expressions.add(expr);
        return expr;
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset);
        for (var expression : expressions)
            out.append("\n|").append("\t".repeat(offset)).append(expression.toString(offset));
        return out.append("\n|").append("\t".repeat(offset)).append(']');
    }
}
