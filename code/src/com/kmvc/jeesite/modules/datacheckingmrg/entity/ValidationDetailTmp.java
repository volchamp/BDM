package com.kmvc.jeesite.modules.datacheckingmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class ValidationDetailTmp extends DataEntity<ValidationDetailTmp>
{
    private static final long serialVersionUID = 1L;
    private String itemCode;
    private String parentItemCode;
    private String itemName;
    private Integer year;
    private Date validStartTime;
    private Date validEndTime;
    private String recordId;

    public ValidationDetailTmp() {
    }

    public ValidationDetailTmp(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u6570\u636e\u9879\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    public String getParentItemCode() {
        return this.parentItemCode;
    }

    public void setParentItemCode(final String parentItemCode) {
        this.parentItemCode = parentItemCode;
    }

    @Length(min = 0, max = 100, message = "\u4ee3\u7801\u96c6\u6570\u636e\u9879\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getValidStartTime() {
        return this.validStartTime;
    }

    public void setValidStartTime(final Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getValidEndTime() {
        return this.validEndTime;
    }

    public void setValidEndTime(final Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    @Length(min = 0, max = 64, message = "\u68c0\u6838\u76ee\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }
}
