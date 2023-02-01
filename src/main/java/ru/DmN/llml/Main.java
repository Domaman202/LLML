package ru.DmN.llml;

import ru.DmN.llml.parser.Parser;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser("""
                fun foo(a: i32, b: i32): i32 {
                    [a b +) -> c
                    [c 2 /) -> |
                }
                """);

        System.out.println(parser.parse());
    }
}