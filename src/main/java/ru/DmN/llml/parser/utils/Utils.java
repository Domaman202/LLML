package ru.DmN.llml.parser.utils;

import ru.DmN.llml.parser.ast.AstAbstractVariable;

import java.util.List;
import java.util.Optional;

public class Utils {
    public static <T extends AstAbstractVariable> Optional<T> findVariable(List<T> list, String name) {
        return list.stream().filter(it -> it.getName().equals(name)).findFirst();
    }

    public static StringBuilder offset(int offset) {
        return new StringBuilder().append("|\t".repeat(offset));
    }

    public static StringBuilder offset(StringBuilder builder, int offset) {
        return builder.append("|\t".repeat(offset));
    }
}
