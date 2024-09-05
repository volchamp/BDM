package com.kmvc.jeesite.modules.codesetmrg.entity;

import com.thinkgem.jeesite.common.persistence.*;
import org.hibernate.validator.constraints.*;

public class DataSetCategory extends DataEntity<DataSetCategory>
{
    private static final long serialVersionUID = 1L;
    private String codeGroupName;
    private String codeStartWith;
    private String codeEndWith;
    private Integer codeGroupSort;

    public DataSetCategory() {
    }

    public DataSetCategory(final String id) {
        super(id);
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u5206\u7ec4\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeGroupName() {
        return this.codeGroupName;
    }

    public void setCodeGroupName(final String codeGroupName) {
        this.codeGroupName = codeGroupName;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u4ee3\u7801\u5f00\u59cb\u8303\u56f4\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeStartWith() {
        return this.codeStartWith;
    }

    public void setCodeStartWith(final String codeStartWith) {
        this.codeStartWith = codeStartWith;
    }

    @Length(min = 0, max = 50, message = "\u4ee3\u7801\u96c6\u4ee3\u7801\u7ed3\u675f\u8303\u56f4\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCodeEndWith() {
        return this.codeEndWith;
    }

    public void setCodeEndWith(final String codeEndWith) {
        this.codeEndWith = codeEndWith;
    }

    public Integer getCodeGroupSort() {
        return this.codeGroupSort;
    }

    public void setCodeGroupSort(final Integer codeGroupSort) {
        this.codeGroupSort = codeGroupSort;
    }
}
