package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Пустое выражение
 */
public final class AstEmptyExpression extends AstExpression {
    public static final AstEmptyExpression INSTANCE = new AstEmptyExpression();

    private AstEmptyExpression() {
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Empty]").toString();
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return Type.UNKNOWN;
    }
}
