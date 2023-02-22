package ru.DmN.llml.parser.ast;

public class AstVariableGet extends AstExpression {
    public final String name;
    public AstAbstractVariable variable;

    public AstVariableGet(String name) {
        this.name = name;
    }
}
