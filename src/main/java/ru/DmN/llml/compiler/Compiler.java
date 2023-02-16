package ru.DmN.llml.compiler;

import ru.DmN.llml.precompiler.PcContext;
import ru.DmN.llml.precompiler.PcFunction;
import ru.DmN.llml.precompiler.PcNIFunction;
import ru.DmN.llml.precompiler.action.*;
import ru.DmN.llml.utils.InitializedGlobalVariable;
import ru.DmN.llml.utils.Type;

public class Compiler {
    public final PcContext src;
    public final StringBuilder out;

    public Compiler(PcContext src) {
        this.src = src;
        this.out = new StringBuilder();
    }

    public void compile() {
        for (var variable : this.src.variables.list) {
            out.append(variable.getName()).append(" = dso_local global ").append(variable.type).append(' ');
            if (variable instanceof InitializedGlobalVariable var)
                out.append(var.constant);
            else out.append('0');
            out.append('\n');
        }
        out.append('\n');
        for (var function : this.src.functions) {
            if (function instanceof PcFunction fun) {
                writeFunction(true, fun);
                out.append(" {\n");
                for (var act : fun.actions) {
                    out.append('\t');
                    if (act instanceof PACall call) {
                        if (call.res != null)
                            out.append(call.res.getName()).append(" = ");
                        out.append("call ").append(call.fun.ret).append(' ').append(call.fun.getName()).append('(');
                        for (int i = 0; i < call.args.size(); i++) {
                            var arg = call.args.get(i);
                            out.append(arg.type()).append(' ').append(arg);
                            if (i + 1 < call.args.size()) {
                                out.append(", ");
                            }
                        }
                        out.append(')');
                    } else if (act instanceof PACast cast) {
                        var of = cast.of.type;
                        var of$int = of.fieldName().startsWith("I");
                        var to = cast.to.type;
                        var to$int = to.fieldName().startsWith("I");
                        out.append(cast.to.getName()).append(" = ").append(of$int?(to$int?(of.bits<to.bits?"sext":"trunc"):"sitofp"):(to$int?"fptosi":(of.bits<to.bits?"fpext":"fptrunc"))).append(' ').append(of).append(' ').append(cast.of.getName()).append(" to ").append(to);
                    } else if (act instanceof PAJmp jump) {
                        out.append("br ");
                        var condition = jump.condition;
                        if (condition != null)
                            out.append(condition.type()).append(' ').append(condition).append(", ");
                        out.append("label ").append(jump.labelA.label.getName());
                        var labelB = jump.labelB;
                        if (labelB != null) {
                            out.append(", label ").append(labelB.label.getName());
                        }
                    } else if (act instanceof PALoad load) {
                        var of = load.of;
                        out.append(load.to.getName()).append(" = load ").append(of.type).append(", ptr ").append(of.getName());
                    } else if (act instanceof PAMath math) {
                        out.append(math.out.getName()).append(" = ").append(math.oper.getIr(math.out.type.fieldName().startsWith("F"))).append(' ').append(math.getType()).append(' ').append(math.a).append(", ").append(math.b);
                    } else if (act instanceof PAReturn ret) {
                        out.append("ret ").append(fun.ret);
                        if (fun.ret != Type.VOID) {
                            out.append(' ').append(ret.value);
                        }
                    } else if (act instanceof PASet set) {
                        out.append(set.var.getName()).append(" = ").append(set.value);
                    } else if (act instanceof PAStore store) {
                        var to = store.to;
                        out.append("store ").append(to.type).append(' ').append(store.value).append(", ptr ").append(to.getName());
                    } else if (act instanceof PLabel label) {
                        out.replace(out.lastIndexOf("\t"), out.length(), label.label.name).append(':');
                    }
                    out.append('\n');
                }
                out.append("}\n\n");
            } else {
                writeFunction(false, function);
            }
        }
        out.append("attributes #0 = { nounwind }");
    }

    protected void writeFunction(boolean define, PcNIFunction fun) {
        out.append(define? "define" : "declare").append(" dso_local ").append(fun.ret).append(" @").append(fun.name).append('(');
        for (int i = 0; i < fun.args.size(); i++) {
            var arg = fun.args.list.get(i);
            out.append(arg.type).append(" noundef ").append(arg.getName());
            if (i + 1 < fun.args.size()) {
                out.append(", ");
            }
        }
        out.append(") #0");
    }
}
