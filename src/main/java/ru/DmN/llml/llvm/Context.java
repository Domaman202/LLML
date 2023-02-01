package ru.DmN.llml.llvm;

import java.util.ArrayList;
import java.util.List;

public class Context {
    public List<Function> functions = new ArrayList<>();
    public List<ExternalFunction> externalFunctions = new ArrayList<>();

    public Function defineFunction(String name, Type ret, List<Argument> arguments) {
        var func = new Function(name, ret, arguments);
        this.functions.add(func);
        return func;
    }

    public void defineExternalFunction(String name, Type ret, List<Argument> arguments) {
        this.externalFunctions.add(new ExternalFunction(name, ret, arguments));
    }

    public String compile() {
        var out = new StringBuilder();
        for (var func : externalFunctions)
            this._insertFunctionDefine(out.append("declare noundef "), func);
        out.append('\n');
        for (var func : functions) {
            func.compile(this, out);
            out.append("}\n");
        }
        return out.toString();
    }

    protected void _insertFunctionDefine(StringBuilder out, ExternalFunction func) {
        out.append(func.ret.name).append(" ").append(func.getName()).append("(");
        int i = 0;
        for (var arg : func.arguments.list) {
            out.append(arg.type.name).append(" noundef ").append(arg.getName());
            i++;
            if (i < func.arguments.size()) {
                out.append(", ");
            }
        }
        out.append(") local_unnamed_addr #").append((func instanceof Function ? this.functions : this.externalFunctions).indexOf(func)).append("\n");
    }
}
