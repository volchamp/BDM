package com.kmvc.jeesite.modules.scheduler;

import org.slf4j.*;
import java.util.*;
import org.quartz.*;

public abstract class BaseJob implements Job
{
    private final Logger log;

    public BaseJob() {
        this.log = LoggerFactory.getLogger((Class)this.getClass());
    }

    public abstract void executeJob(final JobExecutionContext p0) throws Exception;

    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final String jobName = context.getJobDetail().getKey().getName();
        String trigName = "directExec";
        final Trigger trig = context.getTrigger();
        if (trig != null) {
            trigName = trig.getKey().getName();
        }
        final Date strStartTime = new Date();
        final long startTime = System.currentTimeMillis();
        try {
            this.executeJob(context);
            final long endTime = System.currentTimeMillis();
            final Date strEndTime = new Date();
            final long runTime = (endTime - startTime) / 1000L;
            this.addLog(jobName, trigName, strStartTime, strEndTime, runTime, "\u4efb\u52a1\u6267\u884c\u6210\u529f!", 1);
        }
        catch (Exception ex) {
            final long endTime2 = System.currentTimeMillis();
            final Date strEndTime2 = new Date();
            final long runTime2 = (endTime2 - startTime) / 1000L;
            try {
                this.addLog(jobName, trigName, strStartTime, strEndTime2, runTime2, ex.toString(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
                this.log.error("\u6267\u884c\u4efb\u52a1\u51fa\u9519:" + e.getMessage());
            }
        }
    }

    private void addLog(final String jobName, final String trigName, final Date strStartTime, final Date strEndTime, final long runTime, final String content, final int state) throws Exception {
    }
}
