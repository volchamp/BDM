package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class PendingCodeItem extends DataEntity<PendingCodeItem>
{
    public static final int STATUS_NOT = 0;
    public static final int STATUS_STOP = 1;
    public static final int STATUS_START = 2;
    private static final long serialVersionUID = 1L;
    private String itemCode;
    private String parentItemCode;
    private String itemName;
    private Integer year;
    private Date validStartDate;
    private Date validEndDate;
    private Integer status;
    private Integer version;
    private Integer itemCodeSort;
    private Integer operation;
    private String codeSetId;
    private String itemId;
    private Integer processStatus;
    private String col1;
    private String col2;
    private String col3;
    private Date col4;
    private Date col5;
    private List<String> systemIds;
    private String codeSetNo;
    private String distribution;
    private CodeSetData codeSetData;
    private Integer codeSetProcessStatus;
    private Integer codeSetOperation;
    private List<String> selectedRow;
    private String splitMerge;
    private String smType;

    public PendingCodeItem() {
        this.status = 2;
    }

    public PendingCodeItem(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u9879\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
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

    @Length(min = 0, max = 256, message = "\u4ee3\u7801\u9879\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 256 \u4e4b\u95f4")
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

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public Integer getItemCodeSort() {
        return this.itemCodeSort;
    }

    public void setItemCodeSort(final Integer itemCodeSort) {
        this.itemCodeSort = itemCodeSort;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public String getCodeSetId() {
        return this.codeSetId;
    }

    public void setCodeSetId(final String codeSetId) {
        this.codeSetId = codeSetId;
    }

    @Length(min = 0, max = 64, message = "\u6b63\u5f0f\u4ee3\u7801\u96c6ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public Integer getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(final Integer processStatus) {
        this.processStatus = processStatus;
    }

    @Length(min = 0, max = 100, message = "col1\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getCol1() {
        return this.col1;
    }

    public void setCol1(final String col1) {
        this.col1 = col1;
    }

    @Length(min = 0, max = 50, message = "col2\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCol2() {
        return this.col2;
    }

    public void setCol2(final String col2) {
        this.col2 = col2;
    }

    @Length(min = 0, max = 50, message = "col3\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
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

    public List<String> getSystemIds() {
        return this.systemIds;
    }

    public void setSystemIds(final List<String> systemIds) {
        this.systemIds = systemIds;
    }

    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    public String getDistribution() {
        return this.distribution;
    }

    public void setDistribution(final String distribution) {
        this.distribution = distribution;
    }

    public CodeSetData getCodeSetData() {
        return this.codeSetData;
    }

    public void setCodeSetData(final CodeSetData codeSetData) {
        this.codeSetData = codeSetData;
    }

    public Integer getCodeSetProcessStatus() {
        return this.codeSetProcessStatus;
    }

    public void setCodeSetProcessStatus(final Integer codeSetProcessStatus) {
        this.codeSetProcessStatus = codeSetProcessStatus;
    }

    public Integer getCodeSetOperation() {
        return this.codeSetOperation;
    }

    public void setCodeSetOperation(final Integer codeSetOperation) {
        this.codeSetOperation = codeSetOperation;
    }

    public List<String> getSelectedRow() {
        return this.selectedRow;
    }

    public void setSelectedRow(final List<String> selectedRow) {
        this.selectedRow = selectedRow;
    }

    public String getSplitMerge() {
        return this.splitMerge;
    }

    public void setSplitMerge(final String splitMerge) {
        this.splitMerge = splitMerge;
    }

    public String getSmType() {
        return this.smType;
    }

    public void setSmType(final String smType) {
        this.smType = smType;
    }
}
