package com.secularbird.netproject.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.secularbird.netproject.model.UserInfo;

/**
 * Created by jiangguangchao on 2017/7/19.
 */
public class MyApplication extends Application {

    public static final String TAG = "NetProject";

    public static final String baseUrl = "http://35.185.149.228";

    public static UserInfo mUserInfo;

    public static ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(this));

    }
}
