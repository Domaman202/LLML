package ru.DmN.llml.parser.ast;

public class SyElement {
    public StringBuilder toString(int offset) {
        return new StringBuilder("|").append("\t".repeat(offset));
    }
}
