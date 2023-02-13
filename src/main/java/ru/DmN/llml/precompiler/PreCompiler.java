package ru.DmN.llml.precompiler;

import ru.DmN.llml.precompiler.action.*;
import ru.DmN.llml.utils.*;
import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.parser.ast.SyContext;
import ru.DmN.llml.parser.ast.SyFunction;

import java.util.List;
import java.util.Stack;

public class PreCompiler {
    protected final SyContext src;
    public final PcContext ctx;

    public PreCompiler(SyContext src) {
        this.src = src;
        this.ctx = new PcContext();
    }

    public void precompile() {
        for (var function : src.functions) {
            if (function instanceof SyFunction fun) {
                ctx.functions.add(precompile(src.calculate(fun, true, Type.UNKNOWN)));
            } else {
                // TODO:
            }
        }
    }

    protected PcFunction precompile(SyFunction src) {
        var fun = new PcFunction(src.name, src.ret, src.arguments);
        var ivmap = new InternalVarMap();
        var vstack = new Stack<Value>();
        for (int i = 0; i < src.expressions.size(); i++) {
            var expr = src.expressions.get(i);
            for (int j = 0; j < expr.actions.size(); j++) {
                var act = expr.actions.get(j);
                if (act instanceof ActInsertInteger insert) {
                    vstack.push(new Value(new Constant(insert.value)));
                } else if (act instanceof ActInsertVariable insert) {
                    vstack.push(new Value(insert.variable));
                } else if (act instanceof ActMath op) {
                    var a = cast(ivmap, fun.actions, vstack.pop(), op.type);
                    var b = cast(ivmap, fun.actions, vstack.pop(), op.type);
                    var out = ivmap.create(op.type);
                    var operation = new PAMath(a, b, out, op.operation);
                    vstack.push(new Value(out));
                    fun.actions.add(operation);
                } else if (act instanceof ActSetVariable set) {
                    fun.actions.add(new PASet(cast(ivmap, fun.actions, vstack.pop(), set.variable.type), set.variable));
                }
            }
        }
        if (fun.ret != Type.UNKNOWN && fun.ret != Type.VOID)
            fun.actions.add(new PAReturn(vstack.pop()));
        return fun;
    }

    protected Value cast(InternalVarMap vmap, List<PrecompiledAction> actions, Value of, Type to) {
        if (of.constant == null) {
            if (of.variable.type == to)
                return of;
            var nw = vmap.create(to);
            actions.add(new PACast(of.variable, nw));
            return new Value(nw);
        } else if (of.constant.type != to)
            of.constant.type = to;
        return of;
    }
}

/*
var fun = new PcFunction(src.name, src.ret, src.arguments);
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
 */
