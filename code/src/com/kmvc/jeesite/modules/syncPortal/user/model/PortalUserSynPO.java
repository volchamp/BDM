package com.kmvc.jeesite.modules.syncPortal.user.model;

import java.io.*;
import org.apache.shiro.authc.*;
import java.util.*;
import javax.management.relation.*;

public class PortalUserSynPO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer synId;
    private String porLoginName;
    private String sysLoginName;
    private Account user;
    private List<Role> roles;

    public Integer getSynId() {
        return this.synId;
    }

    public void setSynId(final Integer synId) {
        this.synId = synId;
    }

    public String getPorLoginName() {
        return this.porLoginName;
    }

    public void setPorLoginName(final String porLoginName) {
        this.porLoginName = porLoginName;
    }

    public String getSysLoginName() {
        return this.sysLoginName;
    }

    public void setSysLoginName(final String sysLoginName) {
        this.sysLoginName = sysLoginName;
    }

    public Account getUser() {
        return this.user;
    }

    public void setUser(final Account user) {
        this.user = user;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }
}
