package ru.DmN.llml.precompiler;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.parser.utils.CalculationOptions;
import ru.DmN.llml.utils.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Прекомпилятор
 */
public class Precompiler {
    /**
     * Контекст
     */
    public final @NotNull AstContext context;

    /**
     * @param context Контескт
     */
    public Precompiler(@NotNull AstContext context) {
        this.context = context;
    }

    /**
     * Прекомпилирует контекст
     *
     * @return Контекст
     */
    public @NotNull AstContext precompile() {
        for (var function : context.functions) {
            if (function.expressions != null && !function.expressions.isEmpty()) {
                this.buildArguments(function);
                //
                var expressions = getAllExpressions(function);
                this.calc(function, expressions);
                this.calcType(function, expressions, new CalculationOptions(true));
                this.calcType(function, expressions, new CalculationOptions(false));
                //
                if (function.ret == Type.UNKNOWN) {
                    function.ret = Type.VOID;
                    function.expressions.add(new AstReturn(null));
                }
            }
        }
        return this.context;
    }

    protected void buildArguments(AstFunction function) {
        var j = 0;
        //
        var body = function.expressions;
        function.expressions = new ArrayList<>();
        //
        for (int i = 0; i < function.arguments.size(); i++) {
            var argument = function.arguments.get(i);
            var name = argument.name;
            var sc = function.variableSetMap.getOrDefault(name, 0);
            if (sc > 0) {
                function.variables.add(new AstVariable(name, argument.type, false, false, null));
                function.expressions.add(new AstVariableSet(name, new AstVariableGet(String.valueOf(i))));
                function.variableSetMap.put(name, sc + 1);
                //
                argument.i = i;
                argument.name = null;
                //
                j++;
            }
        }
        //
        function.expressions.addAll(body);
        //
        function.tmpVarCount = j;
    }

    protected void calc(AstFunction function, List<AstExpression> expressions) {
        expressions.forEach(it -> it.calc(context, function));
    }

    protected void calcType(AstFunction function, List<AstExpression> expressions, CalculationOptions options) {
        var cycle = true;
        while (cycle) {
            cycle = false;
            expressions = expressions.stream().filter(it -> it.needTypeCalc(context, function)).toList();
            for (AstExpression it : expressions) {
                if (it.calcType(context, function, options)) {
                    cycle = true;
                    break;
                }
            }
        }
    }

    protected static List<AstExpression> getAllExpressions(AstFunction function) {
        var list = new ArrayList<AstExpression>();
        assert function.expressions != null;
        function.expressions.forEach(it -> it.iterate(list::add, AstEmptyExpression.INSTANCE));
        return list;
    }

    /**
     * Get Result Math Type
     */
    public static Type grmt(Type a, Type b) {
        if (a == Type.UNKNOWN)
            return b;
        if (b == Type.UNKNOWN)
            return a;
        if (a.bits > 0) {
            var a$int = a.fieldName().startsWith("I");
            var b$int = b.fieldName().startsWith("I");
            var type = a.bits > b.bits ? a : b;
            return a$int ? (b$int ? type : b) : (b$int ? a : type);
        } else return b.bits > 0 ? b : a;
    }

    /**
     * Get Not "UNKNOWN" Type
     */
    public static Type gnut(Type a, Type b) {
        return a == Type.UNKNOWN ? b : a;
    }
}
