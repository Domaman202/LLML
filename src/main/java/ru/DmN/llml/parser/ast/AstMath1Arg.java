package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.Arrays;

import static ru.DmN.llml.utils.PrintUtils.offset;

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
        return offset(offset(offset).append("[Math [").append(this.operation).append("][").append(this.rettype.name).append("]\n").append(this.a.print(offset + 1)).append('\n').append('\n'), offset).append(']').toString();
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
