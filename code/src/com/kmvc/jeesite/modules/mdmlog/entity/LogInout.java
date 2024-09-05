package com.kmvc.jeesite.modules.mdmlog.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class LogInout extends DataEntity<LogInout>
{
    private static final long serialVersionUID = 1L;
    private String sourceSysName;
    private String destSysName;
    private String codeSetName;
    private Integer recordAmount;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private String failReason;
    private Integer recordType;
    private String messageId;
    private Integer currentPage;
    private Date startDateStart;
    private Date startDateEnd;
    private String areaName;

    public LogInout() {
    }

    public LogInout(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u6e90\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getSourceSysName() {
        return this.sourceSysName;
    }

    public void setSourceSysName(final String sourceSysName) {
        this.sourceSysName = sourceSysName;
    }

    @Length(min = 0, max = 50, message = "\u76ee\u7684\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getDestSysName() {
        return this.destSysName;
    }

    public void setDestSysName(final String destSysName) {
        this.destSysName = destSysName;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public Integer getRecordAmount() {
        return this.recordAmount;
    }

    public void setRecordAmount(final Integer recordAmount) {
        this.recordAmount = recordAmount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    @Length(min = 0, max = 4000, message = "\u5931\u8d25\u539f\u56e0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 4000 \u4e4b\u95f4")
    public String getFailReason() {
        return this.failReason;
    }

    public void setFailReason(final String failReason) {
        this.failReason = failReason;
    }

    public Integer getRecordType() {
        return this.recordType;
    }

    public void setRecordType(final Integer recordType) {
        this.recordType = recordType;
    }

    @Length(min = 0, max = 50, message = "\u6d88\u606fid\uff1a\u8c03\u7528\u63a5\u53e3\u65f6\u4ea7\u751f\u552f\u4e00\u6807\u8bc6\uff0c\u5982\u679c\u6709\u5206\u9875\u60c5\u51b5\uff0c\u6d88\u606fid\u4fdd\u6301\u4e00\u81f4\u3002\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(final Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Date getStartDateStart() {
        return this.startDateStart;
    }

    public void setStartDateStart(final Date startDateStart) {
        this.startDateStart = startDateStart;
    }

    public Date getStartDateEnd() {
        return this.startDateEnd;
    }

    public void setStartDateEnd(final Date startDateEnd) {
        this.startDateEnd = startDateEnd;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }
}
