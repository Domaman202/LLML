package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Безусловный переход
 */
public class AstJump extends AstExpression {
    /**
     * Ссылка на метку
     */
    public final @NotNull AstLabelReference label;

    /**
     * @param label Ссылка на метку
     */
    public AstJump(@NotNull AstLabelReference label) {
        this.label = label;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset(offset(offset).append("[Jmp").append('\n').append(this.block.print(offset + 1)).append('\n'), offset + 1).append("\n"), offset + 1).append('\n'), offset).append(']').toString();
    }
}
