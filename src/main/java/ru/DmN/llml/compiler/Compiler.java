package ru.DmN.llml.compiler;

import ru.DmN.llml.precompiler.PcContext;
import ru.DmN.llml.precompiler.PcFunction;
import ru.DmN.llml.precompiler.PcNIFunction;
import ru.DmN.llml.precompiler.action.PACast;
import ru.DmN.llml.precompiler.action.PAMath;
import ru.DmN.llml.precompiler.action.PAReturn;
import ru.DmN.llml.precompiler.action.PASet;

public class Compiler {
    public final PcContext src;
    public final StringBuilder out;

    public Compiler(PcContext src) {
        this.src = src;
        this.out = new StringBuilder();
    }

    public void compile() {
        for (var function : this.src.functions) {
            if (function instanceof PcFunction fun) {
                writeFunction(true, fun);
                out.append(" {\n");
                for (var act : fun.actions) {
                    out.append('\t');
                    if (act instanceof PACast cast) {
                        out.append(cast.to.getName()).append(" = bitcast ").append(cast.of.type.name).append(' ').append(cast.of.getName()).append(" to ").append(cast.to.type.name);
                    } else if (act instanceof PAMath math) {
                        out.append(math.out.getName()).append(" = ").append(math.oper.ir).append(' ').append(math.getType().name).append(' ').append(math.a).append(", ").append(math.b);
                    } else if (act instanceof PAReturn ret) {
                        out.append("ret ").append(ret.value.type().name).append(' ').append(ret.value);
                    } else if (act instanceof PASet set) {
                        out.append(set.var.getName()).append(" = ").append(set.value);
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
        out.append(define? "define" : "declare").append(" dso_local ").append(fun.ret.name).append(" @").append(fun.name).append('(');
        for (int i = 0; i < fun.arguments.size(); i++) {
            var arg = fun.arguments.list.get(i);
            out.append(arg.type.name).append(" noundef ").append(arg.getName());
            if (i + 1 < fun.arguments.size()) {
                out.append(", ");
            }
        }
        out.append(") #0");
    }
}
