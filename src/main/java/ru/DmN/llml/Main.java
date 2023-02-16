package ru.DmN.llml;

import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.parser.InvalidTokenException;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.PreCompiler;

public class Main {
    public static void main(String[] args) {
        try {
            var parser = new Parser("""  
                    f(i: i32): i32 = {
                        [i 5 > !) -> x
                        @if(x) -> { 5 -> | }
                        i -> |
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
        } catch (InvalidTokenException exception) {
            System.err.println(exception.getMessage());
//            exception.printStackTrace();
        }
    }
}