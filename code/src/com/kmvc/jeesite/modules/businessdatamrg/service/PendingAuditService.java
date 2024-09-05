package com.kmvc.jeesite.modules.businessdatamrg.service;

import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodeItemDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.common.Util;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.kmvc.jeesite.modules.tesk.service.TaskService;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.tesk.service.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import java.util.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import org.apache.commons.lang3.StringUtils;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;

@Service
@Transactional(readOnly = true)
public class PendingAuditService extends CrudService<PendingCodeSetDao, PendingCodeSet>
{
    @Autowired
    private PendingCodeSetService pendingCodeSetService;
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SendingCodeSetService sendingCodeSetService;
    @Autowired
    private SendingCodeItemService sendingCodeItemService;
    @Autowired
    private PendingCodeItemDao pendingCodeItemDao;
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private CodeSetCacheMappingService codeSetCacheMappingService;
    @Autowired
    private CodeSetDao codeSetDao;

    public PendingCodeSet get(final String id) {
        return (PendingCodeSet)super.get(id);
    }

    public List<PendingCodeSet> findList(final PendingCodeSet pendingCodeSet) {
        return (List<PendingCodeSet>)super.findList(pendingCodeSet);
    }

    public Page<PendingCodeSet> findPage(final Page<PendingCodeSet> page, final PendingCodeSet pendingCodeSet) {
        return (Page<PendingCodeSet>)super.findPage((Page)page, pendingCodeSet);
    }

    public Page<PendingCodeSet> taskPendingCodeSetList(final Page<PendingCodeSet> page, final PendingCodeSet pendingCodeSet) {
        page.setOrderBy("t.task_start_date DESC");
        pendingCodeSet.setPage((Page)page);
        final User curUser = UserUtils.getUser();
        if (!curUser.isAdmin()) {
            final TSystem system = new TSystem();
            system.setReceivers("," + curUser.getId() + ",");
            final List<TSystem> systemList = this.tSystemService.findList(system);
            for (final TSystem sys : systemList) {
                if (pendingCodeSet.getSystemIds() == null) {
                    pendingCodeSet.setSystemIds((List)new ArrayList());
                }
                pendingCodeSet.getSystemIds().add(sys.getId());
            }
            if (systemList.size() > 0) {
                pendingCodeSet.getSystemIds().add(" ");
            }
        }
        page.setList(((PendingCodeSetDao)this.dao).taskPendingCodeSetList(pendingCodeSet));
        return page;
    }

    public Page<PendingCodeSet> taskPendingCodeSetAllList(final Page<PendingCodeSet> page, final PendingCodeSet pendingCodeSet) {
        page.setOrderBy("t.task_start_date");
        pendingCodeSet.setPage((Page)page);
        page.setList(((PendingCodeSetDao)this.dao).taskPendingCodeSetAllList(pendingCodeSet));
        return page;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean pendingAudit(final PendingCodeSet pendingCodeSet) throws Exception {
        Task task = null;
        final User user = pendingCodeSet.getCurrentUser();
        final Task taskCriteria = new Task();
        taskCriteria.setSystemId(null);
        taskCriteria.setPendingRecordId(pendingCodeSet.getId());
        taskCriteria.setTaskTypeId(Constant.TASK_TYPE_REVIEW);
        taskCriteria.setTaskStatus(Constant.TASK_STATUS_START);
        final List<Task> taskList = this.taskService.findListByTask(taskCriteria);
        if (taskList.size() == 0) {
            return false;
        }
        if (task == null) {
            task = taskList.get(0);
        }
        if (task == null) {
            return false;
        }
        int taskType = 0;
        final int status = pendingCodeSet.getProcessStatus();
        switch (status) {
            case 1: {
                taskType = 1;
                break;
            }
            case 3: {
                taskType = 5;
                break;
            }
            case 4: {
                taskType = 6;
                break;
            }
        }
        if (taskType == 0) {
            return false;
        }
        final Task taskDb = this.taskService.get(task.getId());
        if (taskDb != null && Constant.TASK_STATUS_STOP.equals(taskDb.getTaskStatus())) {
            return false;
        }
        if (status == 3) {
            if (taskList.size() == 1) {
                this.pendingCodeSetService.updateProcessStatus(pendingCodeSet.getId(), status);
                this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Integer.valueOf(status));
                CodeSet codeSet = this.codeSetService.findByCode(pendingCodeSet.getCodeSetNo());
                if (codeSet == null) {
                    codeSet = new CodeSet();
                }
                codeSet.setUpdateBy(user);
                codeSet.setUpdateDate(new Date());
                codeSet.setCodeGroupId(pendingCodeSet.getCodeGroupId());
                codeSet.setRuleDesc(pendingCodeSet.getRuleDesc());
                codeSet.setCodeSetNo(pendingCodeSet.getCodeSetNo());
                codeSet.setCodeSetName(pendingCodeSet.getCodeSetName());
                codeSet.setFormatDesc(pendingCodeSet.getFormatDesc());
                codeSet.setCodeSetSort(pendingCodeSet.getCodeSetSort());
                codeSet.setRemarks(pendingCodeSet.getRemarks());
                codeSet.setOfficeIds(pendingCodeSet.getOfficeIds());
                if (pendingCodeSet.getOperation() != 2) {
                    this.codeSetService.save(codeSet);
                    final List<PendingCodeItem> pendingCodeItems = (List<PendingCodeItem>)this.pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
                    this.codeSetDataService.saveCodeSetData((List)pendingCodeItems, codeSet.getId());
                }
                final List<TSystem> systems = this.tSystemService.findSystemByCodeSetId(pendingCodeSet.getId());
                if (pendingCodeSet.getChangeForm() != null && pendingCodeSet.getChangeForm().equals("Y")) {
                    this.pendingCodeSetService.saveSendingCodeSet((List)systems, pendingCodeSet);
                    final SendingCodeSet scs = new SendingCodeSet();
                    scs.setCodeSetId(pendingCodeSet.getId());
                    scs.setSendStatus(0);
                    final List<SendingCodeSet> sendingCodeSets = this.sendingCodeSetService.findList(scs);
                    for (final SendingCodeSet s : sendingCodeSets) {
                        final TSystem tSystem = this.tSystemService.get(s.getDestSysId());
                        final SysWebServiceProxy sysWebServiceProxy = new SysWebServiceProxy();
                        sysWebServiceProxy.pushCodeSet2Systems(s.getId(), tSystem);
                    }
                    Thread.sleep(2900L);
                }
                this.logger.info("\u5f00\u59cb\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
                this.codeSetDataService.validtimeCheck();
                this.logger.info("\u5b8c\u6210\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
            }
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Integer.valueOf(taskType), (TSystem)null);
        }
        else if (status == 1) {
            this.pendingCodeSetService.updateProcessStatus(pendingCodeSet.getId(), status);
            this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Integer.valueOf(status));
            for (final Task ts : taskList) {
                if (ts != null && !ts.getId().equals(task.getId()) && StringUtils.isNotBlank((CharSequence)user.getId())) {
                    ts.setTaskHandlerId("1");
                    ts.setTaskCompleteDate(new Date());
                    ts.setTaskStatus(Constant.TASK_STATUS_STOP);
                    this.taskService.save(ts);
                }
            }
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_RETURN, (TSystem)null);
            final User createUser = pendingCodeSet.getCreateBy();
            final TSystem s2 = new TSystem();
            s2.setReceivers("," + createUser.getId() + ",");
            final List<TSystem> sList = this.tSystemService.findList(s2);
            final List<String> systemIdList = (List<String>)this.codeSetCacheMappingService.findByCodesetId(pendingCodeSet.getId());
            final List<TSystem> currentSystem = Util.getIntersection(systemIdList, sList);
            TSystem system2 = null;
            if (currentSystem.size() > 0) {
                system2 = currentSystem.get(0);
            }
            this.pendingCodeSetService.createTask(createUser.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_START, Integer.valueOf(taskType), system2);
        }
        else {
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Integer.valueOf(taskType), (TSystem)null);
            final String systemId = task.getSystemId();
            if (taskList.size() == 1) {
                CodeSet codeSet2 = this.codeSetDao.findByCode2(pendingCodeSet.getCodeSetNo());
                if (codeSet2 == null) {
                    codeSet2 = new CodeSet();
                }
                else {
                    this.pendingCodeSetService.updateProcessStatus(pendingCodeSet.getId(), status);
                    this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Integer.valueOf(status));
                }
                codeSet2.setUpdateBy(user);
                codeSet2.setUpdateDate(new Date());
                codeSet2.setCodeGroupId(pendingCodeSet.getCodeGroupId());
                codeSet2.setRuleDesc(pendingCodeSet.getRuleDesc());
                codeSet2.setCodeSetNo(pendingCodeSet.getCodeSetNo());
                codeSet2.setCodeSetName(pendingCodeSet.getCodeSetName());
                codeSet2.setFormatDesc(pendingCodeSet.getFormatDesc());
                codeSet2.setCodeSetSort(pendingCodeSet.getCodeSetSort());
                codeSet2.setRemarks(pendingCodeSet.getRemarks());
                if (pendingCodeSet.getOperation() == 2 && "1".equals(codeSet2.getDelFlag())) {
                    this.codeSetDao.enable(codeSet2);
                }
            }
        }
        if (task != null) {
            if (!StringUtils.isNotBlank((CharSequence)user.getId())) {
                return false;
            }
            task.setTaskHandlerId(user.getId());
            task.setTaskCompleteDate(new Date());
            task.setTaskStatus(Constant.TASK_STATUS_STOP);
            this.taskService.save(task);
        }
        return true;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean pendingAudit2(final PendingCodeSet pendingCodeSet) throws Exception {
        final User user = pendingCodeSet.getCurrentUser();
        final TSystem system = new TSystem();
        system.setReceivers("," + user.getId() + ",");
        final List<TSystem> systemList = this.tSystemService.findList(system);
        final Task taskCriteria = new Task();
        taskCriteria.setPendingRecordId(pendingCodeSet.getId());
        taskCriteria.setTaskTypeId(Constant.TASK_TYPE_REVIEW);
        final List<Task> taskList = this.taskService.findListByTask(taskCriteria);
        Task task = null;
        boolean isAllTaskDealed = true;
        for (final Task t : taskList) {
            if (systemList.size() > 0 && systemList.get(0).getId().equals(t.getSystemId()) && task == null) {
                task = t;
            }
            if (Constant.TASK_STATUS_START.equals(t.getTaskStatus()) && !systemList.get(0).getId().equals(t.getSystemId())) {
                isAllTaskDealed = false;
            }
        }
        if (task != null && Constant.TASK_STATUS_STOP.equals(task.getTaskStatus())) {
            return false;
        }
        int taskType = 0;
        final int status = pendingCodeSet.getProcessStatus();
        switch (status) {
            case 1: {
                taskType = 1;
                break;
            }
            case 3: {
                taskType = 5;
                break;
            }
            case 4: {
                taskType = 6;
                break;
            }
        }
        if (taskType == 0) {
            return false;
        }
        this.pendingCodeSetService.updateProcessStatus(pendingCodeSet.getId(), status);
        this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Integer.valueOf(status));
        if (status == 3) {
            CodeSet codeSet = this.codeSetService.findByCode(pendingCodeSet.getCodeSetNo());
            if (codeSet == null) {
                codeSet = new CodeSet();
            }
            codeSet.setUpdateBy(user);
            codeSet.setUpdateDate(new Date());
            codeSet.setCodeGroupId(pendingCodeSet.getCodeGroupId());
            codeSet.setRuleDesc(pendingCodeSet.getRuleDesc());
            codeSet.setCodeSetNo(pendingCodeSet.getCodeSetNo());
            codeSet.setCodeSetName(pendingCodeSet.getCodeSetName());
            codeSet.setFormatDesc(pendingCodeSet.getFormatDesc());
            codeSet.setCodeSetSort(pendingCodeSet.getCodeSetSort());
            codeSet.setRemarks(pendingCodeSet.getRemarks());
            if (pendingCodeSet.getOperation() != 2) {
                this.codeSetService.save(codeSet);
                final List<PendingCodeItem> pendingCodeItems = (List<PendingCodeItem>)this.pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
                this.codeSetDataService.saveCodeSetData((List)pendingCodeItems, codeSet.getId());
            }
            final List<TSystem> systems = systemList;
            if (pendingCodeSet.getChangeForm() != null && pendingCodeSet.getChangeForm().equals("Y")) {
                this.pendingCodeSetService.saveSendingCodeSet((List)systems, pendingCodeSet);
                final SendingCodeSet scs = new SendingCodeSet();
                scs.setCodeSetId(pendingCodeSet.getId());
                scs.setSendStatus(0);
                final List<SendingCodeSet> sendingCodeSets = this.sendingCodeSetService.findList(scs);
                for (final SendingCodeSet s : sendingCodeSets) {
                    final TSystem tSystem = this.tSystemService.get(s.getDestSysId());
                    final SysWebServiceProxy sysWebServiceProxy = new SysWebServiceProxy();
                    sysWebServiceProxy.pushCodeSet2Systems(s.getId(), tSystem);
                }
            }
            this.logger.info("\u5f00\u59cb\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
            this.codeSetDataService.validtimeCheck();
            this.logger.info("\u5b8c\u6210\u68c0\u67e5\u4ee3\u7801\u96c6\u662f\u5426\u542f\u7528");
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Integer.valueOf(taskType), (TSystem)systemList.get(0));
        }
        else if (status == 1) {
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_RETURN, (TSystem)systemList.get(0));
            this.pendingCodeSetService.createTask((String)null, pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_START, Integer.valueOf(taskType), (TSystem)systemList.get(0));
            for (final Task reviewTask : taskList) {
                if (!reviewTask.getId().equals(task.getId())) {
                    this.taskService.delete(reviewTask);
                }
            }
        }
        else {
            this.pendingCodeSetService.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Integer.valueOf(taskType), (TSystem)systemList.get(0));
            for (final Task reviewTask : taskList) {
                if (Constant.TASK_STATUS_START.equals(reviewTask.getTaskStatus()) && !reviewTask.getId().equals(task.getId())) {
                    this.taskService.delete(reviewTask);
                }
            }
        }
        if (task != null) {
            if (!StringUtils.isNotBlank((CharSequence)user.getId())) {
                return false;
            }
            task.setTaskHandlerId(user.getId());
            task.setTaskCompleteDate(new Date());
            task.setTaskStatus(Constant.TASK_STATUS_STOP);
            this.taskService.save(task);
        }
        return true;
    }

    public Page<PendingCodeItem> reviewList(final Page<PendingCodeItem> page, final PendingCodeItem pendingCodeItem) {
        pendingCodeItem.setPage((Page)page);
        final List<PendingCodeItem> list = (List<PendingCodeItem>)this.pendingCodeItemDao.reviewList(pendingCodeItem);
        page.setList((List)list);
        page.setCount((long)page.getList().size());
        return page;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public void saveSendingCodeSets(final List<TSystem> systems, final PendingCodeSet pendingCodeSet) {
        CodeSet codeSet = this.codeSetService.findByCode(pendingCodeSet.getCodeSetNo());
        if (codeSet == null) {
            codeSet = new CodeSet();
        }
        codeSet.setUpdateBy(pendingCodeSet.getCurrentUser());
        codeSet.setUpdateDate(new Date());
        codeSet.setCodeGroupId(pendingCodeSet.getCodeGroupId());
        codeSet.setRuleDesc(pendingCodeSet.getRuleDesc());
        codeSet.setCodeSetNo(pendingCodeSet.getCodeSetNo());
        codeSet.setCodeSetName(pendingCodeSet.getCodeSetName());
        codeSet.setFormatDesc(pendingCodeSet.getFormatDesc());
        codeSet.setCodeSetSort(pendingCodeSet.getCodeSetSort());
        codeSet.setRemarks(pendingCodeSet.getRemarks());
        this.codeSetService.save(codeSet);
        final List<PendingCodeItem> pendingCodeItems = (List<PendingCodeItem>)this.pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
        this.codeSetDataService.saveCodeSetData((List)pendingCodeItems, codeSet.getId());
        for (final TSystem system : systems) {
            final PendingCodeSet pendingCodeSetTemp = (PendingCodeSet)super.get(pendingCodeSet);
            final SendingCodeSet sendingCodeSet = this.toSendingCodeSet(pendingCodeSetTemp, system);
            final String uuid = IdGen.uuid();
            sendingCodeSet.setIsNewRecord(true);
            sendingCodeSet.setId(uuid);
            this.sendingCodeSetService.save(sendingCodeSet);
            final List<PendingCodeItem> list = (List<PendingCodeItem>)this.pendingCodeItemService.findByCodeSetId(pendingCodeSetTemp.getId());
            for (final PendingCodeItem pendingCodeItemTemp : list) {
                final SendingCodeItem sendingCodeItem = this.toSendingCodeItem(pendingCodeItemTemp, uuid);
                this.sendingCodeItemService.save(sendingCodeItem);
            }
        }
    }

    public SendingCodeSet toSendingCodeSet(final PendingCodeSet pendingCodeSet, final TSystem system) {
        final SendingCodeSet sendingCodeSet = new SendingCodeSet();
        sendingCodeSet.setDestSysId(system.getId());
        sendingCodeSet.setDestSysCode(system.getSystemCode());
        sendingCodeSet.setDestSysName(system.getSystemName());
        sendingCodeSet.setCodeSetId(pendingCodeSet.getId());
        sendingCodeSet.setCodeSetNo(pendingCodeSet.getCodeSetNo());
        sendingCodeSet.setCodeSetName(pendingCodeSet.getCodeSetName());
        sendingCodeSet.setSendDate(new Date());
        sendingCodeSet.setSendStatus(0);
        sendingCodeSet.setRemarks(pendingCodeSet.getRemarks());
        sendingCodeSet.setOperation(pendingCodeSet.getOperation());
        sendingCodeSet.setOfficeIds(pendingCodeSet.getOfficeIds());
        return sendingCodeSet;
    }

    public SendingCodeItem toSendingCodeItem(final PendingCodeItem pendingCodeItem, final String recordId) {
        final SendingCodeItem s = new SendingCodeItem();
        s.setRecordId(recordId);
        s.setItemCode(pendingCodeItem.getItemCode());
        s.setParentItemCode(pendingCodeItem.getParentItemCode());
        s.setItemName(pendingCodeItem.getItemName());
        s.setYear(pendingCodeItem.getYear());
        s.setValidStartDate(pendingCodeItem.getValidStartDate());
        s.setValidEndDate(pendingCodeItem.getValidStartDate());
        s.setStatus(pendingCodeItem.getStatus());
        s.setSendStatus(0);
        s.setOperation(pendingCodeItem.getOperation());
        s.setRemarks(pendingCodeItem.getRemarks());
        s.setItemCodeSort(pendingCodeItem.getItemCodeSort());
        s.setCol1(pendingCodeItem.getCol1());
        return s;
    }
}
