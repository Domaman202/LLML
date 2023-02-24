package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstVariable extends AstAbstractVariable {
    public final String name;
    public final boolean global, external;
    public final AstConstant value;

    public AstVariable(String name, Type type, boolean global, boolean external, AstConstant value) {
        super(type);
        this.name = name;
        this.global = global;
        this.external = external;
        this.value = value;
    }

    public AstVariable(String name, Type type, boolean external, AstConstant value) {
        this(name, type, true, external, value);
    }

    public AstVariable(String name) {
        this(name, Type.UNKNOWN, false, false, null);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
