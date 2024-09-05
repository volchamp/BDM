package com.kmvc.jeesite.modules.syncPortal.user.vo;

public class ResultVO
{
    private String resultType;
    private String description;

    public ResultVO() {
        this.resultType = "1";
        this.description = "\u64cd\u4f5c\u6210\u529f";
    }

    public String getResultType() {
        return this.resultType;
    }

    public void setResultType(final String resultType) {
        this.resultType = resultType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
