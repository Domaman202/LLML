package ru.DmN.llml.parser.ast;

public class AstVariableSet extends AstExpression {
    public final String name;
    public AstExpression value;

    public AstVariableSet(String name, AstExpression value) {
        this.name = name;
        this.value = value;
    }
}
