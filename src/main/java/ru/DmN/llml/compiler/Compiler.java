package ru.DmN.llml.compiler;

import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

public class Compiler {
    public final AstContext context;
    public StringBuilder out;

    public Compiler(AstContext context) {
        this.context = context;
        this.out = new StringBuilder();
    }

    public String compile() {
        for (var function : this.context.functions) {
            out.append("define ");
            if (function.ret != Type.VOID)
                out.append("noundef ");
            out.append(function.ret.name).append(" @").append(function.name).append('(');
            for (int i = 0; i < function.arguments.size();) {
                var argument = function.arguments.get(i);
                out.append(argument.type.name).append(" noundef %").append(argument.name);
                if (++i < function.arguments.size()) {
                    out.append(", ");
                }
            }
            out.append(") #0 {");
            this.write(function, new AstActions(function.expressions));
            out.append("\n}");
        }

        return this.out.append("\n\nattributes #0 = { nounwind }").toString();
    }

    protected AstValue write(AstFunction function, AstExpression expression) {
        if (expression instanceof AstActions actions) {
            actions.actions.forEach(it -> this.write(function, it));
        } else if (expression instanceof AstAnnotation annotation) {
            switch (annotation.name) {
                case "if" -> {
                    var var$a = this.write(function, annotation.arguments.get(0));
                    out.append("\n\tbr i1 ");
                    this.write(var$a).append(", label %").append(((AstNamedActionsReference) annotation.arguments.get(1)).name).append(", label %").append(((AstNamedActionsReference) annotation.arguments.get(2)).name);
                }
                default -> throw new RuntimeException("TODO:");
            }
        } else if (expression instanceof AstArgument argument) {
            return new AstValue(argument);
        } else if (expression instanceof AstCast cast) {
            var val = this.write(function, cast.value);
            var tmp = function.createTmpVariable();
            //
            var of = val.type();
            var of$int = of.fieldName().startsWith("I");
            var to = cast.type;
            var to$int = of.fieldName().startsWith("I");
            //
            this.write(tmp).append(" = ").append(of$int ? (to$int ? (of.bits > to.bits ? "trunc" : "sext") : "sitofp") : (to$int ? "fptosi" : (of.bits > to.bits ? "fptrunc" : "fpext"))).append(val.type().name).append(' ');
            this.write(val).append(" to ").append(to);
            return new AstValue(tmp);
        } else if (expression instanceof AstConstant constant) {
            return new AstValue(constant);
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
            var tmp = function.createTmpVariable();
            var result$int = math.rettype.fieldName().startsWith("I");
            //
            this.write(tmp).append(" = ");
            switch (math.operation) {
                case EQ, NOT_EQ, GREAT, GREAT_EQ, LESS, LESS_EQ  -> { // todo:
                    out.append(result$int ? 'i' : 'f').append("cmp ");
                }
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
            return new AstValue(function.variable(get.name));
        } else if (expression instanceof AstVariableSet set) {
            var val = this.write(function, set.value);
            var var = function.variable(set.name);
            out.append("\n\t");
            this.write(var).append(" = bitcast ").append(var.type.name).append(' ');
            this.write(val).append(" to ").append(var.type.name);
            return new AstValue(var);
        }

        return null;
    }

    protected StringBuilder write(AstValue value) {
        return value.isConst() ? out.append(value.constant.value) : this.write(value.variable);
    }

    protected StringBuilder write(AstVariable var) {
        return out.append('%').append(var.name);
    }
}
