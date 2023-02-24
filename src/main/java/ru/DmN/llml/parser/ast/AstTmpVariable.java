package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

/**
 * Локальная tmp переменная
 */
public class AstTmpVariable extends AstAbstractVariable {
    /**
     * Номер переменной
     */
    public int i;

    public AstTmpVariable(int i) {
        this.i = i;
    }

    @Override
    public @NotNull String getName() {
        return String.valueOf(this.i);
    }
}
