package com.kmvc.jeesite.modules.scheduler;

import com.kmvc.jeesite.modules.bps.service.WorkFlowService;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.businessdatamrg.service.SendingCodeSetService;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetDataService;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodeItemService;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodeSetService;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.kmvc.jeesite.modules.tesk.service.TaskService;
import org.slf4j.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.kmvc.jeesite.modules.tesk.service.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.businessdatamrg.service.*;
import com.kmvc.jeesite.modules.bps.service.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import java.util.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.quartz.*;

public class ReviewTaskTimeoutProcJob extends BaseJob
{
    private final Logger logger;

    public ReviewTaskTimeoutProcJob() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @Override
    public void executeJob(final JobExecutionContext context) throws JobExecutionException {
        final long currentTimeMillis = System.currentTimeMillis();
        PendingCodeSet pendingCodeSet = null;
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean((Class)LogInoutService.class);
        final UserDao userDao = (UserDao)SpringContextHolder.getBean((Class)UserDao.class);
        final User user = (User)userDao.get("1");
        try {
            final String reviewTaskTimeoutMinute = DictUtils.getDictValue("reviewTaskTimeoutMinute", "timeoutMinute", "240");
            double taskTimeoutMinute = 240.0;
            try {
                taskTimeoutMinute = Double.parseDouble(reviewTaskTimeoutMinute);
            }
            catch (NumberFormatException nfe) {
                taskTimeoutMinute = 240.0;
            }
            final TSystemService tSystemService = (TSystemService)SpringContextHolder.getBean((Class)TSystemService.class);
            final TaskService taskService = (TaskService)SpringContextHolder.getBean((Class)TaskService.class);
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean((Class)PendingCodeSetService.class);
            final PendingCodeItemService pendingCodeItemService = (PendingCodeItemService)SpringContextHolder.getBean((Class)PendingCodeItemService.class);
            final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean((Class)CodeSetService.class);
            final CodeSetDataService codeSetDataService = (CodeSetDataService)SpringContextHolder.getBean((Class)CodeSetDataService.class);
            final SendingCodeSetService sendingCodeSetService = (SendingCodeSetService)SpringContextHolder.getBean((Class)SendingCodeSetService.class);
            final WorkFlowService workFlowService = (WorkFlowService)SpringContextHolder.getBean((Class)WorkFlowService.class);
            this.logger.info("=======\u5f00\u59cb\u6267\u884c\u4efb\u52a1, ReviewTaskTimeoutProcJob->executeJob()");
            final List<String> pendingRecordIdList = (List<String>)taskService.findDistinctPendingRecordId();
            for (final String prId : pendingRecordIdList) {
                pendingCodeSet = pendingCodeSetService.get(prId);
                final Task taskCriteria = new Task();
                taskCriteria.setPendingRecordId(prId);
                taskCriteria.setTaskTypeId(Constant.TASK_TYPE_REVIEW);
                taskCriteria.setTaskStatus(Constant.TASK_STATUS_START);
                final List<Task> taskList = (List<Task>)taskService.findListByTask(taskCriteria);
                boolean isTaskTimeout = false;
                for (final Task t : taskList) {
                    final Date taskStartDate = t.getTaskStartDate();
                    if (currentTimeMillis > taskStartDate.getTime() + taskTimeoutMinute * 60000.0) {
                        isTaskTimeout = true;
                        break;
                    }
                }
                if (isTaskTimeout) {
                    for (final Task t : taskList) {
                        t.setTaskHandlerId(user.getId());
                        t.setTaskCompleteDate(new Date(currentTimeMillis));
                        t.setTaskStatus(Constant.TASK_STATUS_STOP);
                        taskService.save(t);
                    }
                    pendingCodeSetService.updateProcessStatus(pendingCodeSet.getId(), (int)Constant.PENDING_STATUS_AGREE);
                    pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Constant.PENDING_STATUS_AGREE);
                    CodeSet codeSet = codeSetService.findByCode(pendingCodeSet.getCodeSetNo());
                    if (codeSet == null) {
                        codeSet = new CodeSet();
                    }
                    codeSet.setUpdateBy(user);
                    codeSet.setUpdateDate(new Date(currentTimeMillis));
                    codeSet.setCodeGroupId(pendingCodeSet.getCodeGroupId());
                    codeSet.setRuleDesc(pendingCodeSet.getRuleDesc());
                    codeSet.setCodeSetNo(pendingCodeSet.getCodeSetNo());
                    codeSet.setCodeSetName(pendingCodeSet.getCodeSetName());
                    codeSet.setFormatDesc(pendingCodeSet.getFormatDesc());
                    codeSet.setCodeSetSort(pendingCodeSet.getCodeSetSort());
                    codeSet.setRemarks(pendingCodeSet.getRemarks());
                    if (pendingCodeSet.getOperation() != 2) {
                        codeSetService.save(codeSet);
                        final List<PendingCodeItem> pendingCodeItems = (List<PendingCodeItem>)pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
                        codeSetDataService.saveCodeSetData((List)pendingCodeItems, codeSet.getId());
                    }
                    final List<TSystem> systems = (List<TSystem>)tSystemService.findSystemByCodeSetId(pendingCodeSet.getId());
                    pendingCodeSetService.saveSendingCodeSet((List)systems, pendingCodeSet);
                    final SendingCodeSet scs = new SendingCodeSet();
                    scs.setCodeSetId(pendingCodeSet.getId());
                    scs.setSendStatus(Integer.valueOf(0));
                    final List<SendingCodeSet> sendingCodeSets = (List<SendingCodeSet>)sendingCodeSetService.findList(scs);
                    for (final SendingCodeSet s : sendingCodeSets) {
                        final TSystem tSystem = tSystemService.get(s.getDestSysId());
                        final SysWebServiceProxy sysWebServiceProxy = new SysWebServiceProxy();
                        sysWebServiceProxy.pushCodeSet2Systems(s.getId(), tSystem);
                    }
                    this.logger.info("\u5f00\u59cb\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
                    codeSetDataService.validtimeCheck();
                    this.logger.info("\u5b8c\u6210\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
                    pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_AGREE, (TSystem)null);
                }
            }
            this.logger.info("=======\u6570\u636e\u590d\u5ba1\u4efb\u52a1\u8d85\u65f6\u5904\u7406\u7ed3\u675f, ReviewTaskTimeoutProcJob->executeJob()");
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), (Throwable)e);
            final LogInout logInout = new LogInout();
            logInout.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
            logInout.setSourceSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
            logInout.setCodeSetName((pendingCodeSet != null) ? pendingCodeSet.getCodeSetName() : "\u6570\u636e\u590d\u5ba1\u4efb\u52a1\u8d85\u65f6\u5904\u7406job");
            logInout.setRecordAmount(Integer.valueOf(0));
            logInout.setStartDate(new Date(currentTimeMillis));
            logInout.setEndDate(new Date());
            logInout.setStatus(Integer.valueOf(0));
            logInout.setRecordType(Integer.valueOf(2));
            logInout.setFailReason("\u5f02\u5e38\u4fe1\u606f\uff1a" + e.getMessage() + ", LocalizedMessage: " + e.getLocalizedMessage());
            logInout.setRemarks("\u6570\u636e\u590d\u5ba1\u4efb\u52a1\u8d85\u65f6\u5904\u7406job\u51fa\u9519\uff01");
            logInoutService.save(logInout);
        }
    }

    public static void main(final String[] args) {
    }
}
