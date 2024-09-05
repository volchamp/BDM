package com.kmvc.jeesite.modules.common;

import java.util.concurrent.*;

public class FixedThreadPool
{
    private static boolean inited;
    private static ExecutorService service;

    private static void init() {
        FixedThreadPool.service = Executors.newFixedThreadPool(40);
        FixedThreadPool.inited = true;
    }

    public static synchronized void exec(final Runnable runable) {
        if (!FixedThreadPool.inited) {
            init();
        }
        FixedThreadPool.service.execute(runable);
    }
}
