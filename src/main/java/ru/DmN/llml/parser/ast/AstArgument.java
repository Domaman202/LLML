package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Аргумент
 */
public class AstArgument extends AstAbstractVariable {
    /**
     * Номер аргумента
     */
    public int i;
    /**
     * Имя аргумента
     */
    public @Nullable String name;

    /**
     * @param name Название аргумента
     */
    public AstArgument(@Nullable String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return this.name == null ? String.valueOf(this.i) : this.name;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Argument ([").append(this.getName()).append("][").append(this.type.name).append("])").toString();
    }
}
