package com.example.dell.httpclass;

/**
 * Created by dell on 2016/6/8.
 * 定义回调接口
 */

public interface HttpCallBackListener {

    void onRespone(String response);
    void onError (Exception e);

}
