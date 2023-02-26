package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Ссылка на блок наименованых действий
 */
public class AstNamedActionsReference extends AstExpression {
    /**
     * Название
     */
    public final @NotNull String name;

    /**
     *
     * @param name Название
     */
    public AstNamedActionsReference(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Named Actions Ref [").append(this.name).append(']').toString();
    }
}
