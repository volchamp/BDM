package com.kmvc.jeesite.common.persistence;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.SupTreeList;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.io.Serializable;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@SupTreeList
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected User currentUser;
    protected Page<T> page;
    protected Map<String, String> sqlMap;
    protected boolean isNewRecord;
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

    public BaseEntity() {
        this.isNewRecord = false;
    }

    public BaseEntity(String id) {
        this();
        this.id = id;
    }

    @SupCol(
            isUnique = "true",
            isHide = "true"
    )
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    @XmlTransient
    public User getCurrentUser() {
        if (this.currentUser == null) {
            this.currentUser = UserUtils.getUser();
        }

        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @JsonIgnore
    @XmlTransient
    public Page<T> getPage() {
        if (this.page == null) {
            this.page = new Page();
        }

        return this.page;
    }

    public Page<T> setPage(Page<T> page) {
        this.page = page;
        return page;
    }

    @JsonIgnore
    @XmlTransient
    public Map<String, String> getSqlMap() {
        if (this.sqlMap == null) {
            this.sqlMap = Maps.newHashMap();
        }

        return this.sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    public abstract void preInsert();

    public abstract void preUpdate();

    public boolean getIsNewRecord() {
        return this.isNewRecord || StringUtils.isBlank(this.getId());
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    @JsonIgnore
    public Global getGlobal() {
        return Global.getInstance();
    }

    @JsonIgnore
    public String getDbName() {
        return Global.getConfig("jdbc.type");
    }

    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(obj.getClass())) {
            return false;
        } else {
            com.thinkgem.jeesite.common.persistence.BaseEntity<?> that = (com.thinkgem.jeesite.common.persistence.BaseEntity)obj;
            return null == this.getId() ? false : this.getId().equals(that.getId());
        }
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}