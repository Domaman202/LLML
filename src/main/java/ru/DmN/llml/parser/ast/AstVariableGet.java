package ru.DmN.llml.parser.ast;

public class AstVariableGet extends AstExpression {
    public final String name;

    public AstVariableGet(String name) {
        this.name = name;
    }
}
