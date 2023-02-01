package ru.DmN.llml.parser.action;

public class Action {
    public StringBuilder toString(int offset) {
        return new StringBuilder("|").append("\t".repeat(offset));
    }
}
