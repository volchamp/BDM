package com.kmvc.jeesite.modules.datacheckingmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import com.thinkgem.jeesite.common.utils.excel.annotation.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class ValidationDetail extends DataEntity<ValidationDetail>
{
    private static final long serialVersionUID = 1L;
    private String itemCode;
    private String parentItemCode;
    private String itemName;
    private Integer exceptionType;
    private Integer year;
    private Date validStartDate;
    private Date validEndDate;
    private String recordId;
    private String itemCodeS;
    private String parentItemCodeS;
    private String itemNameS;
    private Integer yearS;
    private Date validStartDateS;
    private Date validEndDateS;
    private Integer num;

    @ExcelField(title = "\u5e8f\u53f7", type = 1, align = 2, sort = 10)
    public Integer getNum() {
        return this.num;
    }

    public void setNum(final Integer num) {
        this.num = num;
    }

    public ValidationDetail() {
    }

    public ValidationDetail(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u5f02\u5e38\u4ee3\u7801\u96c6\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u5f02\u5e38\u4ee3\u7801\u7f16\u7801", type = 1, align = 2, sort = 20)
    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    @ExcelField(title = "\u5f02\u5e38\u7236\u4ee3\u7801\u7f16\u7801", type = 1, align = 2, sort = 40)
    public String getParentItemCode() {
        return this.parentItemCode;
    }

    public void setParentItemCode(final String parentItemCode) {
        this.parentItemCode = parentItemCode;
    }

    @Length(min = 0, max = 100, message = "\u5f02\u5e38\u4ee3\u7801\u96c6\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    @ExcelField(title = "\u5f02\u5e38\u4ee3\u7801\u540d\u79f0", type = 1, align = 2, sort = 30)
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    @ExcelField(title = "\u5f02\u5e38\u7c7b\u578b(0:\u7f3a\u5931,1:\u5197\u4f59,2:\u4fee\u6539)", type = 1, align = 2, sort = 70)
    public Integer getExceptionType() {
        return this.exceptionType;
    }

    public void setExceptionType(final Integer exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "\u6709\u6548\u5f00\u59cb\u65f6\u95f4", type = 1, align = 2, sort = 50)
    public Date getValidStartDate() {
        return this.validStartDate;
    }

    public void setValidStartDate(final Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "\u6709\u6548\u7ed3\u675f\u65f6\u95f4", type = 1, align = 2, sort = 60)
    public Date getValidEndDate() {
        return this.validEndDate;
    }

    public void setValidEndDate(final Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    @Length(min = 0, max = 64, message = "\u6838\u67e5\u7ed3\u679cID\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    @Length(min = 0, max = 50, message = "\u6807\u51c6\u4ee3\u7801\u96c6\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u6807\u51c6\u4ee3\u7801\u7f16\u7801", type = 1, align = 2, sort = 80)
    public String getItemCodeS() {
        return this.itemCodeS;
    }

    public void setItemCodeS(final String itemCodeS) {
        this.itemCodeS = itemCodeS;
    }

    @ExcelField(title = "\u6807\u51c6\u7236\u4ee3\u7801\u7f16\u7801", type = 1, align = 2, sort = 100)
    public String getParentItemCodeS() {
        return this.parentItemCodeS;
    }

    public void setParentItemCodeS(final String parentItemCodeS) {
        this.parentItemCodeS = parentItemCodeS;
    }

    @Length(min = 0, max = 100, message = "\u6807\u51c6\u4ee3\u7801\u96c6\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    @ExcelField(title = "\u6807\u51c6\u4ee3\u7801\u540d\u79f0", type = 1, align = 2, sort = 90)
    public String getItemNameS() {
        return this.itemNameS;
    }

    public void setItemNameS(final String itemNameS) {
        this.itemNameS = itemNameS;
    }

    public Integer getYearS() {
        return this.yearS;
    }

    public void setYearS(final Integer yearS) {
        this.yearS = yearS;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "\u6709\u6548\u5f00\u59cb\u65f6\u95f4(\u6807\u51c6)", type = 1, align = 2, sort = 110)
    public Date getValidStartDateS() {
        return this.validStartDateS;
    }

    public void setValidStartDateS(final Date validStartDateS) {
        this.validStartDateS = validStartDateS;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "\u6709\u6548\u7ed3\u675f\u65f6\u95f4(\u6807\u51c6)", type = 1, align = 2, sort = 120)
    public Date getValidEndDateS() {
        return this.validEndDateS;
    }

    public void setValidEndDateS(final Date validEndDateS) {
        this.validEndDateS = validEndDateS;
    }
}
