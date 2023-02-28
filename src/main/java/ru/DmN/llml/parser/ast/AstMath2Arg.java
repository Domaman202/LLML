package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.precompiler.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.Arrays;
import java.util.function.Consumer;

import static ru.DmN.llml.parser.utils.Utils.offset;
import static ru.DmN.llml.precompiler.Precompiler.cast;
import static ru.DmN.llml.precompiler.Precompiler.grmt;

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

    @Override
    public void iterate(@NotNull Consumer<AstExpression> consumer, @NotNull AstExpression parent) {
        super.iterate(consumer, parent);
        this.a.iterate(consumer, this);
        this.b.iterate(consumer, this);
    }

    @Override
    public void calc(AstContext context, AstFunction function, CalculationOptions options) {
        if (options.tc) {
            this.a = cast(context, function, this, this.a, this.rettype);
            this.b = cast(context, function, this, this.b, this.rettype);
        }
    }

    @Override
    public boolean calcType(AstContext context, AstFunction function, CalculationOptions options) {
        var ta = this.a.getType(context, function);
        var tb = this.b.getType(context, function);
        var tr = this.parent.getType(context, function);
        if (options.tamtc) {
            var i = 0;
            if (ta != Type.UNKNOWN)
                i++;
            if (tb != Type.UNKNOWN)
                i++;
            if (tr != Type.UNKNOWN)
                i++;
            if (i <= 1) {
                return false;
            }
        }
        this.rettype = grmt(tr, grmt(ta, tb));
        return this.rettype != Type.UNKNOWN;
    }

    @Override
    public @NotNull Type getType(AstContext context, AstFunction function) {
        return this.rettype;
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
