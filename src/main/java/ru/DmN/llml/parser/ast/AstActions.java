package ru.DmN.llml.parser.ast;

import java.util.List;

import static ru.DmN.llml.utils.PrintUtils.offset;

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
}
