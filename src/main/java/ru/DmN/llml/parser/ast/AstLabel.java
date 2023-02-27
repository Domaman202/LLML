package ru.DmN.llml.parser.ast;

import static ru.DmN.llml.parser.utils.Utils.offset;

public class AstLabel extends AstExpression {
    public final String name;
    public final boolean itc;

    public AstLabel(String name, boolean itc) {
        this.name = name;
        this.itc = itc;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Label [").append(this.name).append("]]").toString();
    }
}
