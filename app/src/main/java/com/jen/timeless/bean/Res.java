package com.jen.timeless.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by chenmingqun on 2015/12/10.
 */
public class Res extends BmobObject {
    String name;
    String location;
    String type;
    String changeType;
    String wantRes;
    String imgUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getWantRes() {
        return wantRes;
    }

    public void setWantRes(String wantRes) {
        this.wantRes = wantRes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Res{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", changeType='" + changeType + '\'' +
                ", wantRes='" + wantRes + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
