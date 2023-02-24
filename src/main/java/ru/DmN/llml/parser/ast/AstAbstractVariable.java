package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public abstract class AstAbstractVariable extends AstExpression {
    public Type type;

    public AstAbstractVariable() {
        this.type = Type.UNKNOWN;
    }

    public AstAbstractVariable(Type type) {
        this.type = type;
    }

    public abstract String getName();
}
