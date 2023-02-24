package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Получение значения переменной
 */
public class AstVariableGet extends AstExpression {
    /**
     * Название
     */
    public final @NotNull String name;
    /**
     * Переменная
     */
    public @Nullable AstAbstractVariable variable;

    /**
     * @param name Название
     */
    public AstVariableGet(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Get Variable [").append(this.name).append("][").append(this.variable == null ? "null" : "X").append("]]").toString();
    }
}
