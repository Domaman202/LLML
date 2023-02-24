package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.Nullable;

import static ru.DmN.llml.utils.PrintUtils.offset;

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
}
