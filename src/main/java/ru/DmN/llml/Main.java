package ru.DmN.llml;

import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.parser.InvalidTokenException;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.Precompiler;
import ru.DmN.llml.utils.PrintUtils;

public class Main {
    public static void main(String[] args) {
        try {
            var lexer = new Lexer("""
                    f(a: i32, b: i32, act: i1): i32 -> {
                          @if(act add sub)
                      
                          @label(add) -> {
                              [+ a b]
                          }
                      
                          @label(sub) -> {
                              [- a b]
                          }
                      }
                    """);
            var parser = new Parser(lexer);
            var ctx = parser.parse();

            System.out.println("Parsed:");
            System.out.println(PrintUtils.print(ctx, 0));
            System.out.println();

            var precompiler = new Precompiler(ctx);
            precompiler.precompile();
            System.out.println("Precompiled: ");
            System.out.println(PrintUtils.print(ctx, 0));
            System.out.println();

            var compiler = new Compiler(ctx);
            compiler.compile();
            System.out.println("Compiled:");
            System.out.println(compiler.out);
            System.out.println();
        } catch (InvalidTokenException exception) {
//            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}