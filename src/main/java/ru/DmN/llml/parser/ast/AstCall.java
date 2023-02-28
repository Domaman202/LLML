package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.List;
import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;
import static ru.DmN.llml.precompiler.Precompiler.cast;

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

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.arguments.forEach(it -> it.iterate(consumer, this));
    }

    @Override
    public void calc(AstContext context, AstFunction function, CalculationOptions options) {
        if (options.tc) {
            for (int i = 0; i < this.arguments.size(); i++) {
                this.arguments.set(i, cast(context, function, this, this.arguments.get(i), this.function.arguments.get(i).getType(context, function)));
            }
        }
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.function.ret;
    }

    @Override
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return false;
    }
}
