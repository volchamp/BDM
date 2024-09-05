package com.kmvc.jeesite.modules.scheduler;

import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetDataService;
import org.quartz.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.thinkgem.jeesite.common.utils.*;

public class CheckValidationJob extends BaseJob
{
    @Override
    public void executeJob(final JobExecutionContext paramJobExecutionContext) throws Exception {
        final CodeSetDataService codeSetDataService = (CodeSetDataService)SpringContextHolder.getBean((Class)CodeSetDataService.class);
        codeSetDataService.validtimeCheck();
    }
}
