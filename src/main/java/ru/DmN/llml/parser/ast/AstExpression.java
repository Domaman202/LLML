package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.parser.utils.CalculationOptions;
import ru.DmN.llml.parser.utils.IAstPrintable;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

public abstract class AstExpression implements IAstPrintable {
    public @NotNull AstExpression parent = AstEmptyExpression.INSTANCE;

    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        consumer.accept(this);
        this.parent = parent;
    }

    public void calc(AstContext context, AstFunction function) {
    }

    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        return false;
    }

    public @NotNull Type getType(AstContext context, AstFunction function) {
        return Type.UNKNOWN;
    }

    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return this.getType(context, function) == Type.UNKNOWN;
    }
}
