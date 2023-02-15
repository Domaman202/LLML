package ru.DmN.llml.utils;

import java.util.List;

public class StringBuilderUtils {
    public static StringBuilder append(StringBuilder sb, Value value) {
        return sb.append("[(").append(value.toString()).append(")(").append(value.type().name).append(")]");
    }

    public static StringBuilder append(StringBuilder sb, Variable variable) {
        return sb.append("[(").append(variable.getName()).append(")(").append(variable.type.name).append(")]");
    }

    public static StringBuilder append(StringBuilder sb, List<Value> list) {
        for (var value : list) append(sb, value);
        return sb;
    }
}
