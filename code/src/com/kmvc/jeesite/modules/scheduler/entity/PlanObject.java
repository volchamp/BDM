package com.kmvc.jeesite.modules.scheduler.entity;

public class PlanObject
{
    private Integer type;
    private String timeInterval;

    public int getType() {
        return this.type;
    }

    public void setType(final Integer type) {
        this.type = ((type == null) ? 0 : type);
    }

    public String getTimeInterval() {
        return this.timeInterval;
    }

    public void setTimeInterval(final String timeInterval) {
        this.timeInterval = ((timeInterval == null) ? "" : timeInterval.trim());
    }
}
