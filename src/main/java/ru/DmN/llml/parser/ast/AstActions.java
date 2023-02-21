package ru.DmN.llml.parser.ast;

import java.util.List;

public class AstActions extends AstExpression {
    public final List<AstExpression> actions;

    public AstActions(List<AstExpression> actions) {
        this.actions = actions;
    }
}
