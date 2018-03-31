package com.secularbird.netproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.secularbird.netproject.R;
import com.secularbird.netproject.bussiness.ILoginBussiness;
import com.secularbird.netproject.bussiness.LoginBussiness;
import com.secularbird.netproject.net.okhttp.NetResultCallback;
import com.secularbird.netproject.net.okhttp.MyOKHttpClient;
import com.secularbird.netproject.ui.component.CustomProgress;

import java.util.HashMap;

import static com.secularbird.netproject.app.MyApplication.TAG;
import static com.secularbird.netproject.app.MyApplication.baseUrl;

public class LoginActivity extends AppCompatActivity implements ILoginActivity {

    private EditText mUserNameEdt;
    private EditText mUserPassWordEdt;

    private CustomProgress customProgress;

    private ILoginBussiness mBussiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBussiness = new LoginBussiness();
        mBussiness.onCreate(this);

        mUserNameEdt = (EditText) findViewById(R.id.userNameEdt);
        mUserPassWordEdt = (EditText) findViewById(R.id.passWordEdt);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserNameEdt.getText().toString();
                String passWord = mUserPassWordEdt.getText().toString();

                if (userName != null && !userName.equals("") && passWord != null && !passWord.equals("")) {
                    mBussiness.login(userName, passWord);
                }


            }
        });


        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserNameEdt.getText().toString();
                String passWord = mUserPassWordEdt.getText().toString();

                if (userName != null && !userName.equals("") && passWord != null && !passWord.equals("")) {
                    mBussiness.registeUser(userName, passWord);
                }

            }
        });

    }

    @Override
    public void onLogining() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        customProgress = CustomProgress.show(LoginActivity.this, "正在登录...", false, null);
    }

    @Override
    public void onLoginSuccess() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisting() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        customProgress = CustomProgress.show(LoginActivity.this, "正在注册...", false, null);
    }

    @Override
    public void onRegisteSuccess() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRegisteFailed() {
        if (customProgress != null) {
            customProgress.cancel();
            customProgress = null;
        }
        Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
    }

}
