package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

/**
 * Значение (константа/переменная)
 */
public class AstValue extends AstExpression {
    /**
     * Константа
     */
    public AstConstant constant;
    /**
     * Переменная
     */
    public AstAbstractVariable variable;

    public AstValue(AstConstant constant) {
        this.constant = constant;
    }

    public AstValue(AstAbstractVariable variable) {
        this.variable = variable;
    }

    public void set(AstConstant constant) {
        this.constant = constant;
        this.variable = null;
    }

    public void set(AstAbstractVariable variable) {
        this.constant = null;
        this.variable = variable;
    }

    public boolean isConst() {
        return this.variable == null;
    }

    public boolean isVariable() {
        return this.constant == null;
    }

    public @NotNull Type type() {
        return this.isConst() ? this.constant.type() : this.variable.type;
    }

    @Override
    public String print(int offset) {
        return this.isConst() ? this.constant.print(offset) : this.variable.print(offset);
    }
}
