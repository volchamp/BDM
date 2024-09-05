package com.kmvc.jeesite.test.entity;

import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;
import com.google.common.collect.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class TestDataMain extends DataEntity<TestDataMain>
{
    private static final long serialVersionUID = 1L;
    private User user;
    private Office office;
    private Area area;
    private String name;
    private String sex;
    private Date inDate;
    private List<TestDataChild> testDataChildList;

    public TestDataMain() {
        this.testDataChildList =Lists.newArrayList();
    }

    public TestDataMain(final String id) {
        super(id);
        this.testDataChildList = Lists.newArrayList();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Office getOffice() {
        return this.office;
    }

    public void setOffice(final Office office) {
        this.office = office;
    }

    public Area getArea() {
        return this.area;
    }

    public void setArea(final Area area) {
        this.area = area;
    }

    @Length(min = 0, max = 100, message = "\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Length(min = 0, max = 1, message = "\u6027\u522b\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 1 \u4e4b\u95f4")
    public String getSex() {
        return this.sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getInDate() {
        return this.inDate;
    }

    public void setInDate(final Date inDate) {
        this.inDate = inDate;
    }

    public List<TestDataChild> getTestDataChildList() {
        return this.testDataChildList;
    }

    public void setTestDataChildList(final List<TestDataChild> testDataChildList) {
        this.testDataChildList = testDataChildList;
    }
}
