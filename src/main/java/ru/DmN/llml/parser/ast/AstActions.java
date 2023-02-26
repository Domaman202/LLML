package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.List;
import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Блок действий
 */
public class AstActions extends AstExpression {
    /**
     * Действия
     */
    public final List<AstExpression> actions;

    /**
     * @param actions Действия
     */
    public AstActions(List<AstExpression> actions) {
        this.actions = actions;
    }

    @Override
    public String print(int offset) {
        var out = offset(offset).append("[Actions");
        for (var action : this.actions)
            out.append('\n').append(action.print(offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.actions.forEach(it -> it.iterate(consumer, this));
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.actions.get(this.actions.size() - 1).getType(context, function);
    }

    @Override
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return false;
    }
}
