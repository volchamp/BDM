package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;

public class PendingCodeSet extends DataEntity<PendingCodeSet>
{
    private static final long serialVersionUID = 1L;
    private String codeSetNo;
    private String codeSetName;
    private String codeGroupId;
    private String ruleDesc;
    private String formatDesc;
    private Integer codeSetSort;
    private Integer processStatus;
    private Integer operation;
    private String officeIds;
    private String procInstId;
    private Integer coreFlag;
    private String taskHandlerId;
    private Integer taskStatus;
    private String systemId;
    private Date taskCompleteDate;
    private Date taskCompleteDateStart;
    private Date taskCompleteDateEnd;
    private String officeName;
    private List<String> systemIds;
    private String workItemId;
    private String systemName;
    private String areaName;
    private String changeForm;

    public PendingCodeSet() {
    }

    public PendingCodeSet(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    @Length(min = 0, max = 100, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }

    @Length(min = 0, max = 64, message = "\u4ee3\u7801\u96c6\u5206\u7ec4ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getCodeGroupId() {
        return this.codeGroupId;
    }

    public void setCodeGroupId(final String codeGroupId) {
        this.codeGroupId = codeGroupId;
    }

    @Length(min = 0, max = 2000, message = "\u4ee3\u7801\u89c4\u5219\u8bf4\u660e\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 2000 \u4e4b\u95f4")
    public String getRuleDesc() {
        return this.ruleDesc;
    }

    public void setRuleDesc(final String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    @Length(min = 0, max = 100, message = "\u8868\u793a\u683c\u5f0f\u8bf4\u660e\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getFormatDesc() {
        return this.formatDesc;
    }

    public void setFormatDesc(final String formatDesc) {
        this.formatDesc = formatDesc;
    }

    public Integer getCodeSetSort() {
        return this.codeSetSort;
    }

    public void setCodeSetSort(final Integer codeSetSort) {
        this.codeSetSort = codeSetSort;
    }

    public Integer getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(final Integer processStatus) {
        this.processStatus = processStatus;
    }

    @Length(min = 0, max = 1, message = "\u64cd\u4f5c\u7c7b\u578b0\uff1a\u65b0\u589e1\uff1a\u4fee\u65392\uff1a\u64a4\u6d88\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 1 \u4e4b\u95f4")
    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public String getChangeForm() {
        return this.changeForm;
    }

    public void setChangeForm(final String changeForm) {
        this.changeForm = changeForm;
    }

    public String getTaskHandlerId() {
        return this.taskHandlerId;
    }

    public void setTaskHandlerId(final String taskHandlerId) {
        this.taskHandlerId = taskHandlerId;
    }

    public Integer getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(final Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getTaskCompleteDate() {
        return this.taskCompleteDate;
    }

    public void setTaskCompleteDate(final Date taskCompleteDate) {
        this.taskCompleteDate = taskCompleteDate;
    }

    public Date getTaskCompleteDateStart() {
        return this.taskCompleteDateStart;
    }

    public void setTaskCompleteDateStart(final Date taskCompleteDateStart) {
        this.taskCompleteDateStart = taskCompleteDateStart;
    }

    public Date getTaskCompleteDateEnd() {
        return this.taskCompleteDateEnd;
    }

    public void setTaskCompleteDateEnd(final Date taskCompleteDateEnd) {
        this.taskCompleteDateEnd = taskCompleteDateEnd;
    }

    public String getOfficeIds() {
        return this.officeIds;
    }

    public void setOfficeIds(final String officeIds) {
        this.officeIds = officeIds;
    }

    public String getOfficeName() {
        return this.officeName;
    }

    public void setOfficeName(final String officeName) {
        this.officeName = officeName;
    }

    public List<String> getSystemIds() {
        return this.systemIds;
    }

    public void setSystemIds(final List<String> systemIds) {
        this.systemIds = systemIds;
    }

    public String getProcInstId() {
        return this.procInstId;
    }

    public void setProcInstId(final String procInstId) {
        this.procInstId = procInstId;
    }

    public String getWorkItemId() {
        return this.workItemId;
    }

    public void setWorkItemId(final String workItemId) {
        this.workItemId = workItemId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }

    public Integer getCoreFlag() {
        return this.coreFlag;
    }

    public void setCoreFlag(final Integer coreFlag) {
        this.coreFlag = coreFlag;
    }
}
