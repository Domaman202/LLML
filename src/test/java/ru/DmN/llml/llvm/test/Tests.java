package ru.DmN.llml.llvm.test;

import ru.DmN.llml.llvm.Argument;
import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.llvm.expr.Math2Expr;

import java.util.List;

public class Tests {
    public static void main(String[] args) {
        for (int i = 1; i < 257; i++) {
            System.out.println("I" + i + "(" + i + "),");
        }
    }

    private static void test0() {
        var ctx = new Context();

        // i32 add(i32 arg$a, i32 arg$b)
        var func = ctx.defineFunction("add", Type.I32, List.of(new Argument("a", Type.I32), new Argument("b", Type.I32)));
        var arg$a = func.arguments.get("a");
        var arg$b = func.arguments.get("b");

        // (a b +) > |
        var expr = func.expression();
        expr.insert(arg$a);
        expr.insert(arg$b);
        expr.operation(Math2Expr.Type.ADD);
        expr.ret();

        System.out.println(ctx.compile());
    }
}
