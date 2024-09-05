package com.kmvc.jeesite.modules.connector.vo.serve.inquire;

import java.util.*;

public class ListCodeSetResp
{
    private List<CodeSet> codeSetCollection;
    private int resFlag;
    private String errorMessage;

    public List<CodeSet> getCodeSetCollection() {
        return this.codeSetCollection;
    }

    public void setCodeSetCollection(final List<CodeSet> codeSetCollection) {
        this.codeSetCollection = codeSetCollection;
    }

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
