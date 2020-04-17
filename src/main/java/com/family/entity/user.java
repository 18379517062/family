package com.family.entity;

import java.util.Date;

public class user {
    private Integer id;

    private String userName;

    private String password;

    private String realName;

    private Boolean sex;

    private String phone;

    private String email;

    private Date birthday;

    private Date createTime;

    private Date updateTime;

    private String headPicture;


    public user(Integer id, String userName, String password, String realName, Boolean sex, String phone, String email, Date birthday, Date createTime, Date updateTime, String headPicture) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.realName = realName;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.headPicture = headPicture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture == null ? null : headPicture.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRealNJame() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }
}