package ru.DmN.llml.precompiler.opt;

import ru.DmN.llml.parser.ast.AstVariableGet;
import ru.DmN.llml.parser.ast.AstVariableSet;

import java.util.HashSet;
import java.util.Set;

public class VariableData {
    public final Set<AstVariableSet> sets = new HashSet<>();
    public final Set<AstVariableGet> gets = new HashSet<>();
}
