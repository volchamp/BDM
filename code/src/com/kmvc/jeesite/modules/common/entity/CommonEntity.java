package com.kmvc.jeesite.modules.common.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class CommonEntity extends TreeEntity<CommonEntity>
{
    private static final long serialVersionUID = 1L;
    private Date pswUpdateDate;
    private Integer illegalLgCount;
    private Date loginDate;
    private String loginName;
    private String loginFlag;
    private Date lgRecordTime;

    public CommonEntity() {
    }

    public CommonEntity(final String id) {
        super(id);
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPswUpdateDate() {
        return this.pswUpdateDate;
    }

    public void setPswUpdateDate(final Date pswUpdateDate) {
        this.pswUpdateDate = pswUpdateDate;
    }

    public Integer getIllegalLgCount() {
        return this.illegalLgCount;
    }

    public void setIllegalLgCount(final Integer illegalLgCount) {
        this.illegalLgCount = illegalLgCount;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(final Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(final String loginName) {
        this.loginName = loginName;
    }

    public String getLoginFlag() {
        return this.loginFlag;
    }

    public void setLoginFlag(final String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public Date getLgRecordTime() {
        return this.lgRecordTime;
    }

    public void setLgRecordTime(final Date lgRecordTime) {
        this.lgRecordTime = lgRecordTime;
    }

    public CommonEntity getParent() {
        return (CommonEntity)this.parent;
    }

    public void setParent(final CommonEntity parent) {
        this.parent = parent;
    }
}
