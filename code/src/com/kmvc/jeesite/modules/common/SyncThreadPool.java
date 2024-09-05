package com.kmvc.jeesite.modules.common;

import org.apache.log4j.*;
import java.util.concurrent.*;

public class SyncThreadPool
{
    private static final int THREAD_SIZE = 20;
    private Logger logger;
    private ExecutorService service;
    private BlockingQueue<Future<Boolean>> blockQueue;
    private CompletionService<Boolean> completionService;
    private int taskCount;

    public SyncThreadPool() {
        this.logger = Logger.getLogger((Class)this.getClass());
        this.service = Executors.newFixedThreadPool(20);
        this.blockQueue = new LinkedBlockingDeque<Future<Boolean>>(20);
        this.completionService = new ExecutorCompletionService<Boolean>(this.service, this.blockQueue);
    }

    public void submit(final Callable<Boolean> task) {
        this.completionService.submit(task);
        ++this.taskCount;
    }

    public void finish() {
        try {
            for (int i = 0; i < this.taskCount; ++i) {
                this.completionService.take();
            }
        }
        catch (Exception e) {
            this.logger.error((Object)e, (Throwable)e);
        }
        finally {
            this.shutDown();
        }
    }

    private void shutDown() {
        this.service.shutdown();
    }
}
