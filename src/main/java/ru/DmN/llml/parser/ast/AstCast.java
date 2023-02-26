package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Преобразование типов
 */
public class AstCast extends AstExpression {
    /**
     * Значение
     */
    public @NotNull AstExpression value;
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

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.value.iterate(consumer, this);
    }

    @NotNull
    @Override
    public Type getType(AstContext context, AstFunction function) {
        return this.type;
    }
}
