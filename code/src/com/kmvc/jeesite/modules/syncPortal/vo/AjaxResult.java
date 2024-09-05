package com.kmvc.jeesite.modules.syncPortal.vo;

public class AjaxResult
{
    private int code;
    private String msg;
    private Object data;

    public AjaxResult() {
        this.code = 1;
        this.msg = "\u64cd\u4f5c\u6210\u529f";
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(final Object data) {
        this.data = data;
    }
}
