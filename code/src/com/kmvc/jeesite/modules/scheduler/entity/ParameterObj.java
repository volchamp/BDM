package com.kmvc.jeesite.modules.scheduler.entity;

public class ParameterObj
{
    private String type;
    private String name;
    private String value;

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = ((type == null) ? "" : type.trim());
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = ((name == null) ? "" : name.trim());
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = ((value == null) ? "" : value.trim());
    }
}
