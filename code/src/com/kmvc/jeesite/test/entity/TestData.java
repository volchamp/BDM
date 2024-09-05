package com.kmvc.jeesite.test.entity;

import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;

public class TestData extends DataEntity<TestData>
{
    private static final long serialVersionUID = 1L;
    private User user;
    private Office office;
    private Area area;
    private String name;
    private String sex;
    private Date inDate;
    private Date beginInDate;
    private Date endInDate;

    public TestData() {
    }

    public TestData(final String id) {
        super(id);
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

    public Date getBeginInDate() {
        return this.beginInDate;
    }

    public void setBeginInDate(final Date beginInDate) {
        this.beginInDate = beginInDate;
    }

    public Date getEndInDate() {
        return this.endInDate;
    }

    public void setEndInDate(final Date endInDate) {
        this.endInDate = endInDate;
    }
}
