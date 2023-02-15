package ru.DmN.llml.precompiler;

import ru.DmN.llml.precompiler.action.*;
import ru.DmN.llml.utils.*;
import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.parser.ast.SyContext;
import ru.DmN.llml.parser.ast.SyFunction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PreCompiler {
    public final SyContext src;
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
                ctx.functions.add(new PcNIFunction(function.name, function.ret, function.arguments));
            }
        }
    }

    protected PcFunction precompile(SyFunction src) {
        var fun = new PcFunction(src.name, src.ret, src.arguments);
        var ivmap = new InternalVarMap();
        var vstack = new ArrayDeque<Value>();
        for (int i = 0; i < src.expressions.size(); i++) {
            var expr = src.expressions.get(i);
            for (int j = 0; j < expr.actions.size(); j++) {
                var act = expr.actions.get(j);
                if (act instanceof ActCall call) {
                    var function = ctx.functions.stream().filter(it -> it.name.equals(call.fun)).findFirst().orElseThrow(() -> new RuntimeException("Функция \"" + call.fun + "\" не определена!"));
                    var args = new ArrayList<Value>();
                    function.args.list.forEach(it -> args.add(cast(ivmap, fun.actions, vstack.pop(), it.type)));
                    Variable res;
                    if (fun.ret == Type.VOID) {
                        res = null;
                    } else {
                        res = ivmap.create(function.ret);
                        vstack.addLast(new Value(res));
                    }
                    fun.actions.add(new PACall(function, args, res));
                } else if (act instanceof ActInsertInteger insert) {
                    vstack.addLast(new Value(new Constant(insert.value)));
                } else if (act instanceof ActInsertVariable insert) {
                    vstack.addLast(new Value(insert.variable));
                } else if (act instanceof ActMath op) {
                    var a = cast(ivmap, fun.actions, vstack.pop(), op.type);
                    var b = cast(ivmap, fun.actions, vstack.pop(), op.type);
                    var out = ivmap.create(op.type);
                    var operation = new PAMath(a, b, out, op.oper);
                    vstack.addLast(new Value(out));
                    fun.actions.add(operation);
                } else if (act instanceof ActReturn) {
                    fun.actions.add(new PAReturn(cast(ivmap, fun.actions, vstack.pop(), fun.ret)));
                } else if (act instanceof ActSetVariable set) {
                    fun.actions.add(new PASet(cast(ivmap, fun.actions, vstack.pop(), set.variable.type), set.variable));
                }
            }
        }
        return fun;
    }

    protected Value cast(InternalVarMap ivmap, List<PrecompiledAction> actions, Value of, Type to) {
        if (of.constant == null) {
            if (of.variable.type == to)
                return of;
            var nw = ivmap.create(to);
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
