package ru.DmN.llml.parser.ast;

public class AstIf extends AstExpression {
    public AstExpression value;
    public final AstNamedActionsReference a, b;

    public AstIf(AstExpression value, AstNamedActionsReference a, AstNamedActionsReference b) {
        this.value = value;
        this.a = a;
        this.b = b;
    }
}
