package com.kmvc.jeesite.modules.syncPortal.user.model;

import org.codehaus.jackson.annotate.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalOrgPO extends PortalPO
{
    private static final long serialVersionUID = 1L;
    private String orgId;
    private String orgCode;
    private String orgInnerCode;
    private String orgName;
    private String orgLevel;
    private Integer orgType;
    private String orgParentId;
    private String orgParentName;
    private String orgDescription;
    private String changeType;

    public PortalOrgPO() {
    }

    public PortalOrgPO(final String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(final String orgId) {
        this.orgId = orgId;
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

    public String getOrgLevel() {
        return this.orgLevel;
    }

    public void setOrgLevel(final String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Integer getOrgType() {
        return this.orgType;
    }

    public void setOrgType(final Integer orgType) {
        this.orgType = orgType;
    }

    public String getOrgParentId() {
        return this.orgParentId;
    }

    public void setOrgParentId(final String orgParentId) {
        this.orgParentId = orgParentId;
    }

    public String getOrgParentName() {
        return this.orgParentName;
    }

    public void setOrgParentName(final String orgParentName) {
        this.orgParentName = orgParentName;
    }

    public String getOrgDescription() {
        return this.orgDescription;
    }

    public void setOrgDescription(final String orgDescription) {
        this.orgDescription = orgDescription;
    }

    public String getChangeType() {
        return this.changeType;
    }

    public void setChangeType(final String changeType) {
        this.changeType = changeType;
    }
}
