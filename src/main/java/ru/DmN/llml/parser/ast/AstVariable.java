package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.utils.Type;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Переменная
 */
public class AstVariable extends AstAbstractVariable {
    /**
     * Название
     */
    public final @NotNull String name;
    /**
     * Глобальная
     */
    public final boolean global;
    /**
     * Внешняя
     */
    public final boolean external;
    /**
     * Значение инициализации
     */
    public final @Nullable AstConstant value;

    public AstVariable(@NotNull String name, Type type, boolean global, boolean external, @Nullable AstConstant value) {
        super(type);
        this.name = name;
        this.global = global;
        this.external = external;
        this.value = value;
    }

    public AstVariable(@NotNull String name, Type type, boolean external, @NotNull AstConstant value) {
        this(name, type, true, external, value);
    }

    public AstVariable(@NotNull String name) {
        this(name, Type.UNKNOWN, false, false, null);
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[").append(this.global ? "Global " : "").append("Variable [").append(this.name).append(']').toString();
    }
}
