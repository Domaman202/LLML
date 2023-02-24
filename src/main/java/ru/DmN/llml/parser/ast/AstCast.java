package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Преобразование типов
 */
public class AstCast extends AstExpression {
    /**
     * Значение
     */
    public final @NotNull AstExpression value;
    /**
     * Тип
     */
    public final @NotNull Type type;

    /**
     * @param value Значение
     * @param type  Тип
     */
    public AstCast(@NotNull AstExpression value, @NotNull Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset).append("[Cast [").append(this.type.name).append("]\n").append(this.value.print(offset + 1)).append('\n'), offset).append(']').toString();
    }
}
