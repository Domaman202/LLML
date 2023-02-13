package ru.DmN.llml;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                f(a, b): i32 = {
                    [a b +) -> c
                    [c 2 /) -> |
                }
                """);
        var ctx = parser.parse();
        ctx.functions.forEach(fun -> ctx.calculate(fun, true, Type.UNKNOWN));

        System.out.println("Parsed:");
        System.out.println(ctx);
        System.out.println();

        var pc = new PreCompiler(ctx);
        pc.precompile();

        System.out.println("Precompiled:");
        System.out.println(pc.ctx);
        System.out.println();
    }
}