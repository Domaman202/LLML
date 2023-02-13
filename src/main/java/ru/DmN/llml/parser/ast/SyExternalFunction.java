package ru.DmN.llml.parser.ast;

import ru.DmN.llml.utils.Argument;
import ru.DmN.llml.utils.Type;

import java.util.List;

public class SyExternalFunction extends SyAbstractFunction {
    public SyExternalFunction(String name, Type ret, List<Argument> arguments) {
        super(name, ret, arguments);
    }

    @Override
    public StringBuilder toString(int offset) {
        return super.toString(offset).append(" [ext]");
    }
}
