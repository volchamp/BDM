package com.kmvc.jeesite.test.entity;

import com.thinkgem.jeesite.common.persistence.*;
import org.hibernate.validator.constraints.*;

public class TestDataChild extends DataEntity<TestDataChild>
{
    private static final long serialVersionUID = 1L;
    private TestDataMain testDataMain;
    private String name;

    public TestDataChild() {
    }

    public TestDataChild(final String id) {
        super(id);
    }

    public TestDataChild(final TestDataMain testDataMain) {
        this.testDataMain = testDataMain;
    }

    @Length(min = 0, max = 64, message = "\u4e1a\u52a1\u4e3b\u8868\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 64 \u4e4b\u95f4")
    public TestDataMain getTestDataMain() {
        return this.testDataMain;
    }

    public void setTestDataMain(final TestDataMain testDataMain) {
        this.testDataMain = testDataMain;
    }

    @Length(min = 0, max = 100, message = "\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 100 \u4e4b\u95f4")
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
