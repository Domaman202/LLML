package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

public class AstWhile extends AstExpression {
    public final AstExpression value;
    public final AstActions actions;
    public final int id;

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
