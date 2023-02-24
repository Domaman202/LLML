package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstConstant extends AstExpression {
    public final Object value;

    public AstConstant(Object value) {
        if (value instanceof String str) {
            var tmp = str.toUpperCase();
            if (tmp.equals("TRUE")) {
                this.value = true;
            } else if (tmp.equals("FALSE")) {
                this.value = false;
            } else {
                // todo: regex num check
                this.value = str.contains(".") ? (Object) Double.parseDouble(str) : (Object) Integer.parseInt(str);
            }
        } else this.value = value;
    }

    public Type type() {
        if (this.value == null)
            return Type.VOID;
        if (this.value instanceof Boolean)
            return Type.I1;
        if (this.value instanceof Integer)
            return Type.I32;
        if (this.value instanceof Double)
            return Type.F32;
        return Type.UNKNOWN;
    }
}
