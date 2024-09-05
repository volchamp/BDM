package com.kmvc.jeesite.modules.syncPortal.vo;

import org.codehaus.jackson.annotate.*;

public class UserOrgPostRelationVO
{
    @JsonProperty("UserCode")
    private String userCode;
    private String orgCode;
    private String orgInnerCode;
    private String orgName;
    private String positionCode;
    private String positionName;
    private String dutyCode;
    private String positionStatus;
    private String displayOrder;

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(final String userCode) {
        this.userCode = userCode;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(final String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgInnerCode() {
        return this.orgInnerCode;
    }

    public void setOrgInnerCode(final String orgInnerCode) {
        this.orgInnerCode = orgInnerCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }

    public String getPositionCode() {
        return this.positionCode;
    }

    public void setPositionCode(final String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public void setPositionName(final String positionName) {
        this.positionName = positionName;
    }

    public String getDutyCode() {
        return this.dutyCode;
    }

    public void setDutyCode(final String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public String getPositionStatus() {
        return this.positionStatus;
    }

    public void setPositionStatus(final String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public String getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(final String displayOrder) {
        this.displayOrder = displayOrder;
    }
}
