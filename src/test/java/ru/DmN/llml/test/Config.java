package ru.DmN.llml.test;

public class Config {
    public final Optimization optimization;

    public Config(Optimization optimization) {
        this.optimization = optimization;
    }

    public static class Optimization {
        public final boolean enable;
        public final int level;

        public Optimization(boolean enable, int level) {
            this.enable = enable;
            this.level = level;
        }
    }
}
