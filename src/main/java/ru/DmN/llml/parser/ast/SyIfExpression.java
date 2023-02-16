package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Variable;

public class SyIfExpression extends SyExpression {
    public Variable condition;

    public SyIfExpression(Variable condition) {
        this.condition = condition;
    }

    @Override
    public StringBuilder toString(int offset) {
        var out = new StringBuilder("|").append("\t".repeat(offset)).append("[If Expression (").append(this.condition.getName()).append(')');
        for (var action : this.actions)
            out.append("\n|").append("\t".repeat(offset)).append("|").append("\t".repeat(offset)).append(action.toString(offset));
        out.append("\n|").append("\t".repeat(offset)).append("|\t]");
        return out;
    }
}
