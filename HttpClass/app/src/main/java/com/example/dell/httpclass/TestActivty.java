package com.example.dell.httpclass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by dell on 2016/6/9.
 * 测试类，我们用封装好的HttpTool类发起一个GET请求。
 */

public class TestActivty extends Activity {

    private Button getbtn;
    private TextView responseTX;
    private Handler handler;
    private Bundle data;

    private static final int UPDATE_TEXT=1;
    private static final int ERROR=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        getbtn= (Button) findViewById(R.id.button);
        responseTX = (TextView)findViewById(R.id.textView2);

        handler = new Handler(){
          public void handleMessage(Message msg){
              switch (msg.what){
                  case UPDATE_TEXT:

                      Bundle getData =msg.getData();
                      String response = getData.getString("text");
                      Log.e("xxx","6");
                      responseTX.setText(response);
                      break;

                  case ERROR:
                      Toast.makeText(TestActivty.this,"啊哦，出错了",Toast.LENGTH_SHORT).show();

              }
          }
        };


        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Url = "https://www.baidu.com/";
                Log.e("xxx","1");

                HttpTool.sendGetRequest(Url, null, null, new HttpCallBackListener() {
                    @Override
                    public void onRespone(String response) {
                        Log.e("xxx","xxx");
                        Log.e("xxx",response);

                        Message message = new Message();
                        data = new Bundle();
                        data.putString("text",response);
                        message.what = UPDATE_TEXT;
                        message.setData(data);

                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Exception e) {

                        Message message = new Message();
                        message.what = ERROR;

                        handler.sendMessage(message);
                    }
                });

            }
        });



    }
}

