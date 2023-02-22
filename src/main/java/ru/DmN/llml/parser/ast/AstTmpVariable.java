package ru.DmN.llml.parser.ast;

public class AstTmpVariable extends AstAbstractVariable {
    public int i;

    public AstTmpVariable(int i) {
        this.i = i;
    }

    @Override
    public String getName() {
        return String.valueOf(this.i);
    }
}
