package com.kmvc.jeesite.modules.connector.vo.serve.inquire;

public class ListCodeSetReq
{
    private String messageId;
    private String systemCode;
    private String systemName;

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public String getSystemCode() {
        return this.systemCode;
    }

    public void setSystemCode(final String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }
}
