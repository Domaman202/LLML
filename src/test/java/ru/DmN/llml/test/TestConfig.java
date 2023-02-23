package ru.DmN.llml.test;

public class TestConfig {
    public final String name;
    public final String src, out;
    public final String arguments;

    public TestConfig(String name, String src, String out, String arguments) {
        this.name = name;
        this.src = src;
        this.out = out;
        this.arguments = arguments;
    }
}
