package com.kmvc.jeesite.modules.syncPortal.process.vo;

public class ProcessVO
{
    private String type;
    private String systemCode;
    private String processType;
    private String sender;
    private String title;
    private String processCode;
    private String processName;
    private String taskname;
    private String href;
    private String potentialOwner;
    private String owner;
    private String priority;
    private String uniid;
    private String created;
    private String property;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getSystemCode() {
        return this.systemCode;
    }

    public void setSystemCode(final String systemCode) {
        this.systemCode = systemCode;
    }

    public String getProcessType() {
        return this.processType;
    }

    public void setProcessType(final String processType) {
        this.processType = processType;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getProcessCode() {
        return this.processCode;
    }

    public void setProcessCode(final String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return this.processName;
    }

    public void setProcessName(final String processName) {
        this.processName = processName;
    }

    public String getTaskname() {
        return this.taskname;
    }

    public void setTaskname(final String taskname) {
        this.taskname = taskname;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    public String getPotentialOwner() {
        return this.potentialOwner;
    }

    public void setPotentialOwner(final String potentialOwner) {
        this.potentialOwner = potentialOwner;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getUniid() {
        return this.uniid;
    }

    public void setUniid(final String uniid) {
        this.uniid = uniid;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(final String created) {
        this.created = created;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.uniid == null) ? 0 : this.uniid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ProcessVO other = (ProcessVO)obj;
        if (this.uniid == null) {
            if (other.uniid != null) {
                return false;
            }
        }
        else if (!this.uniid.equals(other.uniid)) {
            return false;
        }
        return true;
    }
}
