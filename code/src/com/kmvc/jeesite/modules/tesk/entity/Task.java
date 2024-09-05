package com.kmvc.jeesite.modules.tesk.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class Task extends DataEntity<Task>
{
    private static final long serialVersionUID = 1L;
    private String taskHandlerId;
    private Date taskStartDate;
    private Date taskCompleteDate;
    private Integer taskStatus;
    private String systemName;
    private String codeSetName;
    private Integer taskTypeId;
    private String taskLink;
    private String pendingRecordId;
    private String dataExceptionId;
    private String sendingRecordId;
    private String systemId;
    private String taskType;
    private List<Integer> taskTypeList;
    private String taskHandlerName;
    private Date taskStartDateStart;
    private Date taskStartDateEnd;
    private Date taskCompleteDateStart;
    private Date taskCompleteDateEnd;
    private List<String> systemIdList;
    private String procInstId;
    private String areaName;

    public Task() {
        this.systemId = " ";
        this.taskTypeList = new ArrayList<Integer>();
        this.systemIdList = new ArrayList<String>();
    }

    public Task(final String id) {
        super(id);
        this.systemId = " ";
        this.taskTypeList = new ArrayList<Integer>();
        this.systemIdList = new ArrayList<String>();
    }

    @Length(min = 0, max = 64, message = "\u4efb\u52a1\u5904\u7406\u8005ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getTaskHandlerId() {
        return this.taskHandlerId;
    }

    public void setTaskHandlerId(final String taskHandlerId) {
        this.taskHandlerId = taskHandlerId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getTaskStartDate() {
        return this.taskStartDate;
    }

    public void setTaskStartDate(final Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getTaskCompleteDate() {
        return this.taskCompleteDate;
    }

    public void setTaskCompleteDate(final Date taskCompleteDate) {
        this.taskCompleteDate = taskCompleteDate;
    }

    public Integer getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(final Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Length(min = 0, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public Integer getTaskTypeId() {
        return this.taskTypeId;
    }

    public void setTaskTypeId(final Integer taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    @Length(min = 0, max = 100, message = "\u4efb\u52a1\u94fe\u63a5\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getTaskLink() {
        return this.taskLink;
    }

    public void setTaskLink(final String taskLink) {
        this.taskLink = taskLink;
    }

    @Length(min = 0, max = 64, message = "\u5f85\u5904\u7406\u8bb0\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getPendingRecordId() {
        return this.pendingRecordId;
    }

    public void setPendingRecordId(final String pendingRecordId) {
        this.pendingRecordId = pendingRecordId;
    }

    @Length(min = 0, max = 64, message = "\u6570\u636e\u6838\u67e5\u8bb0\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getDataExceptionId() {
        return this.dataExceptionId;
    }

    public void setDataExceptionId(final String dataExceptionId) {
        this.dataExceptionId = dataExceptionId;
    }

    @Length(min = 0, max = 64, message = "\u5f85\u53d1\u9001\u8bb0\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getSendingRecordId() {
        return this.sendingRecordId;
    }

    public void setSendingRecordId(final String sendingRecordId) {
        this.sendingRecordId = sendingRecordId;
    }

    public String getTaskType() {
        return this.taskType;
    }

    public void setTaskType(final String taskType) {
        this.taskType = taskType;
    }

    public String getTaskHandlerName() {
        return this.taskHandlerName;
    }

    public void setTaskHandlerName(final String taskHandlerName) {
        this.taskHandlerName = taskHandlerName;
    }

    public Date getTaskStartDateStart() {
        return this.taskStartDateStart;
    }

    public void setTaskStartDateStart(final Date taskStartDateStart) {
        this.taskStartDateStart = taskStartDateStart;
    }

    public Date getTaskStartDateEnd() {
        return this.taskStartDateEnd;
    }

    public void setTaskStartDateEnd(final Date taskStartDateEnd) {
        this.taskStartDateEnd = taskStartDateEnd;
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

    public List<Integer> getTaskTypeList() {
        return this.taskTypeList;
    }

    public void setTaskTypeList(final List<Integer> taskTypeList) {
        this.taskTypeList = taskTypeList;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    public List<String> getSystemIdList() {
        return this.systemIdList;
    }

    public void setSystemIdList(final List<String> systemIdList) {
        this.systemIdList = systemIdList;
    }

    public String getProcInstId() {
        return this.procInstId;
    }

    public void setProcInstId(final String procInstId) {
        this.procInstId = procInstId;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }
}
