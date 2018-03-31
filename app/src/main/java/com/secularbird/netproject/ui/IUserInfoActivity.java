package com.secularbird.netproject.ui;

/**
 * Created by jiangguangchao on 2017/7/19.
 */
public interface IUserInfoActivity {

    void onUpLoading();

    void onLoadSuccess(String newUrl);

    void onLoadFailed();

}
