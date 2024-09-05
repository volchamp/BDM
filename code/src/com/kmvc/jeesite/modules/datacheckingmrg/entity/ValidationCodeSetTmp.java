package com.kmvc.jeesite.modules.datacheckingmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class ValidationCodeSetTmp extends DataEntity<ValidationCodeSetTmp>
{
    private static final long serialVersionUID = 1L;
    private String systemCode;
    private String systemName;
    private String codeSetNo;
    private String codeSetName;
    private Integer status;
    private Date checkDate;
    private Integer finished;
    private Date finishDate;
    private Date receiveDate;

    public ValidationCodeSetTmp() {
    }

    public ValidationCodeSetTmp(final String id) {
        super(id);
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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(final Date checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getFinished() {
        return this.finished;
    }

    public void setFinished(final Integer finished) {
        this.finished = finished;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(final Date finishDate) {
        this.finishDate = finishDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getReceiveDate() {
        return this.receiveDate;
    }

    public void setReceiveDate(final Date receiveDate) {
        this.receiveDate = receiveDate;
    }
}
