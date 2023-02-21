package ru.DmN.llml.parser.ast;

public class AstNamedActionsReference extends AstExpression {
    public final String name;

    public AstNamedActionsReference(String name) {
        this.name = name;
    }
}
