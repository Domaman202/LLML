package ru.DmN.llml;

import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                fun foo(a, b): i32 {
                    [a b +) -> |
                }
                """);

        var ctx = parser.parse();
        System.out.println("Parsed:");
        System.out.println(ctx);
        System.out.println();

        for (var it : ctx.functions)
            if (it instanceof SyFunction fun)
                ctx.calculate(fun, true);
        System.out.println("Calculation A:");
        System.out.println(ctx);
        System.out.println();

        var precompiler = new PreCompiler(ctx);
        precompiler.precompile();
        System.out.println("Precompiled:");
        System.out.println(precompiler.ctx);
    }
}