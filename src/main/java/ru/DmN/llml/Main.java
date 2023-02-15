package ru.DmN.llml;

import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                add(a, b): i32 = { [a b +) -> | }
                
                f(c: i16, d: i16): i16 = {
                    [c d @call(add)) -> |
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

        var compiler = new Compiler(pc.ctx);
        compiler.compile();

        System.out.println("Compiled:");
        System.out.println(compiler.out);
        System.out.println();
    }
}