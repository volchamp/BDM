package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import org.hibernate.validator.constraints.*;

public class CodeSet extends DataEntity<CodeSet>
{
    private static final long serialVersionUID = 1L;
    private String codeSetNo;
    private String codeSetName;
    private String codeGroupId;
    private String ruleDesc;
    private String formatDesc;
    private Integer codeSetSort;
    private Integer codeSetType;
    private String qsSql;
    private String codeTable;
    private String officeIds;
    private String procInstId;
    private Integer coreFlag;
    private Integer state;
    private Integer operation;
    private Integer processStatus;
    private DataSetCategory dataSetCategory;
    private String itemCode;
    private String itemName;
    private String officeName;
    private String areaName;
    private String areaType;

    public CodeSet() {
    }

    public CodeSet(final String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 50 \u4e4b\u95f4")
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

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Integer getCodeSetType() {
        return this.codeSetType;
    }

    public void setCodeSetType(final Integer codeSetType) {
        this.codeSetType = codeSetType;
    }

    @Length(min = 0, max = 300, message = "qs_sql\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 300 \u4e4b\u95f4")
    public String getQsSql() {
        return this.qsSql;
    }

    public void setQsSql(final String qsSql) {
        this.qsSql = qsSql;
    }

    @Length(min = 0, max = 50, message = "code_table\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeTable() {
        return this.codeTable;
    }

    public void setCodeTable(final String codeTable) {
        this.codeTable = codeTable;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(final Integer state) {
        this.state = state;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public Integer getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(final Integer processStatus) {
        this.processStatus = processStatus;
    }

    public DataSetCategory getDataSetCategory() {
        return this.dataSetCategory;
    }

    public void setDataSetCategory(final DataSetCategory dataSetCategory) {
        this.dataSetCategory = dataSetCategory;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
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

    public Integer getCoreFlag() {
        return this.coreFlag;
    }

    public void setCoreFlag(final Integer coreFlag) {
        this.coreFlag = coreFlag;
    }

    public String getAreaType() {
        return this.areaType;
    }

    public void setAreaType(final String areaType) {
        this.areaType = areaType;
    }
}
