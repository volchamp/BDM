package com.kmvc.jeesite.modules.connector.vo.client.mascodeset;

public class PushCodeSetReqHeader
{
    private String messageId;
    private int pageSize;
    private int currentPage;
    private int totalPage;

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(final int totalPage) {
        this.totalPage = totalPage;
    }
}
