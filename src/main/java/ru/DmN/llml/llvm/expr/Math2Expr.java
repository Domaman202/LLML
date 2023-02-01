package ru.DmN.llml.llvm.expr;

import ru.DmN.llml.llvm.Function;
import ru.DmN.llml.llvm.Value;
import ru.DmN.llml.llvm.Variable;

public class Math2Expr extends Expression {
    public Value a;
    public Value b;
    public Variable result;
    public ru.DmN.llml.llvm.Type type;
    public Type operation;

    public Math2Expr(Value a, Value b, Variable result, ru.DmN.llml.llvm.Type type, Type operation) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.type = type;
        this.operation = operation;
    }

    @Override
    public void compile(Function func, StringBuilder out) {
        out.append(this.result.getName()).append(" = ").append(operation.operation).append(' ').append(this.type.name).append(' ').append(this.a.toString()).append(", ").append(this.b.toString());
    }

    public enum Type {
        ADD("add"),
        SUB("sub"),
        MUL("mul"),
        DIV("udiv");

        public final String operation;
        Type(String operation) {
            this.operation = operation;
        }
    }
}
