package com.kmvc.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import org.hibernate.validator.constraints.*;
import com.thinkgem.jeesite.common.utils.excel.annotation.*;

public class TSystem extends DataEntity<TSystem>
{
    private static final long serialVersionUID = 1L;
    private String systemCode;
    private String systemName;
    private String systemShort;
    private String serviceAddr;
    private String checkDate;
    private Long systemSoprt;
    private String receivers;
    private String txtSysName;
    private String adminName;
    private Area area;
    private Integer num;
    private Long total;

    public TSystem() {
    }

    public TSystem(final String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 1 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801", align = 1, sort = 1)
    public String getSystemCode() {
        return this.systemCode;
    }

    public void setSystemCode(final String systemCode) {
        this.systemCode = systemCode;
    }

    @Length(min = 0, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0", align = 1, sort = 2)
    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    @Length(min = 0, max = 50, message = "\u4e1a\u52a1\u7cfb\u7edf\u7b80\u79f0\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    @ExcelField(title = "\u4e1a\u52a1\u7cfb\u7edf\u7b80\u79f0", align = 1, sort = 3)
    public String getSystemShort() {
        return this.systemShort;
    }

    public void setSystemShort(final String systemShort) {
        this.systemShort = systemShort;
    }

    @Length(min = 0, max = 500, message = "\u670d\u52a1\u5730\u5740\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 500 \u4e4b\u95f4")
    @ExcelField(title = "\u670d\u52a1\u5730\u5740", align = 1, sort = 4)
    public String getServiceAddr() {
        return this.serviceAddr;
    }

    public void setServiceAddr(final String serviceAddr) {
        this.serviceAddr = serviceAddr;
    }

    @ExcelField(title = "\u5907\u6ce8", align = 1, sort = 5)
    public String getRemark() {
        return this.getRemarks();
    }

    public void setRemark(final String remark) {
        this.remarks = remark;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public void setAdminName(final String adminName) {
        this.adminName = adminName;
    }

    @ExcelField(title = "\u533a\u57df", align = 1, sort = 6)
    public Area getArea() {
        return this.area;
    }

    public void setArea(final Area area) {
        this.area = area;
    }

    @Length(min = 0, max = 50, message = "\u6570\u636e\u68c0\u6838\u65f6\u95f4\u5df2\u5f03\u7528\uff0c\u68c0\u6838\u65f6\u95f4\u5df2\u4f7f\u7528pwp\u4ea7\u54c1\u7684\u68c0\u6838\u529f\u80fd\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(final String checkDate) {
        this.checkDate = checkDate;
    }

    public Long getSystemSoprt() {
        return this.systemSoprt;
    }

    public void setSystemSoprt(final Long systemSoprt) {
        this.systemSoprt = systemSoprt;
    }

    @Length(min = 0, max = 1000, message = "\u817e\u8baf\u901a \u8d26\u6237\u4fe1\u606f\uff0c\u591a\u4e2a\u7528\u9017\u53f7\u9694\u5f00\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 1000 \u4e4b\u95f4")
    public String getReceivers() {
        return this.receivers;
    }

    public void setReceivers(final String receivers) {
        this.receivers = receivers;
    }

    @Length(min = 0, max = 50, message = "\u817e\u8baf\u901a\u4e2d\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u957f\u5ea6\u5fc5\u987b\u4ecb\u4e8e 0 \u548c 50 \u4e4b\u95f4")
    public String getTxtSysName() {
        return this.txtSysName;
    }

    public void setTxtSysName(final String txtSysName) {
        this.txtSysName = txtSysName;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(final Long total) {
        this.total = total;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(final Integer num) {
        this.num = num;
    }
}
