package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;

import static ru.DmN.llml.parser.utils.Utils.offset;

public class AstJump extends AstExpression {
    public final @NotNull AstNamedActionsReference block;

    public AstJump(@NotNull AstNamedActionsReference block) {
        this.block = block;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset(offset(offset).append("[Jmp").append('\n').append(this.block.print(offset + 1)).append('\n'), offset + 1).append("\n"), offset + 1).append('\n'), offset).append(']').toString();
    }
}
