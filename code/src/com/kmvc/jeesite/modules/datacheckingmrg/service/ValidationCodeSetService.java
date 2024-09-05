package com.kmvc.jeesite.modules.datacheckingmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationCodeSetDao;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationCodeSetTmpDao;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationDetailDao;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationDetailTmpDao;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSet;
import com.kmvc.jeesite.modules.mdmlog.dao.LogInoutDao;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.sys.dao.TSystemDao;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.tesk.dao.TaskDao;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.kmvc.jeesite.modules.tesk.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.*;
import com.kmvc.jeesite.modules.mdmlog.dao.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.google.common.collect.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import org.apache.commons.lang3.StringUtils;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ValidationCodeSetService extends CrudService<ValidationCodeSetDao, ValidationCodeSet>
{
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private CodeSetDao codeSetDao;
    @Autowired
    private TSystemDao systemDao;
    @Autowired
    private ValidationDetailTmpDao validationDetailTmpDao;
    @Autowired
    private ValidationCodeSetTmpDao validationCodeSetTmpDao;
    @Autowired
    private ValidationDetailDao validationDetailDao;
    @Autowired
    private LogInoutDao logInoutDao;
    @Autowired
    private LogInoutService logInoutService;

    public ValidationCodeSet get(final String id) {
        return (ValidationCodeSet)super.get(id);
    }

    public List<ValidationCodeSet> findList(final ValidationCodeSet validationCodeSet) {
        final List<ValidationCodeSet> codeSetList = (List<ValidationCodeSet>)((ValidationCodeSetDao)this.dao).findList(validationCodeSet);
        final List<ValidationCodeSet> codeSetListResult = Lists.newArrayList();
        final String curUserId = UserUtils.getUser().getId();
        for (final ValidationCodeSet cs : codeSetList) {
            if (StringUtils.isNotBlank((CharSequence)cs.getOfficeIds())) {
                final List<CodeSet> codesetList2 = this.codeSetDao.findOfficeIdByUserId(curUserId);
                for (final CodeSet cs2 : codesetList2) {
                    if (cs.getOfficeIds().contains(cs2.getOfficeIds())) {
                        codeSetListResult.add(cs);
                        break;
                    }
                }
            }
            else {
                codeSetListResult.add(cs);
            }
        }
        return codeSetListResult;
    }

    public Page<ValidationCodeSet> findPage(final Page<ValidationCodeSet> page, final ValidationCodeSet validationCodeSet) {
        validationCodeSet.setPage((Page)page);
        page.setList((List)this.findList(validationCodeSet));
        return page;
    }

    @Transactional(readOnly = false)
    public void save(final ValidationCodeSet validationCodeSet) {
        final String officeIds = validationCodeSet.getOfficeIds();
        if (StringUtils.isNotBlank((CharSequence)officeIds) && !officeIds.contains(",")) {
            final Office office = (Office)this.officeDao.get(officeIds);
            validationCodeSet.setOfficeIds(officeIds + "," + office.getParentIds());
        }
        super.save(validationCodeSet);
    }

    @Transactional(readOnly = false)
    public void delete(final ValidationCodeSet validationCodeSet) {
        super.delete(validationCodeSet);
    }

    @Transactional(readOnly = false)
    public String handle(final ValidationCodeSet valCs) {
        if (valCs == null) {
            return "valCsIsNull";
        }
        if (valCs.getHandleStatus() == 1L) {
            return "alreadyHandle";
        }
        final String curUserId = UserUtils.getUser().getId();
        final Date curDate = new Date();
        valCs.setHandleDate(curDate);
        valCs.setHandleStatus(new Long(1L));
        valCs.setHandleBy(curUserId);
        this.save(valCs);
        final Task task = new Task();
        task.setDataExceptionId(valCs.getId());
        task.setSystemId(null);
        final List<Task> taskList = this.taskDao.findListByTask(task);
        if (taskList.size() == 0) {
            task.setId(IdGen.uuid());
            task.setTaskHandlerId(curUserId);
            task.setTaskStartDate(curDate);
            task.setTaskCompleteDate(curDate);
            task.setTaskStatus(Constant.TASK_STATUS_STOP);
            task.setCodeSetName(valCs.getCodeSetName());
            task.setTaskTypeId(Constant.TASK_TYPE_CHECK);
            task.setTaskLink("/datacheckingmrg/validationCodeSet/form?id=" + valCs.getId() + "&haha=wtf1");
            task.setSystemId(valCs.getSystemId());
            task.setSystemName(valCs.getSystemName());
            this.taskDao.insert(task);
        }
        else {
            for (final Task existTask : taskList) {
                existTask.setTaskHandlerId(curUserId);
                existTask.setTaskCompleteDate(curDate);
                existTask.setTaskStatus(Constant.TASK_STATUS_STOP);
                existTask.setTaskLink("/datacheckingmrg/validationCodeSet/form?id=" + valCs.getId() + "&haha=wtf2");
                this.taskDao.update(existTask);
            }
        }
        return "finish";
    }

    private void setTaskAttr(final String taskHandlerId, final Date taskStartDate, final int status, final String systemName, final String codeSetName, final int type, final Task task) {
        task.setTaskHandlerId(taskHandlerId);
        task.setTaskStartDate(taskStartDate);
        task.setTaskStatus(status);
        task.setSystemName(systemName);
        task.setCodeSetName(codeSetName);
        task.setTaskTypeId(type);
    }

    public boolean findBySystemNoAndDate(final String systemNo) {
        return ((ValidationCodeSetDao)this.dao).findSystemNoAndDate(systemNo).size() > 0;
    }

    @Transactional(readOnly = false)
    public String instantVal() {
        try {
            final User curUser = UserUtils.getUser();
            String systemShort = "";
            final TSystem systemCriteria = new TSystem();
            systemCriteria.setDelFlag("0");
            if (!curUser.isAdmin()) {
                systemCriteria.setReceivers("," + curUser.getId() + ",");
            }
            final List<TSystem> systemList = (List<TSystem>)this.systemDao.findList(systemCriteria);
            this.logger.info("========systemList.size() = " + systemList.size());
            if (systemList == null || systemList.size() == 0) {
                Thread.sleep(3500L);
                return "systemListIsBlank";
            }
            final SysWebServiceProxy proxy = new SysWebServiceProxy();
            for (final TSystem system : systemList) {
                final boolean isCheck = this.findBySystemNoAndDate(system.getSystemCode());
                if (!isCheck) {
                    final List<CodeSet> codeSetList = this.codeSetDao.finBySystemId(system.getId());
                    if (codeSetList.size() == 0) {
                        final LogInout logInout = new LogInout();
                        logInout.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                        logInout.setSourceSysName(system.getSystemName());
                        logInout.setRecordAmount(0);
                        logInout.setStartDate(Calendar.getInstance().getTime());
                        logInout.setEndDate(Calendar.getInstance().getTime());
                        logInout.setStatus(0);
                        logInout.setRecordType(0);
                        logInout.setFailReason("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u5728\u3010" + "MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0" + "\u3011\u4e2d\u6ca1\u6709\u542f\u7528\u7684\u4ee3\u7801\u96c6\u6570\u636e");
                        logInout.setRemarks("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u5728\u3010" + "MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0" + "\u3011\u4e2d\u6ca1\u6709\u542f\u7528\u7684\u4ee3\u7801\u96c6\u6570\u636e");
                        this.logInoutService.save(logInout);
                    }
                    else {
                        this.logger.info("=======\u4ee3\u7406\u53d1\u9001\u4e1a\u52a1\u7cfb\u7edf\uff1a" + system.getSystemShort());
                        this.validationDetailTmpDao.deleteBySystemNo(system.getSystemCode());
                        this.validationCodeSetTmpDao.deleteBySystemNo(system.getSystemCode());
                        this.logInoutDao.deleteByToday();
                        proxy.collectSysCodeSets(system);
                        final Map map = new HashMap();
                        map.put("dataStr", system.getSystemCode());
                        this.validationDetailDao.verificationPro2(map);
                        systemShort = systemShort + system.getSystemShort() + ",";
                    }
                }
                else {
                    final LogInout logInout2 = new LogInout();
                    logInout2.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                    logInout2.setSourceSysName(system.getSystemName());
                    logInout2.setRecordAmount(0);
                    logInout2.setStartDate(Calendar.getInstance().getTime());
                    logInout2.setEndDate(Calendar.getInstance().getTime());
                    logInout2.setStatus(0);
                    logInout2.setRecordType(0);
                    logInout2.setFailReason("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                    logInout2.setRemarks("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                    this.logInoutService.save(logInout2);
                    this.logger.info("\u7f16\u7801\u4e3a\uff1a" + system.getSystemCode() + "\u7684\u4e1a\u52a1\u7cfb\u7edf\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                }
            }
            proxy.asyncCommit();
            Thread.sleep(5900L);
            this.logger.info("=======\u4ee5\u4e0b\u4e1a\u52a1\u7cfb\u7edf\uff1a" + systemShort + "\u5df2\u6267\u884c\u6838\u67e5\u64cd\u4f5c\uff0c\u8bf7\u67e5\u770b\u6838\u67e5\u7ed3\u679c");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "finish";
    }
}
