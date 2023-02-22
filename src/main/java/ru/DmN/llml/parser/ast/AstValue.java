package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstValue extends AstExpression {
    public AstConstant constant;
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

    public Type type() {
        return this.isConst() ? this.constant.type() : this.variable.type;
    }
}
