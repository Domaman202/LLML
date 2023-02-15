package ru.DmN.llml.test;

import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.parser.ast.SyFunction;
import ru.DmN.llml.precompiler.PreCompiler;

import java.io.*;

public class Tests {
    public static void main(String[] args) throws FileNotFoundException {
        new File("log").mkdir();
        //
        test(0, 0, """
                foo(a, b): i32 = {
                    [a b + 2 /) -> |
                }
                """, true, Type.UNKNOWN);
        test(1, 1, """
                foo(a, b): i32 = {
                    [a b + 2 /) -> |
                }
                """, false, Type.I32);
        test(2, 2, """
                foo(a, b): i32 = {
                    [a b +) -> c
                    [c 2 /) -> |
                }
                """, true, Type.UNKNOWN);
        test(3, 3, """
                f(a, b): i32 = {
                    [a b +) -> c
                    [c) -> d
                    [d) -> |
                }
                """, true, Type.UNKNOWN);
        test(4, 3, """
                f(a, b): i32 = {
                    [a b +) -> c
                    c -> d
                    d -> |
                }
                """, true, Type.UNKNOWN);
        test(5, 5, """
                add(a, b): i32 = { [a b +) -> | }
                
                f(c: i16, d: i16): i16 = {
                    [c d @call(add)) -> |
                }
                """, true, Type.UNKNOWN);
    }

    private static void test(int tid, int cid, String code, boolean calcA, Type calcB) throws FileNotFoundException {
        try (var out = testStream(tid, cid)) {
            var parser = new Parser(code);
            var ctx = parser.parse();
            ctx.functions.forEach(fun -> ctx.calculate(fun, calcA, calcB));
            var precompiler = new PreCompiler(ctx);
            precompiler.precompile();
            var compiler = new Compiler(precompiler.ctx);
            compiler.compile();
            out.println(compiler.out);
        }
    }

    private static PrintStream testStream(int tid, int cid) throws FileNotFoundException {
        return new PrintStream(new FileOutputStream("log/test" + tid + ".log")) {
            @Override
            public void close() {
                super.close();
                if (readChecksum("log/test" + tid + ".log") == readChecksum("tlog/test" + cid + ".log")) {
                    System.out.println("Test №" + tid + " success!");
                } else {
                    System.err.println("Test №" + tid + " failed!");
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
