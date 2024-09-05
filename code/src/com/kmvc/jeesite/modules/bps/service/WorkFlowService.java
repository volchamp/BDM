package com.kmvc.jeesite.modules.bps.service;

import org.springframework.stereotype.*;
import com.primeton.bps.disttrans.api.*;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import com.eos.workflow.omservice.*;
import com.thinkgem.jeesite.common.utils.*;
import com.primeton.workflow.api.*;
import com.eos.workflow.data.*;
import com.eos.workflow.api.*;
import com.thinkgem.jeesite.common.config.*;

@Service
public class WorkFlowService
{
    private IBPSServiceClient client;
    private IClientGlobalTransactionManager globalTxMgr;
    private IWFOMService omService;
    private static final String isDispatch;
    private static final int workItemCurrentState = 4;

    public WorkFlowService() throws Exception {
        this.client = null;
        this.globalTxMgr = null;
        this.omService = null;
        if (null == this.client) {
            this.client = BPSServiceClientFactory.getDefaultClient();
        }
        if (null != this.client && null == this.globalTxMgr) {
            this.globalTxMgr = this.client.getClientGlobalTransactionManager();
        }
        if (null != this.client && null == this.omService) {
            this.omService = this.client.getOMService();
        }
    }

    public long createProcess(final String processDefName, final String processInstName, final String processInstDesc) throws Exception {
        final IWFProcessInstManager mng = this.client.getProcessInstManager();
        final long processInstID = mng.createProcessInstance(processDefName, processInstName, processInstDesc);
        return processInstID;
    }

    public void startProcessInstance(final long proInstId) throws Exception {
        final IWFProcessInstManager pim = this.client.getProcessInstManager();
        pim.startProcessInstance(proInstId);
    }

    public void startAndFinishFirstWorkItem(final long processInstID) throws Exception {
        final IWFProcessInstManager mng = this.client.getProcessInstManager();
        mng.startProcessInstAndFinishFirstWorkItem(processInstID, false, new Object[0]);
    }

    public void startProcessInstance(final long proInstId, final Object... params) throws Exception {
        final IWFProcessInstManager pim = this.client.getProcessInstManager();
        pim.startProcessInstance(proInstId, true, params);
    }

    public long createAndStartProcInstAndSetRelativeData(final String proDefName, final String proInstName, final String proInstDesc, final Map<String, Object> paramMap) throws Exception {
        final IWFProcessInstManager pim = this.client.getProcessInstManager();
        return pim.createAndStartProcInstAndSetRelativeData(proDefName, proInstName, proInstDesc, true, (Map)paramMap);
    }

    public long createAndStartProcInstByVersionAndSetRelativeData(final String proDefName, final String proInstName, final String proInstDesc, final String version, final Map<String, Object> paramMap) throws Exception {
        final IWFProcessInstManager pim = this.client.getProcessInstManager();
        return pim.createAndStartProcInstByVersionAndSetRelativeData(proDefName, proInstName, proInstDesc, version, true, (Map)paramMap);
    }

    public WFProcessInst queryProcessInstDetail(final long processInstID) throws Exception {
        final IWFProcessInstManager mng = this.client.getProcessInstManager();
        return mng.queryProcessInstDetail(processInstID);
    }

    public boolean setRelativeData(final long processInstID, final String xpath, final Object value) throws Exception {
        final IWFRelativeDataManager mng = this.client.getRelativeDataManager();
        mng.setRelativeData(processInstID, xpath, value);
        return true;
    }

    public boolean setRelativeDataMap(final long processInstID, final Map<String, Object> paramMap) throws Exception {
        final IWFRelativeDataManager mng = this.client.getRelativeDataManager();
        mng.setRelativeDataBatch(processInstID, (Map)paramMap);
        return true;
    }

    public void beginTransaction() {
        this.globalTxMgr.begin();
    }

    public void commitTransaction() {
        this.globalTxMgr.commit();
    }

    public void rollbackTransaction() {
        this.globalTxMgr.rollback();
    }

    public int getStatus() {
        return this.globalTxMgr.getStatus();
    }

    public void rollbackWithCompensate(final IClientTransactionCompensate compensate) {
        this.globalTxMgr.rollbackWithCompensate(compensate);
    }

    public long createAndStartActivityInstance(final long processInstID, final String activityDefID) throws Exception {
        final IWFActivityInstManager activityInstManager = this.client.getActivityInstManager();
        return activityInstManager.createAndStartActivityInstance(processInstID, activityDefID);
    }

    public void appointNextActivities(final long workItemID, final String appointStrategy, final String[] activityDefID) throws Exception {
        final IWFFreeFlowManager freeFlowManager = this.client.getFreeFlowManager();
        freeFlowManager.appointNextActivities(workItemID, appointStrategy, activityDefID);
    }

    public boolean finishWorkItem(final long workItemID) throws Exception {
        final IWFWorkItemManager mng = this.client.getWorkItemManager();
        mng.finishWorkItem(workItemID, false);
        return true;
    }

    public boolean finishWorkItem(final String procInstId, final String activityDefId, final String userId, final String userName, final Object relativeData) throws Exception {
        if (StringUtils.isNotBlank((CharSequence)procInstId)) {
            final List<WFWorkItem> listwfwork = this.queryWorkItemsByProcessInstID(Long.parseLong(procInstId), activityDefId);
            WFWorkItem workItem = listwfwork.get(0);
            this.setCurrentUser(userId, userName);
            if (workItem.getCurrentState() == 4) {
                if ("1".equals(userId)) {
                    this.addWorkItemParticipant(workItem.getWorkItemID(), userId, userName);
                }
                this.assignWorkItemToSelf(workItem.getWorkItemID());
            }
            workItem = this.getWFWorkItem(workItem.getWorkItemID());
            if (workItem.getCurrentState() == 10) {
                this.setRelativeData(Long.parseLong(procInstId), "wfStatus", relativeData);
                this.finishWorkItem(workItem.getWorkItemID());
            }
        }
        return true;
    }

    public void setCurrentUser(final String userId, final String userName) {
        BPSServiceClientFactory.getLoginManager().setCurrentUser(userId, userName);
    }

    public void terminateWorkItem(final long workItemID) throws Exception {
        this.client.getWorkItemManager().terminateWorkItem(workItemID);
    }

    public void finishActivityInstance(final long activityInstID) throws Exception {
        this.client.getActivityInstManager().finishActivityInstance(activityInstID);
    }

    public void finishActivityInstByActivityID(final long processInstId, final String activityDefId) throws Exception {
        this.client.getActivityInstManager().finishActivityInstByActivityID(processInstId, activityDefId);
    }

    public List<WFActivityDefine> queryAppointedNextActivities(final long workItemID) throws Exception {
        return (List<WFActivityDefine>)this.client.getFreeFlowManager().queryAppointedNextActivities(workItemID);
    }

    public void addWorkItemParticipant(final long workItemId, final String id, final String name) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        final WFParticipant participant = new WFParticipant(id, name, "person");
        workItemManager.addWorkItemParticipant(workItemId, participant);
    }

    public boolean assignWorkItemToSelf(final long workItemId) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        workItemManager.assignWorkItemToSelf(workItemId);
        return true;
    }

    public boolean clearWorkItemParticipant(final long workItemId) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        workItemManager.clearWorkItemParticipant(workItemId);
        return true;
    }

    public List<WFWorkItem> findUserTasks(final String userId, final PageCond pageCond) {
        try {
            final IWFWorklistQueryManager workListMng = this.client.getWorklistQueryManager();
            return (List<WFWorkItem>)workListMng.queryPersonWorkItems(userId, "ALL", "ALL", pageCond);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public List<WFWorkItem> findUserFinishedTasks(final String userId, final PageCond pageCond) {
        final IWFWorklistQueryManager workListMng = this.client.getWorklistQueryManager();
        try {
            return (List<WFWorkItem>)workListMng.queryPersonFinishedWorkItems(userId, "ALL", false, pageCond);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public Object getRelativeData(final long processInstID, final String xpath) {
        try {
            final IWFRelativeDataManager mng = this.client.getRelativeDataManager();
            return mng.getRelativeData(processInstID, xpath);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public WFWorkItem getWFWorkItem(final long workItemId) {
        try {
            return this.client.getWorkItemManager().queryWorkItemDetail(workItemId);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public List<WFWorkItem> queryNextWorkItemsByActivityInstID(final long actInstID, final boolean flag) throws Exception {
        return (List<WFWorkItem>)this.client.getWorkItemManager().queryNextWorkItemsByActivityInstID(actInstID, flag);
    }

    public List<WFWorkItem> queryWorkItemsByActivityInstID(final long actInstID) throws Exception {
        return (List<WFWorkItem>)this.client.getWorkItemManager().queryWorkItemsByActivityInstID(actInstID, (PageCond)null);
    }

    public List<WFWorkItem> queryWorkItemsByProcessInstID(final long processInstId) throws Exception {
        final List<WFActivityInst> wf = (List<WFActivityInst>)this.client.getActivityInstManager().queryActivityInstsByProcessInstID(processInstId, (PageCond)null);
        final List<WFWorkItem> wfWorkItems = (List<WFWorkItem>)this.client.getWorkItemManager().queryWorkItemsByActivityInstID(wf.get(wf.size() - 1).getActivityInstID(), (PageCond)null);
        return wfWorkItems;
    }

    public List<WFWorkItem> queryWorkItemsByProcessInstID(final long processInstId, final String activityDefId) throws Exception {
        final WFActivityInst activityInst = this.findLastActivityInstByActivityID(processInstId, activityDefId);
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        final List<WFWorkItem> workItems = (List<WFWorkItem>)workItemManager.queryWorkItemsByActivityInstID(activityInst.getActivityInstID(), (PageCond)null);
        return workItems;
    }

    public WFActivityInst findLastActivityInstByActivityID(final long processInstId, final String activityDefId) {
        try {
            return this.client.getActivityInstManager().findLastActivityInstByActivityID(processInstId, activityDefId);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public void assignWorkItemToPerson(final long workItemID, final String personID) {
        try {
            this.client.getWorkItemManager().assignWorkItemToPerson(workItemID, personID);
        }
        catch (WFServiceException e) {
            throw Exceptions.unchecked((Exception)e);
        }
    }

    public boolean reassignWorkItem(final List<WFWorkItem> workItems, final String actUserId, final String activityId) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        final WFWorkItem workItem = workItems.get(0);
        if (workItem != null && workItem.getActivityDefID().equals(activityId) && (workItem.getCurrentState() == 10 || workItem.getCurrentState() == 4)) {
            workItemManager.reassignWorkItem(workItems.get(0).getWorkItemID(), actUserId);
            return true;
        }
        return false;
    }

    public boolean withdrawWorkItem(final long processInstId, final String activityDefId) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        final List<WFWorkItem> workItems = this.queryWorkItemsByProcessInstID(processInstId, activityDefId);
        final WFWorkItem workItem = workItems.get(0);
        if (workItem != null && workItem.getActivityDefID().equals(activityDefId)) {
            workItemManager.withdrawWorkItem(workItem.getWorkItemID());
            return true;
        }
        return false;
    }

    public boolean removeWorkItemParticipant(final long workItemID, final String userId, final String userName) throws Exception {
        final IWFWorkItemManager workItemManager = this.client.getWorkItemManager();
        final WFParticipant participant = new WFParticipant(userId, userName, "P");
        workItemManager.removeWorkItemParticipant(workItemID, participant);
        return true;
    }

    public void suspendProcessInst(final long proInstId) throws Exception {
        this.client.getProcessInstManager().suspendProcessInstance(proInstId);
    }

    public void resumeProcessInst(final long proInstId) throws Exception {
        this.client.getProcessInstManager().resumeProcessInstance(proInstId);
    }

    public void terminateProcessInst(final long proInstId) throws Exception {
        this.client.getProcessInstManager().terminateProcessInstance(proInstId);
    }

    public List<WFProcessDefine> queryProcessesByName(final String processDefName, final String state, final PageCond pageCond) throws Exception {
        final IWFDefinitionQueryManager definitionQueryManager = this.client.getDefinitionQueryManager();
        return (List<WFProcessDefine>)definitionQueryManager.queryProcessesByName(processDefName, state, pageCond);
    }

    static {
        isDispatch = Global.getConfig("task.dispatch");
    }
}
