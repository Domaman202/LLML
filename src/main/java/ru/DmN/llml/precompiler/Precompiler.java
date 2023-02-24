package ru.DmN.llml.precompiler;

import ru.DmN.llml.parser.ast.*;
import ru.DmN.llml.utils.Type;

public class Precompiler {
    public final AstContext context;

    public Precompiler(AstContext context) {
        this.context = context;
    }

    public AstContext precompile() {
        for (var function : context.functions) {
            if (function.expressions != null && !function.expressions.isEmpty()) {
                this.precompileTypes(function);
                this.precompileCasts(function);

                if (function.ret == Type.UNKNOWN) {
                    function.ret = Type.VOID;
                    function.expressions.add(new AstReturn(null));
                }
            }
        }
        return this.context;
    }

    protected void precompileCasts(AstFunction function) {
        var cycle = true;
        while (cycle) {
            cycle = false;
            for (int i = 0; i < function.expressions.size(); i++) {
                while (cc(function, function.expressions.get(i))) cycle = true;
            }
        }
    }

    /**
     * Cast Calculation
     */
    protected boolean cc(AstFunction function, AstExpression expression) {
        if (expression instanceof AstReturn ret) {
            var type = dsgt(function, ret.value);
            if (type == function.ret) {
                return false;
            } else {
                ret.value = new AstCast(ret.value, function.ret);
                return true;
            }
        }
        return false;
    }

    protected void precompileTypes(AstFunction function) {
        var cycle = true;
        while (cycle) {
            cycle = false;
            for (int i = 0; i < function.expressions.size(); i++) {
                while (dstc(function, function.expressions.get(i)) != Type.UNKNOWN) cycle = true;
                while (ustc(function, function.expressions.get(i)) != Type.UNKNOWN) cycle = true;
            }
        }
    }

    /**
     * Universal Calculation
     */
    protected void uc(AstFunction function, AstExpression expression) {
        if (expression instanceof AstVariableGet get && get.variable == null)
            get.variable = context.variable(function, get.name);
    }

    /**
     * Up-Step Type Calculation
     */
    protected Type ustc(AstFunction function, AstExpression expression) {
        uc(function, expression);
        //
        if (expression instanceof AstActions actions) {
            Type type = Type.UNKNOWN;
            for (var action : actions.actions)
                type = gnut(type, ustc(function, action));
            return type;
        }

        if (expression instanceof AstReturn ret) {
            if (function.ret == Type.UNKNOWN)
                return function.ret = dsgt(function, ret.value);
            return gnut(ts(function, ret.value, function.ret), ustc(function, ret.value));
        }

        if (expression instanceof AstCall call)
            return ustc(function, new AstActions(call.arguments));
        if (expression instanceof AstIf if_)
            return ustc(function, if_.value);
        if (expression instanceof AstMath1Arg math)
            return gnut(this.ts(function, math.a, math.rettype), ustc(function, math.a));
        if (expression instanceof AstMath2Arg math)
            return gnut(gnut(this.ts(function, math.a, math.rettype), ts(function, math.b, math.rettype)), gnut(ustc(function, math.a), ustc(function, math.b)));
        if (expression instanceof AstNamedActions actions)
            return ustc(function, actions.actions);
        if (expression instanceof AstVariableSet set)
            return ts(function, set.value, (set.variable = context.variable(function, set.name)).type);
        return Type.UNKNOWN;
    }

    /**
     * Down-Step Type Calculation
     */
    protected Type dstc(AstFunction function, AstExpression expression) {
        uc(function, expression);
        //
        if (expression instanceof AstActions actions) {
            Type type = Type.UNKNOWN;
            for (var action : actions.actions)
                type = gnut(type, dstc(function, action));
            return type;
        }

        if (expression instanceof AstCall call)
            return dstc(function, new AstActions(call.arguments));
        if (expression instanceof AstIf if_)
            return dstc(function, if_.value);
        if (expression instanceof AstMath1Arg math)
            return ts(function, math, dsgt(function, math.a));
        if (expression instanceof AstMath2Arg math)
            return ts(function, math, grmt(dsgt(function, math.a), dsgt(function, math.b)));
        if (expression instanceof AstNamedActions actions)
            return dstc(function, actions.actions);

        if (expression instanceof AstReturn ret) {
            var type = dstc(function, ret.value);
            if (function.ret == Type.UNKNOWN)
                function.ret = type;
            return type;
        }

        if (expression instanceof AstVariableSet set) {
            var vtype = (set.variable = context.variable(function, set.name)).type;
            if (vtype == Type.UNKNOWN) {
                return this.vts(function, set.name, dsgt(function, set.value));
            } else {
                uc(function, set.value);
                return ts(function, set.value, vtype);
            }
        }

        return Type.UNKNOWN;
    }

    /**
     * Down-Step Get Type
     */
    protected Type dsgt(AstFunction function, AstExpression expression) {
        uc(function, expression);
        //
        var type = dsgt0(function, expression);
        if (type == Type.UNKNOWN)
            return dstc(function, expression);
        return type;
    }

    /**
     * Down-Step Get Type
     */
    protected Type dsgt0(AstFunction function, AstExpression expression) {
        if (expression instanceof AstCall call)
            return call.function.ret;
        if (expression instanceof AstCast cast)
            return cast.type;
        if (expression instanceof AstConstant constant)
            return constant.type();
        if (expression instanceof AstMath1Arg math)
            return math.rettype;
        if (expression instanceof AstMath2Arg math)
            return math.operation.logicOutput ? Type.I1 : math.rettype;
        if (expression instanceof AstReturn ret)
            return dsgt(function, ret.value);
        if (expression instanceof AstVariableGet get)
            return context.variable(function, get.name).type;
        if (expression instanceof AstVariableSet set)
            return context.variable(function, set.name).type;
        return Type.UNKNOWN;
    }

    /**
     * Type Set
     */
    protected Type ts(AstFunction function, AstExpression expression, Type type) {
        if (expression instanceof AstMath1Arg math && math.rettype == Type.UNKNOWN)
            return math.rettype = type;

        if (expression instanceof AstMath2Arg math && math.rettype == Type.UNKNOWN) {
            math.rettype = type;
            return math.operation.logicOutput ? Type.I1 : math.rettype;
        }

        if (expression instanceof AstVariableGet get)
            return vts(function, get.name, type);
        if (expression instanceof AstVariableSet set)
            return vts(function, set.name, type);
        return Type.UNKNOWN;
    }

    /**
     * Variable Type Set
     */
    protected Type vts(AstFunction function, String name, Type type) {
        var var = context.variable(function, name);
        if (var.type == Type.UNKNOWN)
            return var.type = type;
        return Type.UNKNOWN;
    }

    /**
     * Get Result Math Type
     */
    protected static Type grmt(Type a, Type b) {
        if (a.bits > 0) {
            var a$int = a.fieldName().startsWith("I");
            var b$int = b.fieldName().startsWith("I");
            var type = a.bits > b.bits ? a : b;
            return a$int ? (b$int ? type : b) : (b$int ? a : type);
        } else {
            return b.bits > 0 ? b : a;
        }
    }

    /**
     * Get Not "UNKNOWN" Type
     */
    protected static Type gnut(Type a, Type b) {
        return a == Type.UNKNOWN ? b : a;
    }
}
