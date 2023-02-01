package ru.DmN.llml.llvm.expr;

import ru.DmN.llml.llvm.Function;
import ru.DmN.llml.llvm.Value;

public class VariableDefineExpr extends Expression {
    public String name;
    public Value value;

    public VariableDefineExpr(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void compile(Function func, StringBuilder out) {
        out.append(this.name).append(" = ").append(this.value.toString());
    }
}
