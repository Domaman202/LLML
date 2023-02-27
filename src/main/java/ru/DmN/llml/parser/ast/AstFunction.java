package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.parser.utils.IAstPrintable;
import ru.DmN.llml.parser.utils.Utils;
import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.DmN.llml.parser.utils.Utils.offset;

/**
 * Функция
 */
public class AstFunction implements IAstPrintable {
    /**
     * Имя функции
     */
    public final @NotNull String name;
    /**
     * Аргументы функции
     */
    public final @NotNull List<AstArgument> arguments;
    /**
     * Тип возвращаемого значения
     */
    public @NotNull Type ret;
    /**
     * Выражения
     */
    public @Nullable List<AstExpression> expressions;
    /**
     * Локальные переменные
     */
    public final @NotNull List<AstAbstractVariable> variables;
    public final Map<String, Integer> variableSetMap = new HashMap<>();
    public int tmpVarCount = 0;

    public AstFunction(@NotNull String name, @NotNull List<AstArgument> arguments, @NotNull Type ret) {
        this.name = name;
        this.arguments = arguments;
        this.ret = ret;
        this.variables = new ArrayList<>();
    }

    /**
     * Ищет локальную переменную
     *
     * @param name Имя переменной
     * @return Переменная
     */
    @SuppressWarnings("unchecked")
    public @Nullable AstAbstractVariable variable(@NotNull String name) {
        return Utils.findVariable((List<AstAbstractVariable>) (List<?>) this.arguments, name).orElseGet(() -> Utils.findVariable(this.variables, name).orElse(null));
    }

    /**
     * Создаёт локальную tmp переменную
     *
     * @return Переменная
     */
    public @NotNull AstTmpVariable createTmpVariable() {
        return this.createTmpVariable(Type.UNKNOWN);
    }

    /**
     * Создаёт локальную tmp переменную
     *
     * @param type Тип переменной
     * @return Переменная
     */
    public @NotNull AstTmpVariable createTmpVariable(Type type) {
        var var = new AstTmpVariable(++tmpVarCount);
        var.type = type;
        variables.add(var);
        return var;
    }

    @Override
    public String print(int offset) {
        var out = offset(offset).append('[').append(expressions == null ? "External " : "").append("Function (");
        for (int i = 0; i < arguments.size(); i++) {
            var argument = arguments.get(i);
            out.append('[').append(argument.getName()).append("][").append(argument.type).append(']');
            if (i + 1 < arguments.size()) {
                out.append(", ");
            }
        }
        out.append(") [").append(this.ret).append(']');
        if (expressions != null)
            for (var expression : expressions)
                out.append('\n').append(expression.print(offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }
}
