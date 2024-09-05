package com.kmvc.jeesite.modules.mdmlog.entity;

import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class ProcLog extends DataEntity<ProcLog>
{
    private static final long serialVersionUID = 1L;
    private String procName;
    private String procReason;
    private String procSql;
    private Date procTime;
    private Date beginProcTime;
    private Date endProcTime;

    public ProcLog() {
    }

    public ProcLog(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "proc_name\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getProcName() {
        return this.procName;
    }

    public void setProcName(final String procName) {
        this.procName = procName;
    }

    @Length(min = 0, max = 1000, message = "proc_reason\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 1000 \u4e4b\u95f4")
    public String getProcReason() {
        return this.procReason;
    }

    public void setProcReason(final String procReason) {
        this.procReason = procReason;
    }

    @Length(min = 0, max = 2000, message = "proc_sql\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 2000 \u4e4b\u95f4")
    public String getProcSql() {
        return this.procSql;
    }

    public void setProcSql(final String procSql) {
        this.procSql = procSql;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getProcTime() {
        return this.procTime;
    }

    public void setProcTime(final Date procTime) {
        this.procTime = procTime;
    }

    public Date getBeginProcTime() {
        return this.beginProcTime;
    }

    public void setBeginProcTime(final Date beginProcTime) {
        this.beginProcTime = beginProcTime;
    }

    public Date getEndProcTime() {
        return this.endProcTime;
    }

    public void setEndProcTime(final Date endProcTime) {
        this.endProcTime = endProcTime;
    }
}
