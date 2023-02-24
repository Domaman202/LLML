package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.utils.Type;

import static ru.DmN.llml.utils.PrintUtils.offset;

/**
 * Константа
 */
public class AstConstant extends AstExpression {
    /**
     * Значение
     */
    public final @Nullable Object value;

    /**
     * @param value Значение
     */
    public AstConstant(@Nullable Object value) {
        if (value instanceof String str) {
            var tmp = str.toUpperCase();
            if (tmp.equals("TRUE")) {
                this.value = true;
            } else if (tmp.equals("FALSE")) {
                this.value = false;
            } else {
                // todo: regex num check
                this.value = str.contains(".") ? (Object) Double.parseDouble(str) : (Object) Integer.parseInt(str);
            }
        } else this.value = value;
    }

    /**
     * Возвращает тип значения константы
     *
     * @return Тип значения константы
     */
    public @NotNull Type type() {
        if (this.value == null)
            return Type.VOID;
        if (this.value instanceof Boolean)
            return Type.I1;
        if (this.value instanceof Integer)
            return Type.I32;
        if (this.value instanceof Double)
            return Type.F32;
        return Type.UNKNOWN;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Constant [").append(this.value).append("]]").toString();
    }
}
