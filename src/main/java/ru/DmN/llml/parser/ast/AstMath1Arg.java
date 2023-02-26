package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.parser.utils.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.Arrays;
import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;

public class AstMath1Arg extends AstExpression {
    /**
     * Операция
     */
    public final @NotNull AstMath1Arg.Operation operation;
    /**
     * Аргумент 'a'
     */
    public @NotNull AstExpression a;
    /**
     * Тип результата
     */
    public @NotNull Type rettype;

    /**
     * @param operation Операция
     * @param a         Аргумент 'a'
     */
    public AstMath1Arg(AstMath1Arg.@NotNull Operation operation, @NotNull AstExpression a) {
        this.operation = operation;
        this.a = a;
        this.rettype = Type.UNKNOWN;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset).append("[Math [").append(this.operation).append("][").append(this.rettype.name).append("]\n").append(this.a.print(offset + 1)).append('\n'), offset).append(']').toString();
    }

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.a.iterate(consumer, this);
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        this.rettype = this.operation.logicOutput ? Type.I1 : this.a.getType(context, function);
        return this.rettype != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.rettype;
    }

    public enum Operation {
        NOT("!", false);

        /**
         * Символ операции
         */
        public final @NotNull String symbol;
        /**
         * Логический результат
         */
        public final boolean logicOutput;

        /**
         *
         * @param symbol Символ операции
         * @param logicOutput Логический результат
         */
        Operation(@NotNull String symbol, boolean logicOutput) {
            this.symbol = symbol;
            this.logicOutput = logicOutput;
        }

        /**
         * Возвращает операцию по символу
         *
         * @param symbol Символ
         * @return Операция
         */
        public static AstMath1Arg.Operation of(String symbol) {
            return Arrays.stream(values()).filter(it -> it.symbol.equals(symbol)).findFirst().orElseThrow();
        }
    }
}
