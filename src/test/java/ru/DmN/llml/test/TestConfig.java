package ru.DmN.llml.test;

import ru.DmN.llml.utils.OptimizationConfig;

public class TestConfig {
    public final String name;
    public final String src, out;
    public final String[] test;
    public final OptimizationConfig optimization;

    public TestConfig(String name, String src, String out, String[] test, OptimizationConfig optimization) {
        this.name = name;
        this.src = src;
        this.out = out;
        this.test = test;
        this.optimization = optimization;
    }
}
