package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.Objects;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Константа
 */
public class AstConstant extends AstExpression {
    /**
     * Значение
     */
    public @Nullable Object value;
    /**
     * Тип константы
     */
    public @NotNull Type type = Type.UNKNOWN;

    /**
     * @param value Значение
     */
    public AstConstant(@Nullable Object value) {
        if (value instanceof String str) {
            this.parse(str);
        } else this.value = value;
    }

    /**
     * @param value Значение
     */
    public AstConstant(@NotNull String value) {
        this.parse(value);
    }

    protected void parse(@NotNull String value) {
        var tmp = value.toUpperCase();
        if (tmp.equals("TRUE")) {
            this.value = true;
            this.type = Type.I1;
        } else if (tmp.equals("FALSE")) {
            this.value = false;
            this.type = Type.I1;
        } else {
            // todo: regex num check
            if (value.contains(".")) {
                var d = Double.parseDouble(value);
                var left = (long) d;
                var right = Long.parseLong(value.substring(value.indexOf('.') + 1) + '0');
                if (left >= Integer.MIN_VALUE && left <= Integer.MAX_VALUE && right >= Integer.MIN_VALUE && right <= Integer.MAX_VALUE) {
                    this.value = (float) d;
                    this.type = Type.F32;
                } else {
                    this.value = d;
                    this.type = Type.F64;
                }
            } else {
                var i = Long.parseLong(value);
                if (i >= Integer.MIN_VALUE && i <= Integer.MAX_VALUE) {
                    this.value = (int) i;
                    this.type = Type.I32;
                } else {
                    this.value = i;
                    this.type = Type.I64;
                }
            }
        }
    }

    /**
     * Меняет тип константы
     *
     * @param type Новый тип константы
     */
    public void cast(Type type) {
        if (type.fieldName().startsWith("I")) {
            if (value instanceof Double d) {
                value = (int) (double) d;
            }
        } else {
            if (value instanceof Integer i) {
                value = (double) (int) i;
            }
        }
        this.type = type;
    }

    @Override
    public String print(int offset) {
        return offset(offset).append("[Constant [").append(this.value).append("]]").toString();
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        this.cast(this.parent.getType(context, function));
        return this.type != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.type;
    }

    @Override
    public String toString() {
        return Objects.toString(this.value);
    }
}
