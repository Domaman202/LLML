package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Цикл
 */
public class AstWhile extends AstExpression {
    /**
     * Условие
     */
    public final AstExpression value;
    /**
     * Действия
     */
    public final AstActions actions;
    /**
     * Id
     */
    public final int id;

    /**
     * @param value Условие
     * @param actions Дейвствия
     * @param id Id
     */
    public AstWhile(AstExpression value, AstActions actions, int id) {
        this.value = value;
        this.actions = actions;
        this.id = id;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset).append("[While\n").append(value.print(offset + 1)).append('\n').append(actions.print(offset + 1)).append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.value.iterate(consumer, this);
        this.actions.iterate(consumer, this);
    }
}
