package com.kmvc.jeesite.modules.datacheckingmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class ValidationCodeSet extends DataEntity<ValidationCodeSet>
{
    private static final long serialVersionUID = 1L;
    private String systemCode;
    private String systemName;
    private String codeSetNo;
    private String codeSetName;
    private String exceptionDesc;
    private Date handleDate;
    private Long handleStatus;
    private String handleBy;
    private Date checkDate;
    private Long checkResult;
    private String systemId;
    private String codeSetId;
    private String officeIds;
    private String taskHandlerId;
    private Date beginCheckDate;
    private Date endCheckDate;
    private String areaName;

    public ValidationCodeSet() {
    }

    public ValidationCodeSet(final String id) {
        super(id);
    }

    public Date getBeginCheckDate() {
        return this.beginCheckDate;
    }

    public void setBeginCheckDate(final Date beginCheckDate) {
        this.beginCheckDate = beginCheckDate;
    }

    public Date getEndCheckDate() {
        return this.endCheckDate;
    }

    public void setEndCheckDate(final Date endCheckDate) {
        this.endCheckDate = endCheckDate;
    }

    @Length(min = 0, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getSystemCode() {
        return this.systemCode;
    }

    public void setSystemCode(final String systemCode) {
        this.systemCode = systemCode;
    }

    @Length(min = 0, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }

    @Length(min = 0, max = 100, message = "\u6838\u67e5\u7ed3\u679c\u8bf4\u660e\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getExceptionDesc() {
        return this.exceptionDesc;
    }

    public void setExceptionDesc(final String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHandleDate() {
        return this.handleDate;
    }

    public void setHandleDate(final Date handleDate) {
        this.handleDate = handleDate;
    }

    public Long getHandleStatus() {
        return this.handleStatus;
    }

    public void setHandleStatus(final Long handleStatus) {
        this.handleStatus = handleStatus;
    }

    @Length(min = 0, max = 64, message = "\u5904\u7406\u4eba\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getHandleBy() {
        return this.handleBy;
    }

    public void setHandleBy(final String handleBy) {
        this.handleBy = handleBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(final Date checkDate) {
        this.checkDate = checkDate;
    }

    @Length(min = 0, max = 1, message = "\u6838\u67e5\u7ed3\u679c\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 1 \u4e4b\u95f4")
    public Long getCheckResult() {
        return this.checkResult;
    }

    public void setCheckResult(final Long checkResult) {
        this.checkResult = checkResult;
    }

    @Length(min = 0, max = 64, message = "system_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    @Length(min = 0, max = 64, message = "code_set_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getCodeSetId() {
        return this.codeSetId;
    }

    public void setCodeSetId(final String codeSetId) {
        this.codeSetId = codeSetId;
    }

    public String getTaskHandlerId() {
        return this.taskHandlerId;
    }

    public void setTaskHandlerId(final String taskHandlerId) {
        this.taskHandlerId = taskHandlerId;
    }

    public String getOfficeIds() {
        return this.officeIds;
    }

    public void setOfficeIds(final String officeIds) {
        this.officeIds = officeIds;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }
}
