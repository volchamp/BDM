package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import org.hibernate.validator.constraints.*;

public class PendingCodesetSystem extends DataEntity<PendingCodesetSystem>
{
    private static final long serialVersionUID = 1L;
    private String systemId;
    private String pendingCodesetId;

    public PendingCodesetSystem() {
    }

    public PendingCodesetSystem(final String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "system_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 64 \u4e4b\u95f4")
    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    @Length(min = 1, max = 64, message = "pending_codeset_id\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 64 \u4e4b\u95f4")
    public String getPendingCodesetId() {
        return this.pendingCodesetId;
    }

    public void setPendingCodesetId(final String pendingCodesetId) {
        this.pendingCodesetId = pendingCodesetId;
    }
}
