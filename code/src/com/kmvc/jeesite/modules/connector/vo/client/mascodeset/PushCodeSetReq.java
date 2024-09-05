package com.kmvc.jeesite.modules.connector.vo.client.mascodeset;

public class PushCodeSetReq
{
    private PushCodeSetReqHeader pushCodeSetReqHeader;
    private PushCodeSetReqBody pushCodeSetReqBody;

    public PushCodeSetReqHeader getPushCodeSetReqHeader() {
        return this.pushCodeSetReqHeader;
    }

    public void setPushCodeSetReqHeader(final PushCodeSetReqHeader pushCodeSetReqHeader) {
        this.pushCodeSetReqHeader = pushCodeSetReqHeader;
    }

    public PushCodeSetReqBody getPushCodeSetReqBody() {
        return this.pushCodeSetReqBody;
    }

    public void setPushCodeSetReqBody(final PushCodeSetReqBody pushCodeSetReqBody) {
        this.pushCodeSetReqBody = pushCodeSetReqBody;
    }
}
