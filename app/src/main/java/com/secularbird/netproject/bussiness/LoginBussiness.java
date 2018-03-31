package com.secularbird.netproject.bussiness;

import android.util.Log;

import com.secularbird.netproject.app.MyApplication;
import com.secularbird.netproject.model.RespLogin;
import com.secularbird.netproject.net.okhttp.NetResultCallback;
import com.secularbird.netproject.net.okhttp.MyOKHttpClient;
import com.secularbird.netproject.tools.UtilsJson;
import com.secularbird.netproject.ui.ILoginActivity;

import java.util.HashMap;

import static com.secularbird.netproject.app.MyApplication.TAG;
import static com.secularbird.netproject.app.MyApplication.baseUrl;

/**
 * Created by jiangguangchao on 2017/7/18.
 */
public class LoginBussiness implements ILoginBussiness {

    private ILoginActivity mView;

    private MyOKHttpClient myOKHttpClient = MyOKHttpClient.getInstance();

    @Override
    public void onCreate(ILoginActivity view) {
        this.mView = view;
    }

    @Override
    public void registeUser(String username, String passwd) {

        /***
         * - signup_username："用户名"
         - type："用户类型"(1学生，2老师)
         - big_direction："学习/研究大方向id"
         - signup_password："用户密码"
         * */
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("signup_username", username);
        params.put("type", "1");
        params.put("big_direction", "1");
        params.put("signup_password", passwd);

        mView.onRegisting();

        MyOKHttpClient.getInstance().httpPostPairs(baseUrl + "/user/setting", params, new NetResultCallback() {
            @Override
            public void onResult(String body) {
                Log.i(TAG, "onResult:   " + body);
                mView.onRegisteSuccess();
            }

            @Override
            public void onError(String body) {
                Log.i(TAG, "onError:   " + body);
                mView.onRegisteFailed();
            }
        });


    }

    @Override
    public void login(String username, String passwd) {

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("login_username", username);
        params.put("login_password", passwd);

        mView.onLogining();

        MyOKHttpClient.getInstance().httpPostPairs(baseUrl + "/user/do-login", params, new NetResultCallback() {
            @Override
            public void onResult(String body) {

                RespLogin respLogin = UtilsJson.getBean(body, RespLogin.class);

                MyApplication.mUserInfo = respLogin.getData();

                mView.onLoginSuccess();
                Log.i(TAG, "onResult:   " + body);
            }

            @Override
            public void onError(String body) {
                Log.i(TAG, "onError:   " + body);

                mView.onLoginFailed();
            }
        });

    }
}
