package com.secularbird.netproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.secularbird.netproject.R;
import com.secularbird.netproject.bussiness.IUserInfoBussiness;
import com.secularbird.netproject.bussiness.UserInfoBussiness;
import com.secularbird.netproject.model.RespUpLoad;
import com.secularbird.netproject.net.okhttp.MyOKHttpClient;
import com.secularbird.netproject.net.okhttp.NetResultCallback;
import com.secularbird.netproject.tools.UtilsJson;
import com.secularbird.netproject.ui.component.CircleImageView;
import com.secularbird.netproject.ui.component.CustomProgress;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static com.secularbird.netproject.app.MyApplication.TAG;
import static com.secularbird.netproject.app.MyApplication.baseUrl;
import static com.secularbird.netproject.app.MyApplication.mImageLoader;
import static com.secularbird.netproject.app.MyApplication.mUserInfo;

/**
 * Created by jiangguangchao on 2017/7/18.
 */
public class UserInfoActivity extends AppCompatActivity implements IUserInfoActivity {

    private CircleImageView userIcon;
    private TextView userNameTxt;
    private TextView userIDTxt;
    private TextView userTypeTxt;

    private DisplayImageOptions mOptions;

    private CustomProgress customProgress;

    private IUserInfoBussiness mBussiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        init();
        setData();

    }

    private void init() {
        userIcon = (CircleImageView) findViewById(R.id.userIcon);
        userNameTxt = (TextView) findViewById(R.id.userNameTxt);
        userIDTxt = (TextView) findViewById(R.id.userIDTxt);
        userTypeTxt = (TextView) findViewById(R.id.userTypeTxt);

        mOptions = new DisplayImageOptions.Builder().cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build();

        mBussiness = new UserInfoBussiness();
        mBussiness.onCreate(UserInfoActivity.this);

    }

    private final int REQUEST_PIC_CHOOSED = 102;

    private void setData() {
        //显示头像
        mImageLoader.displayImage(baseUrl + mUserInfo.avatar, userIcon, mOptions);
        userNameTxt.setText(mUserInfo.username);
        userIDTxt.setText(mUserInfo.id + "");
        if (mUserInfo.type.equals("2"))
            userTypeTxt.setText("老师");
        userTypeTxt.setText("学生");

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoPickerIntent intent = new PhotoPickerIntent(UserInfoActivity.this);
                intent.setSelectModel(SelectModel.SINGLE);
                startActivityForResult(intent, REQUEST_PIC_CHOOSED);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PIC_CHOOSED) {
                // 选择照片
                List<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);

                if (paths != null) {

                    String path = paths.get(0);
                    Log.i(TAG, "choosed path: " + path);

                    mBussiness.upLoadIcon(path);

                }


            }
        }
    }


    @Override
    public void onUpLoading() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        customProgress = CustomProgress.show(UserInfoActivity.this, "正在上传...", false, null);
    }

    @Override
    public void onLoadSuccess(String newUrl) {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        //显示头像
        mImageLoader.displayImage(baseUrl + mUserInfo.avatar, userIcon, mOptions);

        Toast.makeText(UserInfoActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFailed() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        Toast.makeText(UserInfoActivity.this, "头像上传失败，请重试", Toast.LENGTH_SHORT).show();
    }
}
