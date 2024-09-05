package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.thinkgem.jeesite.common.utils.excel.annotation.*;
import com.fasterxml.jackson.annotation.*;

public class CodeSetData extends DataEntity<CodeSetData>
{
    private static final long serialVersionUID = 1L;
    private String itemCode;
    private String parentItemCode;
    private String itemName;
    private Integer version;
    private Integer year;
    private Date validStartDate;
    private Date validEndDate;
    private Integer status;
    private String codeSetId;
    private Integer itemCodeSort;
    private String col1;
    private String col2;
    private String col3;
    private Date col4;
    private Date col5;
    private List<String> systemIds;
    private String codeSetNo;
    private Integer operation;
    private List<String> selectedRow;
    private String distribution;
    private Integer processStatus;
    private String splitMerge;

    public CodeSetData() {
    }

    public CodeSetData(final String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "\u6570\u636e\u9879\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1\u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u4ee3\u7801\u96c6\u7f16\u7801", align = 1, sort = 1)
    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    @Length(min = 0, max = 50, message = "\u4e0a\u7ea7\u4ee3\u7801\u96c6\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u4e0a\u7ea7\u4ee3\u7801\u96c6\u7f16\u7801", align = 1, sort = 2)
    public String getParentItemCode() {
        return this.parentItemCode;
    }

    public void setParentItemCode(final String parentItemCode) {
        this.parentItemCode = parentItemCode;
    }

    @Length(min = 1, max = 256, message = "\u4ee3\u7801\u96c6\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 256 \u4e4b\u95f4")
    @ExcelField(title = "\u4ee3\u7801\u96c6\u540d\u79f0", align = 1, sort = 3)
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    @ExcelField(title = "\u7248\u672c", align = 1, sort = 4)
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    @ExcelField(title = "\u5e74\u5ea6", align = 1, sort = 5)
    public Integer getYear() {
        return this.year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "\u6709\u6548\u5f00\u59cb\u65e5\u671f", align = 1, sort = 6)
    public Date getValidStartDate() {
        return this.validStartDate;
    }

    public void setValidStartDate(final Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelField(title = "\u6709\u6548\u7ed3\u675f\u65e5\u671f", align = 1, sort = 7)
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

    @Length(min = 0, max = 64, message = "\u4ee3\u7801\u96c6\u76ee\u5f55ID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getCodeSetId() {
        return this.codeSetId;
    }

    public void setCodeSetId(final String codeSetId) {
        this.codeSetId = codeSetId;
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

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public List<String> getSelectedRow() {
        return this.selectedRow;
    }

    public void setSelectedRow(final List<String> selectedRow) {
        this.selectedRow = selectedRow;
    }

    public String getDistribution() {
        return this.distribution;
    }

    public void setDistribution(final String distribution) {
        this.distribution = distribution;
    }

    public Integer getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(final Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getSplitMerge() {
        return this.splitMerge;
    }

    public void setSplitMerge(final String splitMerge) {
        this.splitMerge = splitMerge;
    }
}
