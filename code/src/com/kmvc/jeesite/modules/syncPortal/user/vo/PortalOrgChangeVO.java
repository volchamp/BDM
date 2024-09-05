package com.kmvc.jeesite.modules.syncPortal.user.vo;

public class PortalOrgChangeVO
{
    private String changeTime;
    private String changeType;
    private String orgId;

    public String getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(final String changeTime) {
        this.changeTime = changeTime;
    }

    public String getChangeType() {
        return this.changeType;
    }

    public void setChangeType(final String changeType) {
        this.changeType = changeType;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(final String orgId) {
        this.orgId = orgId;
    }
}
