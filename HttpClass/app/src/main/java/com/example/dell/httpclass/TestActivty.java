package com.example.dell.httpclass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/6/9.
 * 测试类，我们用封装好的HttpTool类发起一个GET请求。
 */

public class TestActivty extends Activity {

    private Button getbtn;
    private TextView responsetx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        getbtn= (Button) findViewById(R.id.button);
        responsetx = (TextView)findViewById(R.id.textView2);



        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> params = new HashMap<String,String>();
                params.put("plg_nld","1");
                params.put("plg_usr","1");
                params.put("plg_uin","1");
                params.put("plg_auth","1");
                params.put("webid","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1Ijo1NTA4NzkwLCJpZCI6NTIwMTcyOH0.X5lfGey8j0FA_joZvE0OuytydIp1OzvMiODhWqsM7CU");
                params.put("src","app");
                params.put("plg_dev","1");
                params.put("plg_vkey","1");
                params.put("from","groupmessage");
                params.put("isappinstalled","0");

                String Url = "maimai.cn/web/gossip_detail";

                HttpTool.sendGetRequest(Url, params, null, new HttpCallBackListener() {
                    @Override
                    public void onRespone(String response) {
                        responsetx.setText("response");
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(TestActivty.this,"啊哦，出错了",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }
}

