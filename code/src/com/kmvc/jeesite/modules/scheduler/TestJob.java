package com.kmvc.jeesite.modules.scheduler;

import org.slf4j.*;
import org.quartz.*;

public class TestJob extends BaseJob
{
    private final Logger logger;

    public TestJob() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @Override
    public void executeJob(final JobExecutionContext paramJobExecutionContext) throws Exception {
        this.logger.info("=======\u5f00\u59cb\u6267\u884c\u4efb\u52a1, TestJob->executeJob()");
        System.out.println("====================SUCCESS!======================");
    }
}
