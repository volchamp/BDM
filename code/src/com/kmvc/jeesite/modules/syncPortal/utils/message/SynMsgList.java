package com.kmvc.jeesite.modules.syncPortal.utils.message;

import java.util.*;

public class SynMsgList extends ArrayList<SynMsg>
{
    private static final long serialVersionUID = -3142114851450596586L;
    private Date time;

    public SynMsgList() {
        this.time = new Date();
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(final Date time) {
        this.time = time;
    }
}
