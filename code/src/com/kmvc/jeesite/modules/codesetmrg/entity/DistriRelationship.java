package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;

public class DistriRelationship extends DataEntity<DistriRelationship>
{
    private static final long serialVersionUID = 1L;
    private String systemId;
    private String itemId;

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(final String systemId) {
        this.systemId = systemId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }
}
