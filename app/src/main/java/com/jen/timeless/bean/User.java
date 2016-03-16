package com.jen.timeless.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenmingqun on 2015/12/10.
 */
public class User extends BmobObject {
    String username;
    String password;
    String mobilePhoneNumber;
    Boolean mobilePhoneNumberVerified;
    String email;
    Boolean emailVerified;

    public User(){
//        this.setTableName("User");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Boolean getMobilePhoneNumberVerified() {
        return mobilePhoneNumberVerified;
    }

    public void setMobilePhoneNumberVerified(Boolean mobilePhoneNumberVerified) {
        this.mobilePhoneNumberVerified = mobilePhoneNumberVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", mobilePhoneNumberVerified=" + mobilePhoneNumberVerified +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                '}';
    }
}
