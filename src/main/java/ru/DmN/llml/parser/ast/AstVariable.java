package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Type;

public class AstVariable extends AstAbstractVariable {
    public final String name;
    public final boolean global, external;

    public AstVariable(String name, Type type, boolean global, boolean external) {
        super(type);
        this.name = name;
        this.global = global;
        this.external = external;
    }

    public AstVariable(String name, Type type, boolean external) {
        this(name, type, true, external);
    }

    public AstVariable(String name) {
        this(name, Type.UNKNOWN, false, false);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
