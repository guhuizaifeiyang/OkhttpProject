package com.secularbird.netproject.net.okhttp;

public interface NetResultCallback {

    /**
     * 网络请求响应结果的回调接口
     */
    public void onResult(String body);

    /**
     * 网络请求响应结果的回调接口
     */
    public void onError(String body);

}

