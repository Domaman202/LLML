package ru.DmN.llml.parser.ast;

public class AstVariable extends AstAbstractVariable {
    public final String name;

    public AstVariable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
