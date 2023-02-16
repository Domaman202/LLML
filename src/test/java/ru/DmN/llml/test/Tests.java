package ru.DmN.llml.test;

import org.apache.commons.codec.digest.DigestUtils;
import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.PreCompiler;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Tests {
    public static void main(String[] args) throws IOException {
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
        test(6, 6, """
                a = 21
                
                set(i: i32): void = {
                    i -> a
                }
                
                f(b): i32 = {
                    [a b +) -> |
                }
                """, true, Type.UNKNOWN);
        test(7, 7, """
                f(a, b): i32 = {
                    [a, b) -> (c, d]
                }
                """, true, Type.UNKNOWN);
        test(8, 7, """
                f(a, b): i32 = {
                    [a) -> (c]
                    b -> d
                }
                """, true, Type.UNKNOWN);
        test(9, 9, """
                f(a: i32, b: i32): f32 = {
                    [a b +) -> |
                }
                """, true, Type.UNKNOWN);
    }

    private static void test(int tid, int cid, String code, boolean calcA, Type calcB) throws IOException {
        try (var out = new TestStream(tid, cid)) {
            var parser = new Parser(code);
            var ctx = parser.parse();
            ctx.functions.forEach(fun -> ctx.calculate(fun, calcA, calcB));
            var precompiler = new PreCompiler(ctx);
            precompiler.precompile();
            var compiler = new Compiler(precompiler.ctx);
            compiler.compile();
            out.println(compiler.out);
        } finally {
            try {
                if (Objects.equals(readChecksum("log/test" + tid), readChecksum("tlog/test" + cid))) {
                    System.out.println("Test №" + tid + " success!");
                } else {
                    System.err.println("Test №" + tid + " failed!");
                }
            } catch (IOException exception) {
                System.err.println("Test №" + tid + " check ended with error: " + exception.getMessage());
            }
        }
    }

    private static String readChecksum(String name) throws IOException {
        var file = new File(name + ".check");
        if (file.exists()) {
            try (var stream = new FileInputStream(file)) {
                return new String(stream.readAllBytes());
            }
        } else {
            var sum = DigestUtils.md5Hex(new FileInputStream(name + ".log"));
            try (var stream = new FileOutputStream(file)) {
                stream.write(sum.getBytes());
            }
            return sum;
        }
    }
}
