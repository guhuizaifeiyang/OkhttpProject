package com.secularbird.netproject.net.okhttp;


import java.util.Map;


/**
 * 处理网络请求相关的接口类
 */
public interface IOKhttpClient {

    /**
     * 发送GET请求
     *
     * @param url      请求URL
     * @param params   键值对(GET请求的参数加在url后面)
     * @param callback 响应结果回调
     */
    void httpGet(String url, Map<String, String> params, final NetResultCallback callback);

    /**
     * POST键值对
     *
     * @param url      请求URL
     * @param params   键值对
     * @param callback 响应结果回调
     */
    void httpPostPairs(String url, Map<String, String> params, final NetResultCallback callback);

    /**
     * 文件上传
     *
     * @param url    请求URL
     * @param paramsMap 参数（包含文件）
     ***/
    void upLoadFile(String url, Map<String, Object> paramsMap, final NetResultCallback callback);

}

