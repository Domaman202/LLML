package ru.DmN.llml;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.parser.Parser;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                f(a: i16, b: i16): i32 = {
                    [a b +) -> |
                }
                """);
        var ctx = parser.parse();
        ctx.functions.forEach(fun -> ctx.calculate(fun, true, Type.UNKNOWN));
        System.out.println(ctx);
    }
}