package ru.DmN.llml.utils;

import java.util.Iterator;
import java.util.List;

public abstract class Tracer<T> implements Iterator<T> {
    public final List<T> list;
    public int i;

    public Tracer(List<T> list, int i) {
        this.list = list;
        this.i = i;
    }

    public T get() {
        return list.get(i);
    }

    @Override
    public T next() {
        T obj = get();
        step();
        return obj;
    }

    public abstract void step();

    public static class IncStepTracer<T> extends Tracer<T> {
        public IncStepTracer(List<T> list, int i) {
            super(list, i);
        }

        @Override
        public boolean hasNext() {
            return i != list.size();
        }

        @Override
        public void step() {
            i++;
        }
    }

    public static class DecStepTracer<T> extends Tracer<T> {
        public DecStepTracer(List<T> list, int i) {
            super(list, i);
        }

        @Override
        public boolean hasNext() {
            return i != -1;
        }

        @Override
        public void step() {
            i--;
        }
    }
}