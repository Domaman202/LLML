package ru.DmN.llml.utils;

public class InitializedGlobalVariable extends GlobalVariable {
    public Constant constant;

    public InitializedGlobalVariable(String name, Constant constant) {
        super(name, constant.type);
        this.constant = constant;
    }
}
