package com.secularbird.netproject.model;

/**
 * Created by jiangguangchao on 2017/7/19.
 */
public class RespLogin {

    public int status;

    public UserInfo data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
