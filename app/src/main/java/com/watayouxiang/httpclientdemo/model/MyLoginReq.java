package com.watayouxiang.httpclientdemo.model;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/05/10
 *     desc   :
 * </pre>
 */
public class MyLoginReq extends BaseReq<MyLoginResp> {
    private final String account;
    private final String pwd;

    public MyLoginReq(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    @Override
    public String path() {
        return "/mytio/mylogin.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("account", account)
                .append("pwd", pwd);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<MyLoginResp>>() {
        }.getType();
    }
}
