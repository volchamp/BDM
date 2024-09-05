package com.kmvc.jeesite.test.entity;

import com.thinkgem.jeesite.common.persistence.*;
import com.fasterxml.jackson.annotation.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

public class TestTree extends TreeEntity<TestTree>
{
    private static final long serialVersionUID = 1L;
    private TestTree parent;
    private String parentIds;
    private String name;
    private Integer sort;

    public TestTree() {
    }

    public TestTree(final String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull(message = "\u7236\u7ea7\u7f16\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public TestTree getParent() {
        return this.parent;
    }

    public void setParent(final TestTree parent) {
        this.parent = parent;
    }

    @Length(min = 1, max = 2000, message = "\u6240\u6709\u7236\u7ea7\u7f16\u53f7\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 2000 \u4e4b\u95f4")
    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(final String parentIds) {
        this.parentIds = parentIds;
    }

    @Length(min = 1, max = 100, message = "\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 100 \u4e4b\u95f4")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @NotNull(message = "\u6392\u5e8f\u4e0d\u80fd\u4e3a\u7a7a")
    public Integer getSort() {
        return this.sort;
    }

    public void setSort(final Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return (this.parent != null && this.parent.getId() != null) ? this.parent.getId() : "0";
    }
}
