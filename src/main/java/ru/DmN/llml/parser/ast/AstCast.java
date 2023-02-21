package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstCast extends AstExpression {
    public final AstExpression value;
    public final Type type;

    public AstCast(AstExpression value, Type type) {
        this.value = value;
        this.type = type;
    }
}
