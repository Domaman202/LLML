package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.parser.utils.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

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
        return offset(offset).append("[Get Variable [").append(this.name).append("][").append(this.variable == null ? "null" : this.variable.type.name).append("]]").toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        if (this.variable != null) {
            this.variable.iterate(consumer, this);
        }
    }

    @Override
    public void calc(AstContext context, AstFunction function) {
        this.variable = context.variable(function, this.name);
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        assert this.variable != null;
        this.variable.type = this.parent.getType(context, function);
        return this.variable.type != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        assert this.variable != null;
        return this.variable.getType(context, function);
    }
}
