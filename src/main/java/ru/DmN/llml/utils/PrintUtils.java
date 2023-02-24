package ru.DmN.llml.utils;

public class PrintUtils {
    public static StringBuilder offset(int offset) {
        return new StringBuilder().append("|\t".repeat(offset));
    }

    public static StringBuilder offset(StringBuilder builder, int offset) {
        return builder.append("|\t".repeat(offset));
    }
}
