package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;
import static ru.DmN.llml.precompiler.Precompiler.cast;

/**
 * Возврат из функции
 */
public class AstReturn extends AstExpression {
    /**
     * Значение
     */
    public @Nullable AstExpression value;

    /**
     *
     * @param value Значение
     */
    public AstReturn(@Nullable AstExpression value) {
        this.value = value;
    }

    @Override
    public String print(int offset) {
        var out = offset(offset).append("[Return");
        if (this.value != null)
            offset(out.append('\n').append(this.value.print(offset + 1)).append('\n'), offset);
        return out.append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        if (this.value != null) {
            this.value.iterate(consumer, this);
        }
    }

    @Override
    public void calc(AstContext context, AstFunction function, CalculationOptions options) {
        if (options.tc && this.value != null) {
            this.value = cast(context, function, this, this.value, function.ret);
        }
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        assert this.value != null;
        function.ret = this.value.getType(context, function);
        return function.ret != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return function.ret;
    }
}
