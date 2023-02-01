package ru.DmN.llml.llvm;

import ru.DmN.llml.llvm.expr.*;
import ru.DmN.llml.llvm.util.VariableMap;

import java.util.*;

public class Function extends ExternalFunction {
    public VariableMap<Variable> locals = new VariableMap<>();
    public List<Expression> expressions = new ArrayList<>();
    public int lti = 1;

    public Function(String name, Type ret, List<Argument> arguments) {
        super(name, ret, arguments);
        this.locals.addAll(arguments);
    }

    public Builder expression() {
        return new Builder();
    }

    public void compile(Context ctx, StringBuilder out) {
        var tmp = new StringBuilder();
        for (var expr : expressions) {
            expr.compile(this, tmp.append('\t'));
            tmp.append('\n');
        }
        ctx._insertFunctionDefine(out.append("define "), this);
        out.append("{\n").append(tmp);
    }

    public class Builder {
        public ArrayDeque<Value> vs = new ArrayDeque<>();

        protected Builder() {
        }

        protected Variable insertTmp(Type type) {
            Variable var = new Variable(String.valueOf(Function.this.lti++), type);
            Function.this.locals.add(var);
            return var;
        }

        public void insert(Variable variable) {
            this.vs.addLast(new Value(variable));
        }

        public boolean insert(String name) {
            var var = Function.this.locals.get(name);
            if (var == null)
                return false;
            Function.this.locals.add(var);
            insert(var);
            return true;
        }

        public void insert(int value) {
            this.vs.add(new Value(value));
        }

        public void cast(Type type) {
            var of = this.vs.peek();
            if (of.type().bits != type.bits) {
                this.vs.pop();
                var to = insertTmp(type);
                Function.this.expressions.add(new CastExpr(of.variable, to));
                insert(to);
            }
        }

        public void operation(Math2Expr.Type operation) {
            var a = this.vs.pop();
            var a$type = a.type();
            var b = this.vs.pop();
            var b$type = b.type();
            //
            if (a$type.bits != b$type.bits) {
                if (a$type.bits > b$type.bits) {
                    this.vs.push(b);
                    cast(a$type);
                    b = this.vs.pop();
                } else {
                    this.vs.push(a);
                    cast(a$type = b$type);
                    a = this.vs.pop();
                }
            }
            //
            Variable result = insertTmp(a$type);
            Function.this.expressions.add(new Math2Expr(a, b, result, a$type, operation));
            this.vs.push(new Value(result));
        }

        public void ret() {
            if (Function.this.ret != Type.VOID) {
                Value ret;
                if (Function.this.ret == Type.UNKNOWN) {
                    ret = this.vs.pop();
                    Function.this.ret = ret.type();
                } else {
                    cast(Function.this.ret);
                    ret = this.vs.pop();
                }
                Function.this.expressions.add(new ReturnExpr(ret));
            }
        }

        public void save(String to) {
            var value = this.vs.pop();
            if (value.variable == null) {
                if (!Function.this.locals.containsKey(to))
                    Function.this.locals.add(new Variable(to, value.type()));
                Function.this.expressions.add(new VariableDefineExpr("%" + to, value));
            } else {
                if (value.variable.name.matches("[0-9]*"))
                    Function.this.lti--;
                value.variable.name = to;
            }
        }
    }
}
