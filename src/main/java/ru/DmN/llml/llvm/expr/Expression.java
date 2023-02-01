package ru.DmN.llml.llvm.expr;

import ru.DmN.llml.llvm.Function;

public abstract class Expression {
    public abstract void compile(Function func, StringBuilder out);
}
