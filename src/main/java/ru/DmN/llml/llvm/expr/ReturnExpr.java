package ru.DmN.llml.llvm.expr;

import ru.DmN.llml.llvm.Function;
import ru.DmN.llml.llvm.Value;

public class ReturnExpr extends Expression {
    public Value value;

    public ReturnExpr(Value value) {
        this.value = value;
    }

    @Override
    public void compile(Function func, StringBuilder out) {
        out.append("ret ").append(this.value.variable.type.name).append(' ').append(this.value);
    }
}
