package ru.DmN.llml.parser.ast;

public class AstNamedActions extends AstExpression {
    public final String name;
    public final AstActions actions;

    public AstNamedActions(String name, AstActions actions) {
        this.name = name;
        this.actions = actions;
    }
}
