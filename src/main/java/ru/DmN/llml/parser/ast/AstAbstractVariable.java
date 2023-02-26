package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Абстрактное представление переменной
 */
public abstract class AstAbstractVariable extends AstExpression {
    /**
     * Тип переменной
     */
    public @NotNull Type type;

    public AstAbstractVariable() {
        this.type = Type.UNKNOWN;
    }

    public AstAbstractVariable(@NotNull Type type) {
        this.type = type;
    }

    /**
     * Возвращает имя переменной
     *
     * @return Имя переменной
     */
    public abstract @NotNull String getName();

    @Override
    public String print(int offset) {
        return offset(offset).append("[Variable [").append(this.getName()).append(']').toString();
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.type;
    }
}
