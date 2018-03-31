package com.secularbird.netproject.net.okhttp;

/**
 * 处理网络请求相关的接口实现类
 *
 * @author 蒋广超
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.secularbird.netproject.app.MyApplication.TAG;

public class MyOKHttpClient implements IOKhttpClient {

    private static MyOKHttpClient mInstance;
    //OKHttp3对象
    private OkHttpClient mOKHttpClient;

    private MyOKHttpClient() {
        //实现Cookie自动管理
        mOKHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    List<Cookie> cookies;

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        String host = url.host();
                        Log.i(TAG, "url host: " + host);
                        this.cookies = cookies;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        if (cookies != null)
                            return cookies;
                        return new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    public static synchronized MyOKHttpClient getInstance() {
        if (mInstance == null) {
            mInstance = new MyOKHttpClient();
        }
        return mInstance;
    }

    /**
     * handler消息类型
     **/
    private final static int MSG_TYPE_NET = 1001;

    /**
     * 网络请求成功
     **/
    private final static int NET_RESPONSE = 0;

    /**
     * 网络请求失败
     **/
    private final static int NET_FAILURE = 1;

    private Map<String, NetResultCallback> callbacks = new HashMap<String, NetResultCallback>();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG_TYPE_NET) {
                String key = msg.getData().getString("key");
                int type = msg.getData().getInt("type");
                NetResultCallback callback = callbacks.get(key);
                callbacks.remove(key);
                Log.d(TAG, "callbacks.Size = " + callbacks.size());
                if (type == NET_RESPONSE) {
                    String body = (String) msg.obj;
                    callback.onResult(body);
                    msg.obj = null;
                } else if (type == NET_FAILURE) {
                    String res = "failed";
                    if (msg.getData() != null) {
                        res = msg.getData().getString("error");
                    }
                    callback.onError(res);
                }

            }
        }
    };


    @Override
    public void httpGet(String url, Map<String, String> params, final NetResultCallback callback) {
        callbacks.put(callback.toString(), callback);

        StringBuffer paramString = new StringBuffer();
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                paramString.append(key);
                paramString.append("=");
                paramString.append(value);
                paramString.append("&");
            }

        }

        String res = null;
        if (paramString.length() > 0) {
            res = paramString.substring(0, paramString.length() - 1);
        }
        if (res != null) {
            url = url + res;
        }

        Request request = new Request.Builder().url(url).build();
        Call call = mOKHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMsg2Handler(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                senResponseMsg2Handler(callback, response);
            }
        });

    }

    @Override
    public void httpPostPairs(String url, Map<String, String> params, final NetResultCallback callback) {

        callbacks.put(callback.toString(), callback);

        FormBody.Builder formBuilder = new FormBody.Builder();

        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                formBuilder.add(key, value);
            }
        }

        RequestBody body = formBuilder.build();

        Request request = new Request.Builder().url(url).post(body).build();

        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMsg2Handler(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                senResponseMsg2Handler(callback, response);
            }
        });


    }

    @Override
    public void upLoadFile(String url, Map<String, Object> paramsMap, final NetResultCallback callback) {

        callbacks.put(callback.toString(), callback);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
                }
            }
        }
        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request
        final Request request = new Request.Builder().url(url).post(body).build();
        final Call call = mOKHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMsg2Handler(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                senResponseMsg2Handler(callback, response);
            }
        });

    }

    /**
     * 先完成GET请求在提出该函数
     * <p>
     * 向handler发送网络请求失败的消息
     **/
    private void sendFailureMsg2Handler(NetResultCallback callback, IOException e) {
        if (callback != null) {
            Bundle data = new Bundle();
            Message msg = mHandler.obtainMessage();
            data.putInt("type", NET_FAILURE);
            data.putString("key", callback.toString());
            StringBuffer res = new StringBuffer();
            StackTraceElement[] els = e.getStackTrace();
            if (els != null && els.length > 0) {
                for (StackTraceElement ee : els) {
                    res.append(ee.toString());
                    res.append("\n");
                }
            }
            data.putString("error", res.toString());
            msg.setData(data);
            msg.what = MSG_TYPE_NET;
            msg.sendToTarget();
        }
    }

    /**
     * 先完成GET请求在提出该函数
     * <p>
     * 向handler发送网络请求成功的消息
     **/
    private void senResponseMsg2Handler(NetResultCallback callback, Response response) throws IOException {
        if (callback != null) {
            Bundle data = new Bundle();
            Message msg = mHandler.obtainMessage();
            data.putInt("type", NET_RESPONSE);
            data.putString("key", callback.toString());
            msg.setData(data);
            msg.what = MSG_TYPE_NET;
            //注意这里一定得是string方法
            msg.obj = response.body().string();
            msg.sendToTarget();
        }
    }

}
