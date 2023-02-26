package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Блок наименованых действий
 */
public class AstNamedActions extends AstExpression {
    /**
     * Название
     */
    public final @NotNull String name;
    /**
     * Действия
     */
    public final @NotNull AstActions actions;

    /**
     * @param name    Название
     * @param actions Действия
     */
    public AstNamedActions(@NotNull String name, @NotNull AstActions actions) {
        this.name = name;
        this.actions = actions;
    }

    @Override
    public String print(int offset) {
        var out = offset(offset).append("[Named Actions [").append(this.name).append(']');
        for (var action : this.actions.actions)
            out.append('\n').append(action.print(offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.actions.iterate(consumer, this);
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.actions.getType(context, function);
    }

    @Override
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return false;
    }
}
