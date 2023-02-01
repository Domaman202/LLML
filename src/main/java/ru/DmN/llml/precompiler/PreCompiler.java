package ru.DmN.llml.precompiler;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.parser.ast.SyContext;
import ru.DmN.llml.parser.ast.SyFunction;

import java.util.ArrayDeque;

public class PreCompiler {
    protected final SyContext src;
    public final PcContext ctx;

    public PreCompiler(SyContext src) {
        this.src = src;
        this.ctx = new PcContext();
    }

    public void calcA() {
        for (var function : src.functions) {
            if (function instanceof SyFunction fun) {
                calcA(fun);
            } else {

            }
        }
    }

    protected void calcA(SyFunction src) {
        var fun = new PcFunction(src.name, src.ret, src.arguments);
        ctx.functions.add(fun);
        var ts = new ArrayDeque<Type>();
        for (var expression : src.expressions) {
            for (var action : expression.actions) {
                fun.actions.add(action);
                if (action instanceof ActInsertInteger) {
                    ts.push(Type.I32);
                } else if (action instanceof ActInsertVariable act) {
                    ts.push(act.variable.type);
                } else if (action instanceof ActMathOperation act) {
                    ts.pop();
                    var type = ts.pop();
                    if (act.isNeedCalc(src))
                        act.type = type;
                    ts.push(act.type);
                } else if (action instanceof ActReturn act) {
                    if (!ts.isEmpty()) {
                        var type = ts.pop();
                        if (act.isNeedCalc(src)) {
                            if (fun.ret == Type.UNKNOWN)
                                fun.ret = type;
                            act.type = fun.ret;
                        }
                    }
                } else if (action instanceof ActSetVariable act) {
                    var type = ts.pop();
                    if (act.isNeedCalc(src))
                        act.variable.type = type;
                    ts.push(act.variable.type);
                }
            }
        }
    }
}
