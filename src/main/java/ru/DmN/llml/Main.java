package ru.DmN.llml;

import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                foo(a: i32, b: i32): i32 = {
                    [a b + 2 /) -> |
                }
                """);

        var ctx = parser.parse();
        System.out.println("Parsed:");
        System.out.println(ctx);
        System.out.println();

        for (var it : ctx.functions) {
            if (it instanceof SyFunction fun) {
                for (int i = 0; ctx.calculateA(fun); i++) {
                    System.out.println("Calculation A[" + i + "]:");
                    System.out.println(ctx);
                    System.out.println();
                }
            }
        }

        var precompiler = new PreCompiler(ctx);
        precompiler.precompile();
        System.out.println("Precompiled:");
        System.out.println(precompiler.ctx);
    }
}