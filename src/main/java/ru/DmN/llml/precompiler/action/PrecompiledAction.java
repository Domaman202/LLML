package ru.DmN.llml.precompiler.action;

public class PrecompiledAction {
    public StringBuilder toString(int offset) {
        return new StringBuilder("|").append("\t".repeat(offset));
    }
}
