package ru.DmN.llml.llvm.test;

import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.precompiler.PreCompiler;

import java.io.*;

public class Tests {
    public static void main(String[] args) throws FileNotFoundException {
        new File("log").mkdir();
        //
        test(0, """
                foo(a, b): i32 = {
                    [a b + 2 /) -> |
                }
                """, true, Type.UNKNOWN);
        test(1, """
                foo(a, b): i32 = {
                    [a b + 2 /) -> |
                }
                """, false, Type.I32);
        test(2, """
                foo(a, b): i32 = {
                    [a b +) -> c
                    [c 2 /) -> |
                }
                """, true, Type.UNKNOWN);
    }

    private static void test(int id, String code, boolean calcA, Type calcB) throws FileNotFoundException {
        try (var out = testStream(id)) {
            var parser = new Parser(code);
            var ctx = parser.parse();
            ctx.functions.forEach(fun -> ctx.calculate(fun, calcA, calcB));
            var precompiler = new PreCompiler(ctx);
            precompiler.precompile();
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
