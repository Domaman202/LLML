package ru.DmN.llml.parser.ast;

import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.utils.Type;
import ru.DmN.llml.utils.Tracer;

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
                    if (insert.variable.type == Type.UNKNOWN && (insert.variable.type = getTraceType(new Tracer.IncStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
                        return true;
                    }
                } else if (act instanceof ActMath op) {
                    if (op.type == Type.UNKNOWN && (op.type = fun.ret) != Type.UNKNOWN) {
                        return true;
                    }
                } else if (act instanceof ActReturn ret) {
                    if (ret.type == Type.UNKNOWN) {
                        if ((fun.ret = ret.type = getTraceType(new Tracer.DecStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
                            return true;
                        }
                    } else if (ret.type != fun.ret) {
                        ret.type = fun.ret;
                    }
                } else if (act instanceof ActSetVariable set) {
                    if (set.variable.type == Type.UNKNOWN && (set.variable.type = getTraceType(new Tracer.DecStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
                        return true;
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
                    } else if (act instanceof ActSetVariable set) {
                        if (set.variable.type == Type.UNKNOWN) {
                            set.variable.type = type;
                        }
                    }
                }
            }
        }
    }

    protected Type getTraceType(Tracer<Action> tracer) {
        while (tracer.hasNext()) {
            var act = tracer.next();
            if (act instanceof ActCall call) {
                var fun = this.functions.stream().filter(it -> it.name.equals(call.fun)).findFirst().orElseThrow(() -> new RuntimeException("Функция \"" + call.fun + "\" не определена!"));
                if (fun.ret != Type.UNKNOWN) {
                    return fun.ret;
                }
            } else if (act instanceof ActInsertInteger) {
                return Type.I32;
            } else if (act instanceof ActInsertVariable insert) {
                if (insert.variable.type != Type.UNKNOWN) {
                    return insert.variable.type;
                }
            } else if (act instanceof ActMath math) {
                if (math.type != Type.UNKNOWN) {
                    return math.type;
                }
            } else if (act instanceof ActReturn ret) {
                if (ret.type != Type.UNKNOWN) {
                    return ret.type;
                }
            } else if (act instanceof ActSetVariable set) {
                if (set.variable.type != Type.UNKNOWN) {
                    return set.variable.type;
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
