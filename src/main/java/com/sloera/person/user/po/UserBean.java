package com.sloera.person.user.po;

import java.util.Date;

public class UserBean {
    String id;//主键
    String userName;//用户名
    String account;//账号
    String password;//密码
    Date birthday;//出生日期
    int age;//年龄
    String sex;//性别
    String email;//邮箱
    int version;//版本号
    String active;//激活状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", version=" + version +
                ", active='" + active + '\'' +
                '}';
    }
}
