package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.businessdatamrg.service.SendingCodeItemService;
import com.kmvc.jeesite.modules.businessdatamrg.service.SendingCodeSetService;
import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetData;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.common.exception.BizException;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.kmvc.jeesite.modules.tesk.dao.TaskDao;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.kmvc.jeesite.modules.tesk.service.TaskService;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.tesk.service.*;
import com.kmvc.jeesite.modules.businessdatamrg.service.*;
import com.kmvc.jeesite.modules.tesk.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.kmvc.jeesite.modules.common.exception.*;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;

@Service
@Transactional(readOnly = true)
public class PendingCodeSetService extends CrudService<PendingCodeSetDao, PendingCodeSet>
{
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private DistriRelationshipService distriRelationshipService;
    @Autowired
    private CodeSetCacheMappingService codeSetCacheMappingService;
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private SendingCodeSetService sendingCodeSetService;
    @Autowired
    private SendingCodeItemService sendingCodeItemService;
    @Autowired
    private PendingCodesetSystemService pendingCodesetSystemService;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private CodeSetDao codeSetDao;
    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private UserDao userDao;

    public PendingCodeSet get(final String id) {
        BizException.throwWhenNull(id, "\u4e3b\u952e\u4e0d\u80fd\u4e3a\u7a7a");
        final PendingCodeSet pendingCodeSet = (PendingCodeSet)super.get(id);
        if (pendingCodeSet == null) {
            return null;
        }
        final String officeIds = pendingCodeSet.getOfficeIds();
        if (StringUtils.isNotBlank((CharSequence)officeIds)) {
            if (officeIds.contains(",")) {
                final String[] offIdArray = officeIds.split(",");
                final Office office = (Office)this.officeDao.get(offIdArray[0]);
                if (office != null) {
                    pendingCodeSet.setOfficeName(office.getName());
                    pendingCodeSet.setAreaName(office.getArea().getName());
                }
            }
            else {
                final Office office2 = (Office)this.officeDao.get(officeIds);
                if (office2 != null) {
                    pendingCodeSet.setOfficeName(office2.getName());
                    pendingCodeSet.setAreaName(office2.getArea().getName());
                }
            }
        }
        return pendingCodeSet;
    }

    public List<PendingCodeSet> findList(final PendingCodeSet pendingCodeSet) {
        final List<PendingCodeSet> codeSetList = (List<PendingCodeSet>)((PendingCodeSetDao)this.dao).findList(pendingCodeSet);
        final List<PendingCodeSet> codeSetListResult = Lists.newArrayList();
        final String curUserId = UserUtils.getUser().getId();
        for (final PendingCodeSet cs : codeSetList) {
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

    public Page<PendingCodeSet> findPage(final Page<PendingCodeSet> page, final PendingCodeSet pendingCodeSet) {
        pendingCodeSet.setPage((Page)page);
        page.setList((List)this.findList(pendingCodeSet));
        return page;
    }

    @Transactional(readOnly = false)
    public void save(final PendingCodeSet pendingCodeSet) {
        try {
            if (pendingCodeSet.getProcessStatus() != null && pendingCodeSet.getProcessStatus() == 10) {
                pendingCodeSet.setId(IdGen.uuid());
                pendingCodeSet.setIsNewRecord(true);
                pendingCodeSet.setOperation(1);
                pendingCodeSet.setProcessStatus(Constant.PENDING_STATUS_SAVE);
            }
            else if (pendingCodeSet.getProcessStatus() == null) {
                pendingCodeSet.setOperation(0);
                pendingCodeSet.setProcessStatus(Constant.PENDING_STATUS_SAVE);
            }
            final PendingCodeSet existPcs = ((PendingCodeSetDao)this.dao).getByManyAttr(pendingCodeSet);
            if (existPcs != null) {
                pendingCodeSet.setId(existPcs.getId());
                pendingCodeSet.setIsNewRecord(false);
            }
            final User curUser = UserUtils.getUser();
            final Office curOffice = curUser.getOfficeList().get(0);
            String officeIds = pendingCodeSet.getOfficeIds();
            if (StringUtils.isBlank((CharSequence)officeIds)) {
                officeIds = curOffice.getId();
            }
            if (StringUtils.isNotBlank((CharSequence)officeIds) && !officeIds.contains(",")) {
                final Office office = (Office)this.officeDao.get(officeIds);
                pendingCodeSet.setOfficeIds(officeIds + "," + office.getParentIds());
            }
            if (pendingCodeSet.getCoreFlag() == null) {
                final String hncztOfficeCode = DictUtils.getDictValue("hnczt", "officeCode", "001");
                if (curOffice.getCode().equals(hncztOfficeCode)) {
                    pendingCodeSet.setCoreFlag(Constant.CORE_FLAG_TRUE);
                }
                else {
                    pendingCodeSet.setCoreFlag(Constant.CORE_FLAG_FALSE);
                }
            }
            super.save(pendingCodeSet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = false)
    public void delete(PendingCodeSet pendingCodeSet) {
        try {
            if (pendingCodeSet.getOperation() != null && pendingCodeSet.getOperation() == 10) {
                final CodeSet codeSet = this.codeSetService.get(pendingCodeSet.getId());
                final User user = UserUtils.getUser();
                pendingCodeSet = this.toPendingCodeSet(codeSet);
                pendingCodeSet.setId(IdGen.uuid());
                pendingCodeSet.setIsNewRecord(true);
                pendingCodeSet.setOperation(2);
                pendingCodeSet.setProcessStatus(Constant.PENDING_STATUS_REVIEW);
                pendingCodeSet.setUpdateBy(user);
                pendingCodeSet.setUpdateDate(new Date());
                this.save(pendingCodeSet);
                final CodeSetData codeSetData = new CodeSetData();
                codeSetData.setCodeSetId(codeSet.getId());
                final List<CodeSetData> codeSetDatas = this.codeSetDataService.findList(codeSetData);
                for (final CodeSetData c : codeSetDatas) {
                    c.setCodeSetId(pendingCodeSet.getId());
                    final List<String> oldSystemMapping = this.distriRelationshipService.findByItemId(c.getId());
                    final PendingCodeItem p = this.pendingCodeItemService.toPendingCodeItem(c);
                    p.setSystemIds(oldSystemMapping);
                    p.setProcessStatus(Constant.PENDING_STATUS_SAVE);
                    p.setOperation(2);
                    this.pendingCodeItemService.save(p);
                }
                this.createTask(user.getId(), codeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_FIRST, null);
                this.createTask(null, codeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_START, Constant.TASK_TYPE_REVIEW, null);
                this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Constant.PENDING_STATUS_REVIEW);
                if (StringUtils.isNotBlank((CharSequence)codeSet.getId())) {
                    this.codeSetDao.disable(codeSet);
                }
            }
            else {
                this.codeSetCacheMappingService.deleteByCodeSetId(pendingCodeSet.getId());
                this.pendingCodeItemService.deleteByCodeSetId(pendingCodeSet.getId());
                this.taskDao.deleteByPendingRecordId(pendingCodeSet.getId());
                super.delete(pendingCodeSet);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean review(PendingCodeSet pendingCodeSet) throws Exception {
        pendingCodeSet = this.get(pendingCodeSet.getId());
        pendingCodeSet.setProcessStatus(Constant.PENDING_STATUS_REVIEW);
        final Task task = this.taskService.findByCodeSetId(pendingCodeSet.getId(), Constant.TASK_STATUS_START);
        final User currUser = UserUtils.getUser();
        final TSystem system = new TSystem();
        system.setReceivers("," + currUser.getId() + ",");
        final List<TSystem> systemList = this.tSystemService.findList(system);
        if (task != null) {
            if (!StringUtils.isNotBlank((CharSequence)currUser.getId())) {
                return false;
            }
            task.setTaskHandlerId(currUser.getId());
            task.setTaskCompleteDate(new Date());
            task.setTaskStatus(Constant.TASK_STATUS_STOP);
            this.taskService.save(task);
        }
        this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Constant.PENDING_STATUS_REVIEW);
        this.createTask(currUser.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_FIRST, null);
        this.createTask(null, pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_START, Constant.TASK_TYPE_REVIEW, null);
        this.save(pendingCodeSet);
        return true;
    }

    private void procWithBps(final PendingCodeSet pendingCodeSet, final User currUser, final List<String> systemIdList) throws Exception {
        final String procInstId = pendingCodeSet.getProcInstId();
        for (final String systemId : systemIdList) {
            final TSystem system = this.tSystemService.get(systemId);
            this.createTask(null, pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_START, Constant.TASK_TYPE_REVIEW, system);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public PendingCodeSet audit(PendingCodeSet pendingCodeSet) {
        BizException.throwWhenNull(pendingCodeSet.getChangeForm(), "\u662f\u5426\u73b0\u5728\u53d1\u9001\u4e0d\u80fd\u4e3a\u7a7a");
        try {
            final String changeForm = pendingCodeSet.getChangeForm();
            pendingCodeSet = this.get(pendingCodeSet.getId());
            pendingCodeSet.setProcessStatus(Constant.PENDING_STATUS_AGREE);
            this.save(pendingCodeSet);
            final Task task = this.taskService.findByCodeSetId(pendingCodeSet.getId(), Constant.TASK_STATUS_START);
            final User user = UserUtils.getUser();
            if (task != null && StringUtils.isNotBlank((CharSequence)user.getId())) {
                task.setTaskHandlerId(user.getId());
                task.setTaskCompleteDate(new Date());
                task.setTaskStatus(Constant.TASK_STATUS_STOP);
                this.taskService.save(task);
            }
            this.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_FIRST, null);
            this.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_REVIEW, null);
            this.createTask(user.getId(), pendingCodeSet.getCodeSetName(), pendingCodeSet.getId(), Constant.TASK_STATUS_STOP, Constant.TASK_TYPE_AGREE, null);
            this.pendingCodeItemService.updateProcessStatus(pendingCodeSet.getId(), Constant.PENDING_STATUS_AGREE);
            this.updateProcessStatus(pendingCodeSet.getId(), Constant.PENDING_STATUS_AGREE);
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
            this.codeSetService.save(codeSet);
            final List<PendingCodeItem> pendingCodeItems = this.pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
            this.codeSetDataService.saveCodeSetData(pendingCodeItems, codeSet.getId());
            final List<TSystem> systems = this.tSystemService.findSystemByCodeSetId(pendingCodeSet.getId());
            if (changeForm.equals("Y")) {
                this.saveSendingCodeSet(systems, pendingCodeSet);
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
            pendingCodeSet.setId(codeSet.getId());
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), (Throwable)e);
            e.printStackTrace();
        }
        return pendingCodeSet;
    }

    public void updateProcessStatus(final String id, final int processStatus) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("processStatus", processStatus);
        ((PendingCodeSetDao)this.dao).updateProcessStatus(params);
    }

    public PendingCodeSet findByCodeAndStatus(final String codeSetNo, final int status) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("codeSetNo", codeSetNo);
        param.put("status", status);
        return ((PendingCodeSetDao)this.dao).findByCodeAndStatus(param);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void saveSendingCodeSet(final List<TSystem> systems, final PendingCodeSet pendingCodeSet) {
        TSystem system = null;
        if (systems.size() > 0) {
            for (int i = 0; i < systems.size(); ++i) {
                system = systems.get(i);
                SendingCodeSet sendingCodeSet = this.sendingCodeSetService.findBySystemIdAndStatus(system.getId(), pendingCodeSet.getId(), 0);
                if (sendingCodeSet == null) {
                    sendingCodeSet = this.toSendingCodeSet(pendingCodeSet, system);
                    this.sendingCodeSetService.save(sendingCodeSet);
                }
                final List<PendingCodeItem> pendingCodeItems = this.pendingCodeItemService.findByCodeSetIdAndSystemId(pendingCodeSet.getId(), system.getId());
                for (int n = 0; n < pendingCodeItems.size(); ++n) {
                    final PendingCodeItem pendingCodeItem = pendingCodeItems.get(n);
                    if (pendingCodeItem.getOperation() == 4) {
                        final List<String> oldSystemMapping = this.distriRelationshipService.findByItemId(pendingCodeItem.getItemId());
                        final List<String> newSystemMapping = this.codeSetCacheMappingService.findByItemId(pendingCodeItem.getId());
                        if (oldSystemMapping.size() > 0) {
                            final List<String> tempSystemMapping = new ArrayList<String>(oldSystemMapping);
                            tempSystemMapping.removeAll(newSystemMapping);
                            TSystem s = null;
                            for (final String temp : tempSystemMapping) {
                                s = this.tSystemService.get(temp);
                                SendingCodeSet scs = this.sendingCodeSetService.findBySystemIdAndStatus(s.getId(), pendingCodeSet.getId(), 0);
                                if (scs == null) {
                                    scs = this.toSendingCodeSet(pendingCodeSet, s);
                                    this.sendingCodeSetService.save(scs);
                                }
                                final SendingCodeItem sci = this.sendingCodeItemService.findByCodeSetIdAndCodeAndOperation(scs.getId(), pendingCodeItem.getItemCode(), 2);
                                if (sci == null) {
                                    final SendingCodeItem sendingCodeItem = this.toSendingCodeItem(pendingCodeItem, scs.getId(), true);
                                    this.sendingCodeItemService.save(sendingCodeItem);
                                }
                            }
                            if (oldSystemMapping.contains(system.getId())) {
                                pendingCodeItems.remove(n);
                                --n;
                            }
                        }
                    }
                    if (pendingCodeItem.getOperation() == 5) {
                        final List<String> oldSystemMapping = this.distriRelationshipService.findByItemId(pendingCodeItem.getItemId());
                        final List<String> newSystemMapping = this.codeSetCacheMappingService.findByItemId(pendingCodeItem.getId());
                        if (oldSystemMapping.size() > 0) {
                            final List<String> tempSystemMapping = new ArrayList<String>(oldSystemMapping);
                            tempSystemMapping.removeAll(newSystemMapping);
                            TSystem s = null;
                            for (final String temp : tempSystemMapping) {
                                s = this.tSystemService.get(temp);
                                SendingCodeSet scs = this.sendingCodeSetService.findBySystemIdAndStatus(s.getId(), pendingCodeSet.getId(), 0);
                                if (scs == null) {
                                    scs = this.toSendingCodeSet(pendingCodeSet, s);
                                    this.sendingCodeSetService.save(scs);
                                }
                                final SendingCodeItem sci = this.sendingCodeItemService.findByCodeSetIdAndCodeAndOperation(scs.getId(), pendingCodeItem.getItemCode(), 2);
                                if (sci == null) {
                                    final SendingCodeItem sendingCodeItem = this.toSendingCodeItem(pendingCodeItem, scs.getId(), true);
                                    this.sendingCodeItemService.save(sendingCodeItem);
                                }
                            }
                            if (oldSystemMapping.contains(system.getId())) {
                                pendingCodeItems.remove(n);
                                --n;
                            }
                        }
                    }
                }
                if (pendingCodeItems.size() > 0) {
                    for (int n = 0; n < pendingCodeItems.size(); ++n) {
                        final PendingCodeItem pendingCodeItem = pendingCodeItems.get(n);
                        final SendingCodeItem s2 = this.toSendingCodeItem(pendingCodeItem, sendingCodeSet.getId(), false);
                        this.sendingCodeItemService.save(s2);
                    }
                }
                else {
                    this.sendingCodeSetService.delete(sendingCodeSet);
                }
            }
        }
        else {
            final List<PendingCodeItem> pendingCodeItems2 = this.pendingCodeItemService.findByCodeSetId(pendingCodeSet.getId());
            for (int j = 0; j < pendingCodeItems2.size(); ++j) {
                final PendingCodeItem p = pendingCodeItems2.get(j);
                final List<String> oldSystemMapping2 = this.distriRelationshipService.findByItemId(p.getItemId());
                TSystem s3 = null;
                SendingCodeSet scs2 = null;
                for (final String systemId : oldSystemMapping2) {
                    s3 = this.tSystemService.get(systemId);
                    scs2 = this.sendingCodeSetService.findBySystemIdAndStatus(s3.getId(), pendingCodeSet.getId(), 0);
                    if (scs2 == null) {
                        scs2 = this.toSendingCodeSet(pendingCodeSet, s3);
                        this.sendingCodeSetService.save(scs2);
                    }
                    final SendingCodeItem sci2 = this.sendingCodeItemService.findByCodeSetIdAndCodeAndOperation(scs2.getId(), p.getItemCode(), 2);
                    if (sci2 == null) {
                        final SendingCodeItem sendingCodeItem2 = this.toSendingCodeItem(p, scs2.getId(), true);
                        this.sendingCodeItemService.save(sendingCodeItem2);
                    }
                }
            }
        }
    }

    public void createTask(final String accountId, final String codeSetName, final String codeSetId, final Integer taskStatus, final Integer taskType, final TSystem system) {
        final Task task = new Task();
        task.setTaskStartDate(new Date());
        task.setTaskHandlerId(accountId);
        if (accountId != null) {
            task.setTaskCompleteDate(new Date());
        }
        if (system != null) {
            task.setSystemName(system.getSystemName());
            task.setSystemId(system.getId());
        }
        else {
            task.setSystemName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
        }
        task.setTaskStatus(taskStatus);
        task.setTaskTypeId(taskType);
        task.setCodeSetName(codeSetName);
        if (taskType == Constant.TASK_TYPE_FIRST) {
            task.setTaskLink("/codesetmrg/codeSetData/index?codeSetId=" + codeSetId);
        }
        else if (taskType == Constant.TASK_TYPE_REVIEW) {
            task.setTaskLink("/businessdatamrg/pendingAudit/form?id=" + codeSetId);
        }
        else if (taskType == Constant.TASK_TYPE_AGREE || Constant.TASK_TYPE_RETURN.equals(taskType) || Constant.TASK_TYPE_REFUSE.equals(taskType)) {
            task.setTaskLink("/businessdatamrg/pendingAudit/form?id=" + codeSetId);
        }
        task.setPendingRecordId(codeSetId);
        this.taskService.save(task);
    }

    public PendingCodeSet toPendingCodeSet(final CodeSet codeSet) {
        final PendingCodeSet pendingCodeSet = new PendingCodeSet();
        pendingCodeSet.setCodeSetNo(codeSet.getCodeSetNo());
        pendingCodeSet.setCodeSetName(codeSet.getCodeSetName());
        pendingCodeSet.setCodeGroupId(codeSet.getCodeGroupId());
        pendingCodeSet.setRuleDesc(codeSet.getRuleDesc());
        pendingCodeSet.setFormatDesc(codeSet.getFormatDesc());
        pendingCodeSet.setCodeSetSort(codeSet.getCodeSetSort());
        pendingCodeSet.setProcessStatus(codeSet.getProcessStatus());
        pendingCodeSet.setOperation(codeSet.getOperation());
        pendingCodeSet.setRemarks(codeSet.getRemarks());
        pendingCodeSet.setOfficeIds(codeSet.getOfficeIds());
        if (codeSet.getCoreFlag() != null) {
            pendingCodeSet.setCoreFlag(codeSet.getCoreFlag());
        }
        return pendingCodeSet;
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

    public SendingCodeItem toSendingCodeItem(final PendingCodeItem pendingCodeItem, final String recordId, final boolean isOperation) {
        final SendingCodeItem s = new SendingCodeItem();
        s.setRecordId(recordId);
        s.setItemCode(pendingCodeItem.getItemCode());
        s.setParentItemCode(pendingCodeItem.getParentItemCode());
        s.setItemName(pendingCodeItem.getItemName());
        s.setYear(pendingCodeItem.getYear());
        s.setValidStartDate(pendingCodeItem.getValidStartDate());
        s.setValidEndDate(pendingCodeItem.getValidEndDate());
        s.setStatus(pendingCodeItem.getStatus());
        s.setSendStatus(0);
        if (isOperation) {
            s.setOperation(2);
        }
        else {
            s.setOperation(pendingCodeItem.getOperation());
        }
        s.setRemarks(pendingCodeItem.getRemarks());
        s.setItemCodeSort(pendingCodeItem.getItemCodeSort());
        s.setCol1(pendingCodeItem.getCol1());
        return s;
    }

    public Long count(final PendingCodeSet pendingCodeSet) {
        return ((PendingCodeSetDao)this.dao).count(pendingCodeSet);
    }

    public PendingCodeSet getByProcInstId(final String procInstId) {
        return ((PendingCodeSetDao)this.dao).getByProcInstId(procInstId);
    }
}
