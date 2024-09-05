package com.kmvc.jeesite.modules.connector.vo.client.mascodeset;

import java.util.*;

public class PushCodeSetReqBody
{
    private String sysCode;
    private String sysName;
    private String sysId;
    private String codeSetNo;
    private String codeSetName;
    private String description;
    private Integer operation;
    private List<PushCodeSetData> codeEntities;
    private String districtCode;
    private String districtName;

    public List<PushCodeSetData> getCodeEntities() {
        return this.codeEntities;
    }

    public void setCodeEntities(final List<PushCodeSetData> codeEntities) {
        this.codeEntities = codeEntities;
    }

    public String getSysCode() {
        return this.sysCode;
    }

    public void setSysCode(final String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysName(final String sysName) {
        this.sysName = sysName;
    }

    public String getSysId() {
        return this.sysId;
    }

    public void setSysId(final String sysId) {
        this.sysId = sysId;
    }

    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public Integer getOperation() {
        return this.operation;
    }

    public void setOperation(final Integer operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDistrictCode() {
        return this.districtCode;
    }

    public void setDistrictCode(final String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }
}
