package com.kmvc.jeesite.modules.connector.vo.client.mascodeset;

public class PushCodeSetResp
{
    private int resFlag;
    private String errorMessage;

    public int getResFlag() {
        return this.resFlag;
    }

    public void setResFlag(final int resFlag) {
        this.resFlag = resFlag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
