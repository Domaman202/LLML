package ru.DmN.llml.parser.ast;

import ru.DmN.llml.parser.action.*;
import ru.DmN.llml.utils.*;

import java.util.ArrayList;
import java.util.List;

public class SyContext {
    public List<SyAbstractFunction> functions = new ArrayList<>();
    public VariableMap<Variable> variables = new VariableMap<>();

    public <T extends SyAbstractFunction> T calculate(T function, boolean calcA, Type calcB) {
        boolean needCalculation;
        do {
            needCalculation = false;
            // Замена неопределённых типов на `calcB`
            if (calcB != Type.UNKNOWN) calculateB(function,calcB);
            if (function instanceof SyFunction fun) {
                // Удаление пустых выражений
                for (int i = 0; i < fun.expressions.size(); i++) {
                    var expr = fun.expressions.get(i);
                    if (expr.actions.isEmpty()) {
                        fun.expressions.remove(expr);
                        i--;
                    }
                }
                // Просчёт типов
                if (calcA) {
                    while (calculateA(fun)) {
                        needCalculation = true;
                    }
                }
            }
        } while (needCalculation);
        return function;
    }

    public boolean calculateA(SyFunction fun) {
        for (var expr : fun.expressions) {
            for (int i = expr.actions.size(); i > 0; ) {
                var act = expr.actions.get(--i);
                if (act instanceof ActInsertVariable insert) {
                    if (insert.variable.type == Type.UNKNOWN && (insert.variable.type = getTraceType(fun, new Tracer.IncStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
                        return true;
                    }
                } else if (act instanceof ActMath op) {
                    if (op.isNeedCalc(fun)) {
                        if (op.inputType == Type.UNKNOWN)
                            return (op.inputType = getTraceType(fun, new Tracer.IncStepTracer<>(expr.actions, i))) != Type.UNKNOWN;
                        if (op.outputType == Type.UNKNOWN)
                            return (op.outputType = getTraceType(fun, new Tracer.DecStepTracer<>(expr.actions, i))) != Type.UNKNOWN;
                        return true;
                    }
                } else if (act instanceof ActReturn ret) {
                    if (ret.type == Type.UNKNOWN) {
                        if ((fun.ret = ret.type = getTraceType(fun, new Tracer.DecStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
                            return true;
                        }
                    } else if (ret.type != fun.ret) {
                        ret.type = fun.ret;
                    }
                } else if (act instanceof ActSetVariable set) {
                    if (set.variable.type == Type.UNKNOWN && (set.variable.type = getTraceType(fun, new Tracer.DecStepTracer<>(expr.actions, i))) != Type.UNKNOWN) {
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
                        if (op.inputType == Type.UNKNOWN)
                            op.inputType = type;
                        if (op.outputType == Type.UNKNOWN)
                            op.outputType = type;
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

    protected Type getTraceType(SyFunction function, Tracer<Action> tracer) {
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
                if (tracer instanceof Tracer.DecStepTracer) {
                    if (math.outputType != Type.UNKNOWN) {
                        return math.outputType;
                    } else if (math.inputType != Type.UNKNOWN) {
                        return math.inputType;
                    }
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
        return tracer instanceof Tracer.IncStepTracer ? function.ret : Type.UNKNOWN;
    }

    @Override
    public String toString() {
        var out = new StringBuilder("[Context");
        for (var variable : variables.list) {
            out.append('\n').append("|\t[var ").append(variable.name).append(": ").append(variable.type);
            if (variable instanceof InitializedGlobalVariable var) out.append(" = ").append(var.constant);
            out.append(']');
        }
        out.append("\n|");
        for (var function : functions)
            out.append('\n').append(function.toString(1));
        return out.append("\n]").toString();
    }
}
