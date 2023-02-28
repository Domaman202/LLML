package ru.DmN.llml.compiler;

import org.jetbrains.annotations.NotNull;
import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.NotRealizedException;
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

        for (var variable : context.variables) {
            out.append('\n').append('@').append(variable.name).append(" = ");
            if (variable.external)
                out.append("external ");
            out.append("global ").append(variable.type.name);
            if (!variable.external) {
                out.append(' ').append(variable.value.value);
            }
            out.append('\n');
        }

        for (var function : context.functions) {
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
            throw new NotRealizedException();
        } else if (expression instanceof AstArgument argument) {
            return new AstValue(argument);
        } else if (expression instanceof AstCall call) {
            var arguments = call.arguments.stream().map(it -> this.write(function, it)).toList();
            var tmp = function.createTmpVariable(call.function.ret);
            out.append("\n\t");
            this.write(tmp).append(" = tail call noundef ").append(tmp.type).append(" @").append(call.function.name).append('(');
            for (int i = 0; i < arguments.size();) {
                var argument = arguments.get(i);
                out.append(argument.getType(context, function)).append(" noundef ");
                this.write(argument);
                if (++i < arguments.size())
                    out.append(", ");
            }
            out.append(')');
            return new AstValue(tmp);
        } else if (expression instanceof AstCast cast) {
            var val = this.write(function, cast.value);
            //
            var of = val.getType(context, function);
            var of$int = of.fieldName().startsWith("I");
            var to = cast.type;
            var to$int = of.fieldName().startsWith("I");
            //
            var tmp = function.createTmpVariable(to);
            out.append("\n\t");
            this.write(tmp).append(" = ").append(of$int ? (to$int ? (of.bits > to.bits ? "trunc" : "sext") : "sitofp") : (to$int ? "fptosi" : (of.bits > to.bits ? "fptrunc" : "fpext"))).append(' ').append(val.getType(this.context, function).name).append(' ');
            this.write(val).append(" to ").append(to);
            return new AstValue(tmp);
        } else if (expression instanceof AstConstant constant) { // todo:
            var type = constant.getType(context, function);
            if (type.fieldName().startsWith("I")) {
                return new AstValue(constant);
            } else {
                assert constant.value != null;
                var value = String.valueOf(constant.value).split("\\.");
                if (value.length == 1) {
                    constant.value = (double) (int) constant.value;
                    return new AstValue(constant);
                } else {
                    var tmp$a = function.createTmpVariable(type);
                    out.append("\n\t%").append(tmp$a.i).append(" = fdiv ").append(type.name).append(' ').append(value[1]).append(".0, ").append(Math.pow(10, value[1].length()));
                    var tmp$b = function.createTmpVariable(type);
                    out.append("\n\t%").append(tmp$b.i).append(" = fadd ").append(type.name).append(' ').append(value[0]).append(".0, %").append(tmp$a.i);
                    return new AstValue(tmp$b);
                }
            }
        } else if (expression instanceof AstIf if_) {
            var var$a = this.write(function, if_.value);
            out$nl("br i1 ");
            this.write(var$a).append(", label %").append(if_.a.name);
            if (if_.b != null) {
                out.append(", label %").append(if_.b.name);
            }
        } else if (expression instanceof AstJump jump) {
            out$nl("br label %").append(jump.label.name);
        } else if (expression instanceof AstLabel label) {
            this.write(function, new AstJump(new AstLabelReference(label.name)));
            out.append('\n').append(label.name).append(':');
            if (label.itc) {
                function.tmpVarCount++;
            }
        } else if (expression instanceof AstMath1Arg math) {
            out.append("\n\t");
            var val$a = this.write(function, math.a);
            switch (math.operation) {
                case NOT -> {
                    var tmp$0 = function.createTmpVariable();
                    this.write(tmp$0).append(" = xor ").append(val$a.getType(context, function).name).append(' ');
                    this.write(val$a).append(", true");
                    return new AstValue(tmp$0);
                }
                default -> throw new NotRealizedException();
            }
        } else if (expression instanceof AstMath2Arg math) {
            var var$a = this.write(function, math.a);
            var var$b = this.write(function, math.b);
            var tmp = function.createTmpVariable(math.operation.logicOutput ? Type.I1 : math.rettype);
            var result$int = math.rettype.fieldName().startsWith("I");
            //
            this.out.append("\n\t");
            this.write(tmp).append(" = ");
            switch (math.operation) {
                case EQ, NOT_EQ, GREAT, GREAT_EQ, LESS, LESS_EQ  -> out.append(result$int ? 'i' : 'f').append("cmp ");
                default -> throw new NotRealizedException();
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
                out$nl("ret ").append(function.ret.name).append(' ');
                this.write(val);
            } else {
                out$nl("ret void");
            }
        } else if (expression instanceof AstVariableGet get) {
            return this.get(function, new AstValue(get.variable));
        } else if (expression instanceof AstVariableSet set) {
            var var = set.variable;
            if (var instanceof AstVariable v && !v.allocated && function.variableSetMap.getOrDefault(var.getName(), 0) > 1) {
                out$nl("%").append(v.getName()).append(" = alloca ").append(var.type.name); // todo: align
                v.allocated = true;
            }
            this.set(function, this.write(function, set.value), var);
            return new AstValue(var);
        } else if (expression instanceof AstWhile while_) {
            var i = while_.id;
            var name$check = ".w" + i + ".c";
            var name$loop = ".w" + i + ".l";
            var name$exit = ".w" + i + ".e";
            this.write(function, new AstLabel(name$check, false));
            this.write(function, new AstIf(while_.value, new AstLabelReference(name$loop), new AstLabelReference(name$exit)));
            this.write(function, new AstLabel(name$loop, true));
            this.write(function, while_.actions);
            this.write(function, new AstJump(new AstLabelReference(name$check)));
            this.write(function, new AstLabel(name$exit, true));
        } else throw new NotRealizedException();

        return null;
    }

    /**
     * Если "значение" глобальная переменная - возвращает её значение.<br>
     * Если "значение" константа/локальная переменная - возвращает её.
     * @param function Функция
     * @param value "Значение"
     * @return Значение
     */
    protected AstValue get(@NotNull AstFunction function, @NotNull AstValue value) {
        if (value.isConst())
            return value;
        if (value.variable instanceof AstVariable var && var.allocated) {
            var tmp = function.createTmpVariable(var.type);
            out.append("\n\t");
            this.write(tmp).append(" = load ").append(var.type.name).append(", ptr ").append(var.global ? '@' : '%').append(var.name);
            return new AstValue(tmp);
        } else return value;
    }

    /**
     * Устанавливает значение переменной
     * @param function Функция
     * @param of Значение
     * @param to Переменная
     */
    protected void set(@NotNull AstFunction function, @NotNull AstValue of, @NotNull AstAbstractVariable to) {
        var tname = to.type.name;
        out.append("\n\t");
        of = this.get(function, of);
        if (to instanceof AstVariable var && var.allocated) {
            out.append("store ").append(tname).append(' ');
            this.write(of).append(", ptr ").append(var.global ? '@' : '%').append(var.name);
        } else {
            this.write(to).append(" = bitcast ").append(tname).append(' ');
            this.write(of).append(" to ").append(tname);
        }
    }

    protected StringBuilder out$nl(String str) {
        return out.append("\n\t").append(str);
    }

    /**
     * Записывает значение (константу/переменную)
     * @param value Значение
     * @return out
     */
    protected StringBuilder write(@NotNull AstValue value) {
        return value.isConst() ? out.append(value.constant.value) : this.write(value.variable);
    }

    /**
     * Записывает переменную (локальную/глобальную)
     * @param avar Переменная
     * @return out
     */
    protected StringBuilder write(@NotNull AstAbstractVariable avar) {
        return out.append(avar instanceof AstVariable var && var.global ? '@' : '%').append(avar.getName());
    }
}
