package ru.DmN.llml.parser.ast;

import ru.DmN.llml.llvm.Type;
import ru.DmN.llml.parser.Tracer;
import ru.DmN.llml.parser.action.ActInsertVariable;
import ru.DmN.llml.parser.action.ActMath;
import ru.DmN.llml.parser.action.ActReturn;
import ru.DmN.llml.parser.action.Action;

import java.util.ArrayList;
import java.util.List;

public class SyContext {
    public List<SyAbstractFunction> functions = new ArrayList<>();

    public <T extends SyAbstractFunction> T calculate(T fun, boolean calcA, Type calcB) {
        boolean needCalculation;
        do {
            needCalculation = false;
            if (calcB != Type.UNKNOWN) calculateB(fun,calcB);
            if (fun instanceof SyFunction f) {
                if (calcA) while (calculateA(f)) needCalculation = true;
            }
        } while (needCalculation);
        return fun;
    }

    public boolean calculateA(SyFunction fun) {
        for (var expr : fun.expressions) {
            for (int i = expr.actions.size(); i > 0; ) {
                var act = expr.actions.get(--i);
                if (act instanceof ActInsertVariable insert) {
                    if (insert.variable.type == Type.UNKNOWN) {
                        insert.variable.type = getTraceType(new Tracer.UpStepTracer<>(expr.actions, i));
                        return insert.variable.type != Type.UNKNOWN;
                    }
                } else if (act instanceof ActMath op) {
                    if (op.type == Type.UNKNOWN) {
                        op.type = fun.ret;
                        return op.type != Type.UNKNOWN;
                    }
                } else if (act instanceof ActReturn ret) {
                    if (ret.type == Type.UNKNOWN) {
                        fun.ret = ret.type = getTraceType(new Tracer.DownStepTracer<>(expr.actions, i));
                        return ret.type != Type.UNKNOWN;
                    }
                }
            }
        }
        return false;
    }

    public <T extends SyAbstractFunction> void calculateB(T fun, Type type) {
        //
        fun.arguments.values().forEach(arg -> {
            if (arg.type == Type.UNKNOWN) {
                arg.type = type;
            }
        });
        //
        if (fun instanceof SyFunction f) {
            for (var expr : f.expressions) {
                for (var act : expr.actions) {
                    if (act instanceof ActInsertVariable insert) {
                        if (insert.variable.type == Type.UNKNOWN) {
                            insert.variable.type = type;
                        }
                    } else if (act instanceof ActMath op) {
                        if (op.type == Type.UNKNOWN) {
                            op.type = type;
                        }
                    } else if (act instanceof ActReturn ret) {
                        if (ret.type == Type.UNKNOWN) {
                            fun.ret = ret.type = type;
                        }
                    }
                }
            }
        }
    }

    protected Type getTraceType(Tracer<Action> tracer) {
        while (tracer.hasNext()) {
            var act = tracer.next();
            if (act instanceof ActInsertVariable insert) {
                if (insert.variable.type != Type.UNKNOWN) {
                    return insert.variable.type;
                }
            } else if (act instanceof ActMath op) {
                if (op.type != Type.UNKNOWN) {
                    return op.type;
                }
            } else if (act instanceof ActReturn ret) {
                if (ret.type != Type.UNKNOWN) {
                    return ret.type;
                }
            }
        }
        return Type.UNKNOWN;
    }

    @Override
    public String toString() {
        var out = new StringBuilder("[Context");
        for (var function : functions)
            out.append("\n").append(function.toString(1));
        return out.append("\n]").toString();
    }
}
