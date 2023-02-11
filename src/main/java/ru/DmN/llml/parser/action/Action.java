package ru.DmN.llml.parser.action;

import ru.DmN.llml.parser.ast.SyFunction;

public abstract class Action {
    public boolean isNeedCalc(SyFunction fun) {
        return false;
    }

    public StringBuilder toString(int offset) {
        return new StringBuilder("|").append("\t".repeat(offset));
    }
}
