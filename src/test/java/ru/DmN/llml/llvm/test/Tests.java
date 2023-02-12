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
        try (var out = testStream(0)) {
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
        try (var out = testStream(1)) {
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

    private static PrintStream testStream(int i) throws FileNotFoundException {
        final var name = "log/test" + i + ".log";
        return new PrintStream(new FileOutputStream(name)) {
            @Override
            public void close() {
                super.close();
                if (readChecksum(name) == readChecksum("t" + name)) {
                    System.out.println("Test №" + i + " success!");
                } else {
                    System.err.println("Test №" + i + " failed!");
                }
            }
        };
    }

    private static int readChecksum(String name) {
        var checksum = 0;
        try (var stream = new FileInputStream(name)) {
            while (stream.available() > 0) {
                checksum += stream.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return checksum;
    }
}
