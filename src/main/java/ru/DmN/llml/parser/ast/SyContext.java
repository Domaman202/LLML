package ru.DmN.llml.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class SyContext extends SyElement {
    public List<SyAbstractFunction> functions = new ArrayList<>();

    @Override
    public String toString() {
        var out = new StringBuilder("[Context");
        for (var function : functions)
            out.append("\n").append(function.toString(1));
        return out.append("\n]").toString();
    }
}
