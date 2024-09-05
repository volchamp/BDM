package com.kmvc.jeesite.modules.syncPortal.user.model;

import org.codehaus.jackson.annotate.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalRelationPO extends PortalPO
{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String uid;
    private String oid;
    private String orgCode;
    private String orgName;
    private Integer displayOrder;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(final String oid) {
        this.oid = oid;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(final String orgInnerCode) {
        this.orgCode = orgInnerCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(final String orgName) {
        this.orgName = orgName;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
