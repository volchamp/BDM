package com.kmvc.jeesite.modules.connector.vo.serve.getcodeset;

import java.util.*;

public class CollectCodeSetData
{
    private String code;
    private String parenCode;
    private String name;
    private String year;
    private Date validStartDate;
    private Date validEndDate;
    private String innerCode;
    private String oldRelation;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getParenCode() {
        return this.parenCode;
    }

    public void setParenCode(final String parenCode) {
        this.parenCode = parenCode;
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
