package ru.DmN.llml.parser.ast;

public class AstArgument extends AstAbstractVariable {
    public int i;
    public String name;

    public AstArgument(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name == null ? String.valueOf(this.i) : this.name;
    }
}
