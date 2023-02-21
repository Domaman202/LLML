package ru.DmN.llml.parser.ast;

import java.util.List;

public class AstCall extends AstExpression {
    public final AstFunction function;
    public final List<AstExpression> arguments;

    public AstCall(AstFunction function, List<AstExpression> arguments) {
        this.function = function;
        this.arguments = arguments;
    }
}
