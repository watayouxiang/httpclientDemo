package com.watayouxiang.httpclientdemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.listener.KickOutListener;
import com.watayouxiang.httpclient.listener.OnCookieListener;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.ConfigReq;
import com.watayouxiang.httpclient.model.request.LoginReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.LoginResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.httpclientdemo.model.MyLoginReq;
import com.watayouxiang.httpclientdemo.model.MyLoginResp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Cookie;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://test.tiocloud.com";
    private static final String PWD = null;
    private static final String ACCOUNT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTioHttpClient();

        reqConfig();
        login(PWD, ACCOUNT);
        synReq();
        defineMyReqSample("test", "123456");
    }

    private void defineMyReqSample(String account, String pwd) {
        new MyLoginReq(account, pwd)
                .setCancelTag(this)
                .setCacheMode(CacheMode.NO_CACHE)
                .get(new TioCallback<MyLoginResp>() {
                    @Override
                    public void onTioSuccess(MyLoginResp myLoginResp) {

                    }

                    @Override
                    public void onTioError(String s) {

                    }
                });
    }

    private void synReq() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 同步请求
                Response<BaseResp<UserCurrResp>> response = new UserCurrReq().get();
                if (response.isSuccessful()) {
                    BaseResp<UserCurrResp> body = response.body();
                    if (body.isOk()) {
                        UserCurrResp data = body.getData();
                    } else {
                        String msg = body.getMsg();
                    }
                } else {
                    Throwable e = response.getException();
                }

            }
        }).start();
    }

    private void login(String pwd, String account) {
        // post 异步请求
        LoginReq.getPwdInstance(pwd, account)
                .post(new TioCallback<LoginResp>() {
                    @Override
                    public void onTioSuccess(LoginResp loginResp) {

                    }

                    @Override
                    public void onTioError(String s) {

                    }
                });
    }

    public void reqConfig() {
        new ConfigReq()
                // 取消请求的 TAG
                .setCancelTag(this)
                // 缓存模式：请求失败，才读取缓存
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                // get 异步请求
                .get(new TioCallback<ConfigResp>() {
                    @Override
                    public void onTioSuccess(ConfigResp configResp) {

                    }

                    @Override
                    public void onTioError(String s) {

                    }
                });
    }

    private void initTioHttpClient() {
        // 初始化
        TioHttpClient.getInstance().init();
        // 修改 baseUrl 地址
        HttpPreferences.saveBaseUrl(BASE_URL);
        // 修改 baseUrl 地址
        HttpPreferences.saveBaseUrl(BASE_URL);
        // cookies 变更监听
        TioHttpClient.getInstance().setOnCookieListener(new OnCookieListener() {
            @Override
            public void onSaveTioCookiesFromResponse(@NonNull @NotNull List<Cookie> list) {

            }
        });
        // 设置渠道号，默认 official
        TioHttpClient.getInstance().getHeaderInterceptor().setChannelId("httpclient sample");
        // 踢出登录监听
        TioHttpClient.getInstance().getRespInterceptor().setKickOutListener(new KickOutListener() {
            @Override
            public void onKickOut(String s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消 TAG 标记的请求
        TioHttpClient.cancel(this);
    }
}