package ru.DmN.llml.utils;

import net.sourceforge.argparse4j.inf.Namespace;

import java.util.List;
import java.util.Map;

public class OptimizationConfig {
    /**
     * Argument Name Optimization
     */
    public boolean ano;
    /**
     * Variable Assign Optimization
     */
    public boolean vao;

    /**
     *
     * @param ano Argument Name Optimization
     * @param vao Variable Assign Optimization
     */
    public OptimizationConfig(boolean ano, boolean vao) {
        this.ano = ano;
    }

    public OptimizationConfig() {
    }

    public static OptimizationConfig of(Namespace space) {
        var config = new OptimizationConfig();
        var optimizations = (List<String>) space.get("opt");
        if (optimizations != null) {
            for (var optimization : optimizations) {
                try {
                    OptimizationConfig.class.getField(optimization).set(config, true);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Ошибка! Неизвестная оптимизация \"" + optimization + "\"");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return config;
    }
}