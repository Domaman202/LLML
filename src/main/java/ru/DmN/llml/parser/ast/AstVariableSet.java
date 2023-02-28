package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;
import static ru.DmN.llml.precompiler.Precompiler.gnut;

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
        return offset(offset(offset).append("[Set Variable [").append(this.name).append("][").append(this.variable == null ? "null" : this.variable.type.name).append("]\n").append(this.value.print(offset + 1)).append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        if (this.variable != null)
            this.variable.iterate(consumer, this);
        this.value.iterate(consumer, this);
    }

    @Override
    public void calc(AstContext context, AstFunction function, CalculationOptions options) {
        if (!options.tc) {
            this.variable = context.variable(function, this.name);
        }
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        assert this.variable != null;
        this.variable.type = gnut(this.value.getType(context, function), this.parent.getType(context, function));
        return this.variable.type != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        assert this.variable != null;
        return this.variable.getType(context, function);
    }
}
