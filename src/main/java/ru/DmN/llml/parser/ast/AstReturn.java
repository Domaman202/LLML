package ru.DmN.llml.parser.ast;

public class AstReturn extends AstExpression {
    public AstExpression value;

    public AstReturn(AstExpression value) {
        this.value = value;
    }
}
