package ru.DmN.llml.parser.ast;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Метка
 */
public class AstLabel extends AstExpression {
    /**
     * Название
     */
    public final String name;
    /**
     * Increment Tmp-variable Count
     */
    public final boolean itc;

    /**
     * @param name Название
     * @param itc Increment Tmp-variable Count
     */
    public AstLabel(String name, boolean itc) {
        this.name = name;
        this.itc = itc;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Label [").append(this.name).append("]]").toString();
    }
}
