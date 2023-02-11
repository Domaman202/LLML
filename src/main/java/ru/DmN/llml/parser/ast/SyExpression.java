package ru.DmN.llml.parser.ast;

import ru.DmN.llml.parser.action.Action;

import java.util.ArrayList;
import java.util.List;

public class SyExpression extends SyElement {
    public List<Action> actions = new ArrayList<>();

    @Override
    public StringBuilder toString(int offset) {
        var out = super.toString(offset);
        out.append("[Expression");
        for (var action : actions)
            out.append("\n|").append("\t".repeat(offset)).append("|").append("\t".repeat(offset)).append(action.toString(offset));
        out.append("\n|").append("\t".repeat(offset)).append("|\t]");
        return out;
    }
}
