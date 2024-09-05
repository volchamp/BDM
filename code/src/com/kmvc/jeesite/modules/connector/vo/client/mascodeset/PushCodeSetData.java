package com.kmvc.jeesite.modules.connector.vo.client.mascodeset;

import java.util.*;

public class PushCodeSetData
{
    private String code;
    private String name;
    private String year;
    private Date validStartDate;
    private Date validEndDate;
    private String parentCode;
    private Integer operation;
    private String innerCode;
    private String oldRelation;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public Date getValidStartDate() {
        return this.validStartDate;
    }

    public void setValidStartDate(final Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    public Date getValidEndDate() {
        return this.validEndDate;
    }

    public void setValidEndDate(final Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(final String parentCode) {
        this.parentCode = parentCode;
    }

    public String getInnerCode() {
        return this.innerCode;
    }

    public void setInnerCode(final String innerCode) {
        this.innerCode = innerCode;
    }

    public String getOldRelation() {
        return this.oldRelation;
    }

    public void setOldRelation(final String oldRelation) {
        this.oldRelation = oldRelation;
    }
}
