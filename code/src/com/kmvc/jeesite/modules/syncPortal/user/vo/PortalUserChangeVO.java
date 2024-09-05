package com.kmvc.jeesite.modules.syncPortal.user.vo;

public class PortalUserChangeVO
{
    private String changeTime;
    private String changeType;
    private String uid;

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

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }
}
