package ru.DmN.llml.llvm.expr;

import ru.DmN.llml.llvm.Function;
import ru.DmN.llml.llvm.Variable;

public class CastExpr extends Expression {
    public Variable of, to;

    public CastExpr(Variable of, Variable to) {
        this.of = of;
        this.to = to;
    }

    @Override
    public void compile(Function func, StringBuilder out) {
        String oper, of, to;
        if (this.of.type.bits > this.to.type.bits) {
            oper = "trunc";
            of = this.to.type.name;
            to = this.of.type.name;
        } else {
            oper = "sext";
            of = this.of.type.name;
            to = this.to.type.name;
        }
        out.append(this.to.getName()).append(" = ").append(oper).append(' ').append(of).append(' ').append(this.of.getName()).append(" to ").append(to);
    }
}
