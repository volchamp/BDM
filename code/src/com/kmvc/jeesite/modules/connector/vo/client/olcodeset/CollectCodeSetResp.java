package com.kmvc.jeesite.modules.connector.vo.client.olcodeset;

public class CollectCodeSetResp
{
    private CollectCodeSetResHeader collectCodeSetResHeader;
    private CollectCodeSetResBody collectCodeSetResBody;
    private String resFlag;
    private String errorMessage;

    public String getResFlag() {
        return this.resFlag;
    }

    public void setResFlag(final String resFlag) {
        this.resFlag = resFlag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CollectCodeSetResHeader getCollectCodeSetResHeader() {
        return this.collectCodeSetResHeader;
    }

    public void setCollectCodeSetResHeader(final CollectCodeSetResHeader collectCodeSetResHeader) {
        this.collectCodeSetResHeader = collectCodeSetResHeader;
    }

    public CollectCodeSetResBody getCollectCodeSetResBody() {
        return this.collectCodeSetResBody;
    }

    public void setCollectCodeSetResBody(final CollectCodeSetResBody collectCodeSetResBody) {
        this.collectCodeSetResBody = collectCodeSetResBody;
    }
}
