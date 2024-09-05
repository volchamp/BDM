package com.kmvc.jeesite.modules.syncPortal.user.model;

import com.kmvc.jeesite.modules.syncPortal.utils.Base64Util;
import org.codehaus.jackson.annotate.*;
import java.util.*;
import com.kmvc.jeesite.modules.syncPortal.utils.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalUserPO extends PortalPO
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String userCode;
    private String userDescription;
    private String userName;
    private String password;
    private String name;
    private String sex;
    private String idCard;
    private String mobile;
    private String telephone;
    private String email;
    private List<PortalRelationPO> relation;
    private String type;
    private String status;
    private String changeTyep;
    private Date startTime;
    private Date endTime;
    public String officeType;

    public String getOfficeType() {
        return this.officeType;
    }

    public void setOfficeType(final String officeType) {
        this.officeType = officeType;
    }

    public String getChangeTyep() {
        return this.changeTyep;
    }

    public void setChangeTyep(final String changeTyep) {
        this.changeTyep = changeTyep;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }

    public PortalUserPO() {
    }

    public PortalUserPO(final String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(final String userCode) {
        this.userCode = userCode;
    }

    public String getUserDescription() {
        return this.userDescription;
    }

    public void setUserDescription(final String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setIdCard(final String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(final String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<PortalRelationPO> getRelation() {
        return this.relation;
    }

    public void setRelation(final List<PortalRelationPO> relation) {
        this.relation = relation;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    private String dePassword(final String pwd) {
        System.out.println("======pwd=====" + pwd);
        final String newPwd = Base64Util.decode(pwd);
        return newPwd;
    }
}
