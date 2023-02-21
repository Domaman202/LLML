package ru.DmN.llml.utils;

import ru.DmN.llml.parser.ast.*;

public class PrintUtils {
    public static String print(AstActions ast, int offset) {
        var out = offset(offset).append("[Actions");
        for (var action : ast.actions)
            out.append('\n').append(print(action, offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    public static String print(AstAnnotation ast, int offset) {
        var out = offset(offset).append("[Annotation [").append(ast.name).append(']');
        for (var argument : ast.arguments)
            out.append('\n').append(print(argument, offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    public static String print(AstArgument ast, int offset) {
        return offset(offset).append("[Argument ([").append(ast.name).append("][").append(ast.type.name).append("])").toString();
    }

    public static String print(AstCall ast, int offset) {
        var out = offset(offset(offset).append("[Call\n"), offset + 1).append('[').append(ast.function.name).append(']');
        for (var argument : ast.arguments)
            out.append('\n').append(print(argument, offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    public static String print(AstCast ast, int offset) {
        return offset(offset(offset).append("[Cast [").append(ast.type.name).append("]\n").append(print(ast.value, offset + 1)).append('\n'), offset).append(']').toString();
    }

    public static String print(AstConstant ast, int offset) {
        return offset(offset).append("[Constant [").append(ast.value).append("]]").toString();
    }

    public static String print(AstContext ast, int offset) {
        var out = new StringBuilder("[Context");
        for (var function : ast.functions) {
            out.append('\n').append(print(function, offset + 1));
        }
        return out.append("\n]").toString();
    }

    public static String print(AstExpression expression, int offset) {
        if (expression instanceof AstActions ast)
            return print(ast, offset);
        if (expression instanceof AstAnnotation ast)
            return print(ast, offset);
        if (expression instanceof AstArgument ast)
            return print(ast, offset);
        if (expression instanceof AstCall ast)
            return print(ast, offset);
        if (expression instanceof AstCast ast)
            return print(ast, offset);
        if (expression instanceof AstConstant ast)
            return print(ast, offset);
        if (expression instanceof AstIf ast)
            return print(ast, offset);
        if (expression instanceof AstMath1Arg ast)
            return print(ast, offset);
        if (expression instanceof AstMath2Arg ast)
            return print(ast, offset);
        if (expression instanceof AstNamedActions ast)
            return print(ast, offset);
        if (expression instanceof AstNamedActionsReference ast)
            return print(ast, offset);
        if (expression instanceof AstReturn ast)
            return print(ast, offset);
        if (expression instanceof AstValue ast)
            return print(ast, offset);
        if (expression instanceof AstVariable ast)
            return print(ast, offset);
        if (expression instanceof AstVariableGet ast)
            return print(ast, offset);
        if (expression instanceof AstVariableSet ast)
            return print(ast, offset);
        throw new RuntimeException("TODO:");
    }

    public static String print(AstFunction ast, int offset) {
        var out = offset(offset).append("[Function (");
        for (int i = 0; i < ast.arguments.size(); i++) {
            var argument = ast.arguments.get(i);
            out.append('[').append(argument.name).append("][").append(argument.type).append(']');
            if (i + 1 < ast.arguments.size()) {
                out.append(", ");
            }
        }
        out.append(") [").append(ast.ret).append(']');
        for (var expression : ast.expressions)
            out.append('\n').append(print(expression, offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    public static String print(AstIf ast, int offset) {
        return offset(offset(offset(offset(offset).append("[If").append('\n').append(print(ast.value, offset + 1)).append('\n'), offset + 1).append('[').append(ast.a.name).append("]\n"), offset + 1).append('[').append(ast.b.name).append(']').append('\n'), offset).append(']').toString();
    }

    public static String print(AstMath1Arg ast, int offset) {
        return offset(offset(offset).append("[Math [").append(ast.operation).append("][").append(ast.rettype.name).append("]\n").append(print(ast.a, offset + 1)).append('\n').append('\n'), offset).append(']').toString();
    }

    public static String print(AstMath2Arg ast, int offset) {
        return offset(offset(offset).append("[Math [").append(ast.operation).append("][").append(ast.rettype.name).append("]\n").append(print(ast.a,offset + 1)).append('\n').append(print(ast.b,offset + 1)).append('\n'), offset).append(']').toString();
    }

    public static String print(AstNamedActions ast, int offset) {
        var out = offset(offset).append("[Named Actions [").append(ast.name).append(']');
        for (var action : ast.actions.actions)
            out.append('\n').append(print(action, offset + 1));
        return offset(out.append('\n'), offset).append(']').toString();
    }

    public static String print(AstNamedActionsReference ast, int offset) {
        return offset(offset).append("[Named Actions Ref [").append(ast.name).append(']').toString();
    }

    public static String print(AstReturn ast, int offset) {
        return offset(offset(offset).append("[Return\n").append(print(ast.value,offset + 1)).append('\n'), offset).append(']').toString();
    }

    public static String print(AstValue ast, int offset) {
        return ast.isConst() ? print(ast.constant, offset) : print(ast.variable, offset);
    }

    public static String print(AstVariable ast, int offset) {
        return offset(offset).append("[Variable [").append(ast.name).append(']').toString();
    }

    public static String print(AstVariableGet ast, int offset) {
        return offset(offset).append("[Get Variable [").append(ast.name).append("]]").toString();
    }

    public static String print(AstVariableSet ast, int offset) {
        return offset(offset(offset).append("[Set Variable [").append(ast.name).append("]\n").append(print(ast.value,offset + 1)).append('\n'), offset).append(']').toString();
    }

    public static StringBuilder offset(int offset) {
        return new StringBuilder().append("|\t".repeat(offset));
    }

    public static StringBuilder offset(StringBuilder builder, int offset) {
        return builder.append("|\t".repeat(offset));
    }
}
