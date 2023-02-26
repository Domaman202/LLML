package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

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

    @Override
    public String print(int offset) {
        return this.isConst() ? this.constant.print(offset) : this.variable.print(offset);
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        if (this.isConst())
            this.constant.iterate(consumer, this);
        else this.variable.iterate(consumer, this);
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.isConst() ? this.constant.getType(context, function) : this.variable.getType(context, function);
    }

    @Override
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return false;
    }
}
