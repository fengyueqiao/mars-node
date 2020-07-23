package io.github.fengyueqiao.marsnode.common.utils;

public class ProgressTimer {
    private long startTime = System.currentTimeMillis();
    public long elapse() {
        return System.currentTimeMillis() - startTime;
    }
}
