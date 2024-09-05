package com.kmvc.jeesite.modules.connector.vo.client.olcodeset;

import java.util.*;

public class CollectCodeSetData
{
    private String code;
    private String parentCode;
    private String name;
    private String year;
    private Date validStartDate;
    private Date validEndDate;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(final String parentCode) {
        this.parentCode = parentCode;
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
}
