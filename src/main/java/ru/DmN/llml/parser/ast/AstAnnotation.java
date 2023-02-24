package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Аннотация
 */
public class AstAnnotation extends AstExpression {
    /**
     * Название аннотации
     */
    public final @NotNull String name;
    /**
     * Аргументы
     */
    public final List<AstExpression> arguments;

    /**
     * @param name Название аннотации
     */
    public AstAnnotation(@NotNull String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }

    @Override
    public String print(int offset) {
        var out = offset(offset).append("[Annotation [").append(this.name).append(']');
        for (var argument : this.arguments)
            out.append('\n').append(argument.print(offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }
}
