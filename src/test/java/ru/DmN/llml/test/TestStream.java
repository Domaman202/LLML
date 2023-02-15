package ru.DmN.llml.test;

import java.io.*;

public class TestStream extends PrintStream {
    public final int tid,cid;

    public TestStream(int tid, int cid) throws FileNotFoundException {
        super(new FileOutputStream("log/test" + tid + ".log"));
        this.tid = tid;
        this.cid = cid;
    }
}
