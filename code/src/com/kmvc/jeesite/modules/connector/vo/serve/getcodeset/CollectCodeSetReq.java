package com.kmvc.jeesite.modules.connector.vo.serve.getcodeset;

public class CollectCodeSetReq
{
    private CollectCodeSetReqHeader collectCodeSetReqHeader;
    private CollectCodeSetReqBody collectCodeSetReqBody;

    public CollectCodeSetReqHeader getCollectCodeSetReqHeader() {
        return this.collectCodeSetReqHeader;
    }

    public void setCollectCodeSetReqHeader(final CollectCodeSetReqHeader collectCodeSetReqHeader) {
        this.collectCodeSetReqHeader = collectCodeSetReqHeader;
    }

    public CollectCodeSetReqBody getCollectCodeSetReqBody() {
        return this.collectCodeSetReqBody;
    }

    public void setCollectCodeSetReqBody(final CollectCodeSetReqBody collectCodeSetReqBody) {
        this.collectCodeSetReqBody = collectCodeSetReqBody;
    }
}
