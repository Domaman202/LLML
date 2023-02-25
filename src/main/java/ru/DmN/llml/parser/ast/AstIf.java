package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.DmN.llml.utils.PrintUtils.offset;

public class AstIf extends AstExpression {
    /**
     * Условие
     */
    public @NotNull AstExpression value;
    /**
     * Ссылка на блок (if)
     */
    public final @NotNull AstNamedActionsReference a;
    /**
     * Ссылка на блок (else)
     */
    public final @Nullable AstNamedActionsReference b;

    /**
     * @param value Условие
     * @param a     if
     * @param b     else
     */
    public AstIf(@NotNull AstExpression value, @NotNull AstNamedActionsReference a, @Nullable AstNamedActionsReference b) {
        this.value = value;
        this.a = a;
        this.b = b;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset(offset(offset).append("[If").append('\n').append(this.value.print(offset + 1)).append('\n'), offset + 1).append('[').append(this.a.name).append("]\n"), offset + 1).append('[').append(this.b == null ? "null" : this.b.name).append(']').append('\n'), offset).append(']').toString();
    }
}
