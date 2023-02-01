package ru.DmN.llml;

import ru.DmN.llml.lexer.Parser;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                fun foo(a: i32, b: i32) {
                    [a b + 2 /) -> |
                }
                """);

        System.out.println(parser.parse().compile());
    }
}