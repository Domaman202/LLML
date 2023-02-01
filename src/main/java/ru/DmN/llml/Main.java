package ru.DmN.llml;

import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                fun foo(a: i32, b: i32): i32 {
                    [a b +) -> c
                    [c 2 /) -> |
                }
                """);

        var ctxA = parser.parse();
        System.out.println("Parsed:");
        System.out.println(ctxA);
        System.out.println();

        var precompiler = new PreCompiler(ctxA);
        precompiler.calcA();
        System.out.println("Precompiled:");
        System.out.println(precompiler.ctx);
    }
}