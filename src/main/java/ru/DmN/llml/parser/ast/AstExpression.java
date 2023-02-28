package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.parser.utils.IAstPrintable;
import ru.DmN.llml.utils.Type;

import java.util.function.Consumer;

public abstract class AstExpression implements IAstPrintable {
    /**
     * Вышестоящие выражение
     */
    public @NotNull AstExpression parent = AstEmptyExpression.INSTANCE;

    /**
     * Итерация выражений
     *
     * @param consumer Приёмник выражения
     * @param parent   Вышестоящее выражение
     */
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        consumer.accept(this);
        this.parent = parent;
    }

    /**
     * Вычисление выражения
     *
     * @param context  Контекст
     * @param function Функция
     */
    public void calc(AstContext context, AstFunction function, CalculationOptions options) {
    }

    /**
     * Вычисление типа выражения
     *
     * @param context  Контекст
     * @param function Функция
     * @param options  Опции вычисления
     * @return Тип выражения вычислен?
     */
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        return false;
    }

    /**
     * Возвращает тип выражения
     *
     * @param context  Контекст
     * @param function Функция
     * @return Тип
     */
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return Type.UNKNOWN;
    }

    /**
     * @param context  Контекст
     * @param function Функция
     * @return Требуется ли вычислять тип выражения?
     */
    public boolean needTypeCalc(AstContext context, AstFunction function) {
        return this.getType(context, function) == Type.UNKNOWN;
    }
}
