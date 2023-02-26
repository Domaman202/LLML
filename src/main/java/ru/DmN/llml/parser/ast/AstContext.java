package ru.DmN.llml.parser.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.DmN.llml.parser.utils.IAstPrintable;
import ru.DmN.llml.parser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Контекст
 */
public class AstContext implements IAstPrintable {
    /**
     * Глобальные переменные
     */
    public final List<AstVariable> variables = new ArrayList<>();
    /**
     * Глобальные функции
     */
    public final List<AstFunction> functions = new ArrayList<>();

    /**
     * Ищет глобальную/локальную переменную
     *
     * @param function Функция (локальные переменные)
     * @param name     Имя переменной
     * @return Переменная
     */
    @SuppressWarnings("unchecked")
    public @Nullable AstAbstractVariable variable(@Nullable AstFunction function, @NotNull String name) {
        var var = Utils.findVariable((List<AstAbstractVariable>) (List<?>) this.variables, name);
        return var.orElseGet(() -> function == null ? null : function.variable(name));
    }

    @Override
    public String print(int offset) {
        var out = new StringBuilder("[Context");
        for (var variable : this.variables)
            out.append('\n').append(variable.print(offset + 1)).append('[').append(variable.type).append("]]");
        for (var function : this.functions)
            out.append('\n').append(function.print(offset + 1));
        return out.append("\n]").toString();
    }
}
