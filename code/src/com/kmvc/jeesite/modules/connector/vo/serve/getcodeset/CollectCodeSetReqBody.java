package com.kmvc.jeesite.modules.connector.vo.serve.getcodeset;

public class CollectCodeSetReqBody
{
    private String systemCode;
    private String systemName;
    private String codeSetNo;
    private String codeSetName;

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

    public String getCodeSetNo() {
        return this.codeSetNo;
    }

    public void setCodeSetNo(final String codeSetNo) {
        this.codeSetNo = codeSetNo;
    }

    public String getCodeSetName() {
        return this.codeSetName;
    }

    public void setCodeSetName(final String codeSetName) {
        this.codeSetName = codeSetName;
    }
}
