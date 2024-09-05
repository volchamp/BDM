package com.kmvc.jeesite.modules.syncPortal.vo;

import org.codehaus.jackson.annotate.*;
import java.util.*;

public class UserVO
{
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("UserCode")
    private String userCode;
    @JsonProperty("UserDescription")
    private String userDescription;
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Sex")
    private String sex;
    @JsonProperty("IDCARD")
    private String iDCARD;
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("Telephone")
    private String telephone;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("relation")
    private List<UserOrgPostRelationVO> relation;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("STATUS")
    private String status;
    private UserChangeVO changeInfo;

    public UserChangeVO getChangeInfo() {
        return this.changeInfo;
    }

    public void setChangeInfo(final UserChangeVO changeInfo) {
        this.changeInfo = changeInfo;
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

    public String getiDCARD() {
        return this.iDCARD;
    }

    public void setiDCARD(final String iDCARD) {
        this.iDCARD = iDCARD;
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

    public List<UserOrgPostRelationVO> getRelation() {
        return this.relation;
    }

    public void setRelation(final List<UserOrgPostRelationVO> relation) {
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
}
