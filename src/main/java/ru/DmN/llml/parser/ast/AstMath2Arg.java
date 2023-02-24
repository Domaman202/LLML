package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.utils.Type;

import java.util.Arrays;

import static ru.DmN.llml.utils.PrintUtils.offset;

public class AstMath2Arg extends AstExpression {
    /**
     * Операция
     */
    public final @NotNull Operation operation;
    /**
     * Аргумент 'a'
     */
    public @NotNull AstExpression a;
    /**
     * Аргумент 'b'
     */
    public @NotNull AstExpression b;
    /**
     * Тип результата
     */
    public @NotNull Type rettype;

    /**
     * @param operation Операция
     * @param a         Аргумент 'a'
     * @param b         Аргумент 'b'
     */
    public AstMath2Arg(@NotNull Operation operation, @NotNull AstExpression a, @NotNull AstExpression b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
        this.rettype = Type.UNKNOWN;
    }

    @Override
    public String print(int offset) {
        return offset(offset(offset).append("[Math [").append(this.operation).append("][").append(this.rettype.name).append("]\n").append(this.a.print(offset + 1)).append('\n').append(this.b.print(offset + 1)).append('\n'), offset).append(']').toString();
    }

    public enum Operation {
        ADD("+", "add", "fadd", false),
        SUB("-", "sub", "fsub", false),
        MUL("*", "mul", "fmul", false),
        DIV("/", "sdiv", "fdiv", false),
        AND("&", "and", "and", false),
        OR("|", "or", "or", false),
        EQ("=", "eq", "oeq", true),
        NOT_EQ("!=", "ne", "one", true),
        GREAT(">", "sgt", "ogt", true),
        GREAT_EQ(">=", "sge", "oge", true),
        LESS("<", "slt", "olt", true),
        LESS_EQ("<=", "sle", "ole", true);

        /**
         * Символ операции
         */
        public final @NotNull String symbol;
        /**
         * Integer IR
         */
        public final @NotNull String iir;
        /**
         * Float IR
         */
        public final @NotNull String fir;
        /**
         * Логический результат
         */
        public final boolean logicOutput;

        /**
         * @param symbol      Символ операции
         * @param iir         Integer IR
         * @param fir         Float IR
         * @param logicOutput Логический результат
         */
        Operation(@NotNull String symbol, @NotNull String iir, @NotNull String fir, boolean logicOutput) {
            this.symbol = symbol;
            this.iir = iir;
            this.fir = fir;
            this.logicOutput = logicOutput;
        }

        /**
         * Возвращает операцию по символу
         *
         * @param symbol Символ
         * @return Операция
         */
        public static Operation of(String symbol) {
            return Arrays.stream(values()).filter(it -> it.symbol.equals(symbol)).findFirst().orElseThrow();
        }
    }
}
