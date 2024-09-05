package com.kmvc.jeesite.common.persistence;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

public abstract class DataEntity<T> extends BaseEntity<T> {
    private static final long serialVersionUID = 1L;
    protected String remarks;
    protected User createBy;
    protected Date createDate;
    protected User updateBy;
    protected Date updateDate;
    protected String delFlag;

    public DataEntity() {
        this.delFlag = "0";
    }

    public DataEntity(String id) {
        super(id);
    }

    public void preInsert() {
        if (!this.isNewRecord) {
            this.setId(IdGen.uuid());
        }

        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
            this.createBy = user;
        }

        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    public void preUpdate() {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user;
        }

        this.updateDate = new Date();
    }

    @Length(
            min = 0,
            max = 255
    )
    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonIgnore
    public User getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    public User getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @JsonIgnore
    @Length(
            min = 1,
            max = 1
    )
    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
