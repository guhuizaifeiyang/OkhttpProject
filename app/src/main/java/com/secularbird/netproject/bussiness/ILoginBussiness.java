package com.secularbird.netproject.bussiness;

import com.secularbird.netproject.ui.ILoginActivity;

/**
 * Created by jiangguangchao on 2017/7/18.
 */
public interface ILoginBussiness {

     void onCreate(ILoginActivity view);

     void registeUser(String username, String passwd);

     void login(String username, String passwd);


}
