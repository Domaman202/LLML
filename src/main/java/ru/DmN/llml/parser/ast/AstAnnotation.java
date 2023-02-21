package ru.DmN.llml.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class AstAnnotation extends AstExpression {
    public final String name;
    public final List<AstExpression> arguments;

    public AstAnnotation(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }
}
