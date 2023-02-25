package ru.DmN.llml.compiler;

import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

public class Compiler {
    /**
     * Контекст
     */
    public final AstContext context;
    /**
     * Скомпилированный код
     */
    public StringBuilder out;

    /**
     *
     * @param context Контекст
     */
    public Compiler(AstContext context) {
        this.context = context;
        this.out = new StringBuilder();
    }

    /**
     * Компилирует контекст
     * @return Скомпилированный код
     */
    public String compile() {
        out.append("target triple = \"x86_64-pc-linux-gnu\"\n");

        for (var variable : this.context.variables) {
            out.append('\n').append('@').append(variable.name).append(" = ");
            if (variable.external)
                out.append("external ");
            out.append("global ").append(variable.type.name);
            if (!variable.external) {
                out.append(' ').append(variable.value.value);
            }
            out.append('\n');
        }

        for (var function : this.context.functions) {
            out.append(function.expressions == null ? "\ndeclare " : "\ndefine ");
            if (function.ret != Type.VOID)
                out.append("noundef ");
            out.append(function.ret.name).append(" @").append(function.name).append('(');
            //
            for (int i = 0; i < function.arguments.size();) {
                var argument = function.arguments.get(i);
                out.append(argument.type.name).append(" noundef %").append(argument.getName());
                if (++i < function.arguments.size()) {
                    out.append(", ");
                }
            }
            out.append(") #0 ");
            // пишем тело функции
            if (function.expressions != null) {
                out.append('{');
                this.write(function, new AstActions(function.expressions));
                out.append("\n}");
            }
            //
            out.append('\n');
        }

        return this.out.append("\nattributes #0 = { nounwind }").toString();
    }

    /**
     * Записывает выражение
     * @param function Функция
     * @param expression Выражение
     * @return Значение выражения
     */
    protected AstValue write(AstFunction function, AstExpression expression) {
        if (expression instanceof AstActions actions) {
            actions.actions.forEach(it -> this.write(function, it));
        } else if (expression instanceof AstAnnotation annotation) {
            throw new RuntimeException("TODO:");
        } else if (expression instanceof AstArgument argument) {
            return new AstValue(argument);
        } else if (expression instanceof AstCall call) {
            var arguments = call.arguments.stream().map(it -> this.write(function, it)).toList();
            var tmp = function.createTmpVariable(call.function.ret);
            out.append("\n\t");
            this.write(tmp).append(" = tail call noundef ").append(tmp.type).append(" @").append(call.function.name).append('(');
            for (int i = 0; i < arguments.size();) {
                var argument = arguments.get(i);
                out.append(argument.type()).append(" noundef ");
                this.write(argument);
                if (++i < arguments.size())
                    out.append(", ");
            }
            out.append(')');
            return new AstValue(tmp);
        } else if (expression instanceof AstCast cast) {
            var val = this.write(function, cast.value);
            //
            var of = val.type();
            var of$int = of.fieldName().startsWith("I");
            var to = cast.type;
            var to$int = of.fieldName().startsWith("I");
            //
            var tmp = function.createTmpVariable(to);
            out.append("\n\t");
            this.write(tmp).append(" = ").append(of$int ? (to$int ? (of.bits > to.bits ? "trunc" : "sext") : "sitofp") : (to$int ? "fptosi" : (of.bits > to.bits ? "fptrunc" : "fpext"))).append(' ').append(val.type().name).append(' ');
            this.write(val).append(" to ").append(to);
            return new AstValue(tmp);
        } else if (expression instanceof AstConstant constant) {
            return new AstValue(constant);
        } else if (expression instanceof AstIf if_) {
            var var$a = this.write(function, if_.value);
            out.append("\n\tbr i1 ");
            this.write(var$a).append(", label %").append(if_.a.name).append(", label %").append(if_.b.name);

        } else if (expression instanceof AstMath1Arg math) {
            out.append("\n\t");
            var val$a = this.write(function, math.a);
            switch (math.operation) {
                case NOT -> {
                    var tmp$0 = function.createTmpVariable();
                    write(tmp$0).append(" = icmp eq ").append(val$a.type().name).append(' ');
                    write(val$a);
                    var tmp$1 = function.createTmpVariable();
                    write(tmp$1).append(" = zext i1 ");
                    write(tmp$0).append(" to ").append(math.rettype.name);
                }
            }
        } else if (expression instanceof AstMath2Arg math) {
            out.append("\n\t");
            //
            var var$a = this.write(function, math.a);
            var var$b = this.write(function, math.b);
            var tmp = function.createTmpVariable(math.operation.logicOutput ? Type.I1 : math.rettype);
            var result$int = math.rettype.fieldName().startsWith("I");
            //
            this.write(tmp).append(" = ");
            switch (math.operation) {
                case EQ, NOT_EQ, GREAT, GREAT_EQ, LESS, LESS_EQ  -> out.append(result$int ? 'i' : 'f').append("cmp ");
            }
            out.append(result$int ? math.operation.iir : math.operation.fir).append(' ').append(math.rettype.name).append(' ');
            this.write(var$a);
            out.append(", ");
            this.write(var$b);
            return new AstValue(tmp);
        } else if (expression instanceof AstNamedActions actions) {
            out.append('\n').append(actions.name).append(':');
            this.write(function, actions.actions);
        } else if (expression instanceof AstReturn ret) {
            if (function.ret != Type.VOID) {
                var val = this.write(function, ret.value);
                out.append("\n\t").append("ret ").append(function.ret.name).append(' ');
                this.write(val);
            } else {
                out.append("\n\t").append("ret void");
            }
        } else if (expression instanceof AstVariableGet get) {
            return this.get(function, new AstValue(get.variable));
        } else if (expression instanceof AstVariableSet set) {
            var var = set.variable;
            this.set(function, this.write(function, set.value), var);
            return new AstValue(var);
        }

        return null;
    }

    /**
     * Если "значение" глобальная переменная - возвращает её значение.<br>
     * Если "значение" константа/локальная переменная - возвращает её.
     * @param function Функция
     * @param value "Значение"
     * @return Значение
     */
    protected AstValue get(AstFunction function, AstValue value) {
        if (value.isConst())
            return value;
        if (value.variable instanceof AstVariable var && var.global) {
            var tmp = function.createTmpVariable(var.type);
            out.append("\n\t");
            this.write(tmp).append(" = load ").append(var.type.name).append(", ptr @").append(var.name);
            return new AstValue(tmp);
        } else return value;
    }

    /**
     * Устанавливает значение переменной
     * @param function Функция
     * @param of Значение
     * @param to Переменная
     */
    protected void set(AstFunction function, AstValue of, AstAbstractVariable to) {
        var tname = to.type.name;
        out.append("\n\t");
        of = this.get(function, of);
        if (to instanceof AstVariable var && var.global) {
            out.append("store ").append(tname).append(' ');
            this.write(of).append(", ptr @").append(var.name);
        } else {
            this.write(to).append(" = bitcast ").append(tname).append(' ');
            this.write(of).append(" to ").append(tname);
        }
    }

    /**
     * Записывает значение (константу/переменную)
     * @param value Значение
     * @return out
     */
    protected StringBuilder write(AstValue value) {
        return value.isConst() ? out.append(value.constant.value) : this.write(value.variable);
    }

    /**
     * Записывает переменную (локальную/глобальную)
     * @param avar Переменная
     * @return out
     */
    protected StringBuilder write(AstAbstractVariable avar) {
        return out.append(avar instanceof AstVariable var ? var.global ? '@' : '%' : '%').append(avar.getName());
    }
}
