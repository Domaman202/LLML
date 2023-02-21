package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstVariable extends AstExpression {
    public final String name;
    public Type type;

    public AstVariable(String name) {
        this.name = name;
        this.type = Type.UNKNOWN;
    }
}
