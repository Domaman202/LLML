package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.parser.utils.CalculationOptions;
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
    public @NotNull Type type;

    /**
     * @param value Значение
     */
    public AstConstant(@Nullable Object value) {
        if (value instanceof String str) {
            var tmp = str.toUpperCase();
            if (tmp.equals("TRUE")) {
                this.value = true;
                this.type = Type.I1;
            } else if (tmp.equals("FALSE")) {
                this.value = false;
                this.type = Type.I1;
            } else {
                // todo: regex num check
                if (str.contains(".")) {
                    this.value = Double.parseDouble(str);
                    this.type = Type.F32;
                } else {
                    this.value = Integer.parseInt(str);
                    this.type = Type.I32;
                }
            }
        } else {
            this.value = value;
            this.type = Type.UNKNOWN;
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
