package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Установка значения переменной
 */
public class AstVariableSet extends AstExpression {
    /**
     * Название
     */
    public final @NotNull String name;
    /**
     * Переменная
     */
    public @Nullable AstAbstractVariable variable;
    /**
     * Значение
     */
    public @NotNull AstExpression value;

    public AstVariableSet(@NotNull String name, @NotNull AstExpression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset).append("[Set Variable [").append(this.name).append("][").append(this.variable == null ? "null" : "X").append("]\n").append(this.value.print(offset + 1)).append('\n'), offset).append(']').toString();
    }
}
