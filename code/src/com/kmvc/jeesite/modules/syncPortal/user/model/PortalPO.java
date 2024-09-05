package com.kmvc.jeesite.modules.syncPortal.user.model;

import java.io.*;
import java.util.*;

public class PortalPO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Date synPortalTime;
    private Integer synType;

    public Date getSynPortalTime() {
        return this.synPortalTime;
    }

    public void setSynPortalTime(final Date synPortalTime) {
        this.synPortalTime = synPortalTime;
    }

    public Integer getSynType() {
        return this.synType;
    }

    public void setSynType(final Integer synType) {
        this.synType = synType;
    }
}
