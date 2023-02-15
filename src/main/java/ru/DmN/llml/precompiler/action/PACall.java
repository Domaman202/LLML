package ru.DmN.llml.precompiler.action;

import ru.DmN.llml.precompiler.PcNIFunction;
import ru.DmN.llml.utils.StringBuilderUtils;
import ru.DmN.llml.utils.Value;
import ru.DmN.llml.utils.Variable;

import java.util.List;

public class PACall extends PrecompiledAction {
    public PcNIFunction fun;
    public List<Value> args;
    public Variable res;

    public PACall(PcNIFunction fun, List<Value> args, Variable res) {
        this.fun = fun;
        this.args = args;
        this.res = res;
    }

    @Override
    public StringBuilder toString(int offset) {
        return StringBuilderUtils.append(StringBuilderUtils.append(super.toString(offset).append("(Call): [").append(fun.name).append("][").append(fun.ret).append("] "), args).append(' '), res);
    }
}
