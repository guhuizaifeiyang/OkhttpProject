package com.secularbird.netproject.bussiness;

import android.util.Log;
import android.widget.Toast;

import com.secularbird.netproject.model.RespUpLoad;
import com.secularbird.netproject.net.okhttp.MyOKHttpClient;
import com.secularbird.netproject.net.okhttp.NetResultCallback;
import com.secularbird.netproject.tools.UtilsJson;
import com.secularbird.netproject.ui.IUserInfoActivity;
import com.secularbird.netproject.ui.UserInfoActivity;

import java.io.File;
import java.util.HashMap;

import static com.secularbird.netproject.app.MyApplication.TAG;
import static com.secularbird.netproject.app.MyApplication.baseUrl;
import static com.secularbird.netproject.app.MyApplication.mImageLoader;
import static com.secularbird.netproject.app.MyApplication.mUserInfo;

/**
 * Created by jiangguangchao on 2017/7/19.
 */
public class UserInfoBussiness implements IUserInfoBussiness {

    private IUserInfoActivity mView;

    private MyOKHttpClient myOKHttpClient = MyOKHttpClient.getInstance();

    @Override
    public void onCreate(IUserInfoActivity view) {
        this.mView = view;
    }

    @Override
    public void upLoadIcon(String path) {

        mView.onUpLoading();

        File uploadFile = new File(path);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("UploadForm[file]", uploadFile);
        MyOKHttpClient.getInstance().upLoadFile(baseUrl + "/file/upload-img", params, new NetResultCallback() {
            @Override
            public void onResult(String body) {
                Log.i(TAG, "onResult:   " + body);

                RespUpLoad respUpLoad = UtilsJson.getBean(body, RespUpLoad.class);

                mUserInfo.avatar = respUpLoad.data.img_url;

                mView.onLoadSuccess(mUserInfo.avatar);

            }

            @Override
            public void onError(String body) {
                Log.i(TAG, "onError:   " + body);
                mView.onLoadFailed();
            }
        });

    }
}
