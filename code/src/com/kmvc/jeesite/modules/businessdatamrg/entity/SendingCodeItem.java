package com.kmvc.jeesite.modules.businessdatamrg.entity;

import java.util.*;

import com.kmvc.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class SendingCodeItem extends DataEntity<SendingCodeItem>
{
    private static final long serialVersionUID = 1L;
    private String recordId;
    private String itemCode;
    private String parentItemCode;
    private String itemName;
    private Integer year;
    private Date validStartDate;
    private Date validEndDate;
    private Integer status;
    private Integer sendStatus;
    private Integer operation;
    private Integer itemCodeSort;
    private String col1;
    private String col2;
    private String col3;
    private Date col4;
    private Date col5;
    private String sendStatusStr;

    public String getSendStatusStr() {
        return this.sendStatusStr;
    }

    public void setSendStatusStr(final String sendStatusStr) {
        this.sendStatusStr = sendStatusStr;
    }

    public SendingCodeItem() {
    }

    public SendingCodeItem(final String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "\u5f85\u53d1\u9001\u8bb0\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    @Length(min = 0, max = 50, message = "\u4e0a\u7ea7\u4ee3\u7801\u96c6\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getParentItemCode() {
        return this.parentItemCode;
    }

    public void setParentItemCode(final String parentItemCode) {
        this.parentItemCode = parentItemCode;
    }

    @Length(min = 0, max = 100, message = "\u4ee3\u7801\u96c6\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
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
    public Date getValidStartDate() {
        return this.validStartDate;
    }

    public void setValidStartDate(final Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getValidEndDate() {
        return this.validEndDate;
    }

    public void setValidEndDate(final Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Integer getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(final Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public Integer getItemCodeSort() {
        return this.itemCodeSort;
    }

    public void setItemCodeSort(final Integer itemCodeSort) {
        this.itemCodeSort = itemCodeSort;
    }

    @Length(min = 0, max = 100, message = "col1\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getCol1() {
        return this.col1;
    }

    public void setCol1(final String col1) {
        this.col1 = col1;
    }

    @Length(min = 0, max = 100, message = "col2\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getCol2() {
        return this.col2;
    }

    public void setCol2(final String col2) {
        this.col2 = col2;
    }

    @Length(min = 0, max = 100, message = "col3\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getCol3() {
        return this.col3;
    }

    public void setCol3(final String col3) {
        this.col3 = col3;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCol4() {
        return this.col4;
    }

    public void setCol4(final Date col4) {
        this.col4 = col4;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCol5() {
        return this.col5;
    }

    public void setCol5(final Date col5) {
        this.col5 = col5;
    }
}
