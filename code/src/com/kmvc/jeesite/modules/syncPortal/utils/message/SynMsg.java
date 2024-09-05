package com.kmvc.jeesite.modules.syncPortal.utils.message;

import java.util.*;

public class SynMsg
{
    private String title;
    private int type;
    private List<String> error;
    private List<String> warn;
    private List<String> info;
    private SynMsgList childs;

    public SynMsg() {
        this.error = new ArrayList<String>();
        this.warn = new ArrayList<String>();
        this.info = new ArrayList<String>();
        this.childs = new SynMsgList();
    }

    public SynMsg(final String title) {
        this.error = new ArrayList<String>();
        this.warn = new ArrayList<String>();
        this.info = new ArrayList<String>();
        this.childs = new SynMsgList();
        this.title = title;
    }

    public SynMsgList getChilds() {
        return this.childs;
    }

    public void setChilds(final SynMsgList childs) {
        this.childs = childs;
    }

    public List<String> getInfo() {
        return this.info;
    }

    public void setInfo(final List<String> info) {
        this.info = info;
    }

    public void addError(final String msg) {
        this.error.add(msg);
    }

    public void addWarn(final String msg) {
        this.warn.add(msg);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<String> getError() {
        return this.error;
    }

    public void setError(final List<String> error) {
        this.error = error;
    }

    public List<String> getWarn() {
        return this.warn;
    }

    public void setWarn(final List<String> warn) {
        this.warn = warn;
    }

    public int getType() {
        return this.type;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public void addInfo(final String msg) {
        this.info.add(msg);
    }
}
