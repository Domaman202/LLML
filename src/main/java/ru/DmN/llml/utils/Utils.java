package ru.DmN.llml.utils;

import ru.DmN.llml.parser.ast.AstAbstractVariable;

import java.util.List;
import java.util.Optional;

public class Utils {
    public static <T extends AstAbstractVariable> Optional<T> findVariable(List<T> list, String name) {
        return list.stream().filter(it -> it.getName().equals(name)).findFirst();
    }
}
