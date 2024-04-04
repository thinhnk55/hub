package com.defi.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPoolWorker {
    public ExecutorService executor;
    public ScheduledThreadPoolExecutor scheduler;
    private static ThreadPoolWorker ins = null;

    private ThreadPoolWorker() {
        int procs = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(2 * procs);
        scheduler = new ScheduledThreadPoolExecutor(2 * procs);
    }

    public static ThreadPoolWorker instance() {
        if (ins == null) {
            ins = new ThreadPoolWorker();
        }
        return ins;
    }
}
