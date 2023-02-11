package ru.DmN.llml.parser.ast;

import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.Variable;
import ru.DmN.llml.llvm.VariableMap;

import java.util.ArrayList;
import java.util.List;

public class SyFunction extends SyAbstractFunction {
    public VariableMap<Variable> locals = new VariableMap<>();
    public List<SyExpression> expressions = new ArrayList<>();

    public SyFunction(String name, Type ret, List<Argument> arguments) {
        super(name, ret, arguments);
        this.locals.addAll(arguments);
    }

    public SyExpression expression() {
        var expr = new SyExpression();
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
