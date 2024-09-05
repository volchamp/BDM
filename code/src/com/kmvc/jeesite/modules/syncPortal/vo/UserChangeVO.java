package com.kmvc.jeesite.modules.syncPortal.vo;

import org.codehaus.jackson.annotate.*;

public class UserChangeVO
{
    public static final String ADD = "add";
    public static final String MODIFY = "modify";
    public static final String DELETE = "delete";
    @JsonProperty("ChangeNumber")
    private String changeNumber;
    @JsonProperty("changeTime")
    private String changeTime;
    @JsonProperty("changeType")
    private String changeType;
    @JsonProperty("Uid")
    private String uid;

    public String getChangeNumber() {
        return this.changeNumber;
    }

    public void setChangeNumber(final String changeNumber) {
        this.changeNumber = changeNumber;
    }

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
