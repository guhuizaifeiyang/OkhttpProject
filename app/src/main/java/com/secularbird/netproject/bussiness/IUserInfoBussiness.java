package com.secularbird.netproject.bussiness;

import com.secularbird.netproject.ui.ILoginActivity;
import com.secularbird.netproject.ui.IUserInfoActivity;

/**
 * Created by jiangguangchao on 2017/7/19.
 */

public interface IUserInfoBussiness {


    void onCreate(IUserInfoActivity view);

    void upLoadIcon(String path);

}
