package ru.DmN.llml.parser;

import java.util.List;

public class Annotation {
    public String name;
    public List<String> args;

    public Annotation(String name, List<String> args) {
        this.name = name;
        this.args = args;
    }
}
