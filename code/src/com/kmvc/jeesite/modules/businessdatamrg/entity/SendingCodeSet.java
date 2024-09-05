package com.kmvc.jeesite.modules.businessdatamrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class SendingCodeSet extends DataEntity<SendingCodeSet>
{
    private static final long serialVersionUID = 1L;
    private String destSysId;
    private String destSysCode;
    private String destSysName;
    private String codeSetId;
    private String codeSetNo;
    private String codeSetName;
    private Date sendDate;
    private Integer sendStatus;
    private Integer codeSetSort;
    private Integer operation;
    private String officeIds;
    private Date sendDateStart;
    private Date sendDateEnd;
    private String taskHandlerId;
    private Integer taskStatus;
    private Date taskCompleteDate;
    private Date taskCompleteDateStart;
    private Date taskCompleteDateEnd;
    private String officeName;
    private String areaName;

    public SendingCodeSet() {
    }

    public SendingCodeSet(final String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "\u76ee\u7684\u4e1a\u52a1\u7cfb\u7edfID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getDestSysId() {
        return this.destSysId;
    }

    public void setDestSysId(final String destSysId) {
        this.destSysId = destSysId;
    }

    @Length(min = 0, max = 50, message = "\u76ee\u7684\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getDestSysCode() {
        return this.destSysCode;
    }

    public void setDestSysCode(final String destSysCode) {
        this.destSysCode = destSysCode;
    }

    @Length(min = 0, max = 50, message = "\u76ee\u7684\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getDestSysName() {
        return this.destSysName;
    }

    public void setDestSysName(final String destSysName) {
        this.destSysName = destSysName;
    }

    @Length(min = 0, max = 64, message = "\u4ee3\u7801\u96c6\u76ee\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getCodeSetId() {
        return this.codeSetId;
    }

    public void setCodeSetId(final String codeSetId) {
        this.codeSetId = codeSetId;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getSendDate() {
        return this.sendDate;
    }

    public void setSendDate(final Date sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(final Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getCodeSetSort() {
        return this.codeSetSort;
    }

    public void setCodeSetSort(final Integer codeSetSort) {
        this.codeSetSort = codeSetSort;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public Date getSendDateStart() {
        return this.sendDateStart;
    }

    public void setSendDateStart(final Date sendDateStart) {
        this.sendDateStart = sendDateStart;
    }

    public Date getSendDateEnd() {
        return this.sendDateEnd;
    }

    public void setSendDateEnd(final Date sendDateEnd) {
        this.sendDateEnd = sendDateEnd;
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

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }
}
