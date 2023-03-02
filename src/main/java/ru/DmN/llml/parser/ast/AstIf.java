package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Условный переход
 */
public class AstIf extends AstExpression {
    /**
     * Условие
     */
    public @NotNull AstExpression value;
    /**
     * Ссылка на метку (if)
     */
    public final @NotNull AstLabelReference a;
    /**
     * Ссылка на метку (else)
     */
    public final @NotNull AstLabelReference b;

    /**
     * @param value Условие
     * @param a     if
     * @param b     else
     */
    public AstIf(@NotNull AstExpression value, @NotNull AstLabelReference a, @NotNull AstLabelReference b) {
        this.value = value;
        this.a = a;
        this.b = b;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset(offset(offset).append("[If").append('\n').append(this.value.print(offset + 1)).append('\n'), offset + 1).append('[').append(this.a.name).append("]\n"), offset + 1).append('[').append(this.b == null ? "null" : this.b.name).append(']').append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.value.iterate(consumer, this);
        this.a.iterate(consumer, this);
        if (this.b != null) {
            this.b.iterate(consumer, this);
        }
    }

    @Override
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return false;
    }
}
