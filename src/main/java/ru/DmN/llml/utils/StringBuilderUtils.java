package ru.DmN.llml.utils;

import ru.DmN.llml.llvm.Value;
import ru.DmN.llml.llvm.Variable;

public class StringBuilderUtils {
    public static StringBuilder append(StringBuilder sb, Value value) {
        return sb.append("[").append(value.toString()).append('(').append(value.type().name).append(")]");
    }

    public static StringBuilder append(StringBuilder sb, Variable variable) {
        return sb.append("[").append(variable.name).append('(').append(variable.type.name).append(")]");
    }
}
