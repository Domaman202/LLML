package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Вызов функции
 */
public class AstCall extends AstExpression {
    /**
     * Функция
     */
    public final @NotNull AstFunction function;
    /**
     * Аргументы
     */
    public final @NotNull List<AstExpression> arguments;

    /**
     * @param function  Функция
     * @param arguments Аргументы
     */
    public AstCall(@NotNull AstFunction function, @NotNull List<AstExpression> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public String print(int offset) {
        var out = offset(offset(offset).append("[Call\n"), offset + 1).append('[').append(this.function.name).append(']');
        for (var argument : this.arguments)
            out.append('\n').append(argument.print(offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }
}
