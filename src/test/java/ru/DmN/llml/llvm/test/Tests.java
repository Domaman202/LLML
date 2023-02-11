package ru.DmN.llml.llvm.test;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.precompiler.PreCompiler;

import java.io.*;

public class Tests {
    public static void main(String[] args) throws FileNotFoundException {
        new File("log").mkdir();
        test0();
        test1();
    }

    private static void test0() throws FileNotFoundException {
        try (var out = new PrintStream(new FileOutputStream("log/test0.log"))) {
            var parser = new Parser("""
                    foo(a, b): i32 = {
                        [a b + 2 /) -> |
                    }
                    """);

            var ctx = parser.parse();
            out.println("Parsed:");
            out.println(ctx);
            out.println();

            for (var it : ctx.functions) {
                if (it instanceof SyFunction fun) {
                    for (int i = 0; ctx.calculateA(fun); i++) {
                        out.println("Calculation A[" + i + "]:");
                        out.println(ctx);
                        out.println();
                    }
                }
            }

            var precompiler = new PreCompiler(ctx);
            precompiler.precompile();
            out.println("Precompiled:");
            out.println(precompiler.ctx);
        }
    }

    private static void test1() throws FileNotFoundException {
        try (var out = new PrintStream(new FileOutputStream("log/test1.log"))) {
            var parser = new Parser("""
                    foo(a, b): i32 = {
                        [a b + 2 /) -> |
                    }
                    """);

            var ctx = parser.parse();
            out.println("Parsed:");
            out.println(ctx);
            out.println();

            for (var it : ctx.functions) {
                if (it instanceof SyFunction fun) {
                    ctx.calculateB(fun, Type.I32);
                    out.println("Calculation B[" + 0 + "]:");
                    out.println(ctx);
                    out.println();
                }
            }

            var precompiler = new PreCompiler(ctx);
            precompiler.precompile();
            out.println("Precompiled:");
            out.println(precompiler.ctx);
        }
    }
}
