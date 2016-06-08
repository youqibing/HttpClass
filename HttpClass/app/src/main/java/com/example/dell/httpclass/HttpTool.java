package com.example.dell.httpclass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by dell on 2016/6/8.
 * 创建一个HttpTool类，封装了HttpConnection的GET和POST方法，sendGetRequest设置为静态方法是为了避免调用该的时候实例化，
 * 传入HttpCallBackListener对象是为了方法回调，回调接口中的方法，因为网络请求比较耗时，一般在子线程中进行，为了获得服务
 * 器返回的数据，需要用到JAVA的回调机制。
 */

/**
 * Get方法
 */

public class HttpTool {
    public static void sendGetRequest(final String UrlGet, final Map<String,String>Params,
                                      final Map<String,String>Headers, final HttpCallBackListener callBackListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    StringBuilder buffer = new StringBuilder(UrlGet);
                    Set<Map.Entry<String,String>> entrys = null;


                    //第一部分，如果是GET请求，参数在URL中。
                    if(Params!=null&&Params.isEmpty()){
                        buffer.append("?");
                        entrys = Params.entrySet();
                        for(Map.Entry<String,String> entry:entrys){
                            buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8")).append("&");
                        }
                        buffer.deleteCharAt(buffer.length()-1);
                    }
                    URL url= new URL(buffer.toString());    //将URL连带参数一并传进去。
                    connection =(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    //这一部分是获取服务器传给我们的数据的
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder("");
                    String readLine ="";

                    while((readLine=reader.readLine())!=null){
                            response.append(readLine);
                    }

                    if(callBackListener!=null){                         //如果传入了callListener,那么肯定是为了得到结果的，回到OnResponse方法
                        callBackListener.onRespone(response.toString());
                    }

                    //这部分是设置Headers
                    if(Headers!=null&&!Headers.isEmpty()){
                        entrys=Params.entrySet();
                        for(Map.Entry<String,String> entry : entrys){
                            connection.setRequestProperty(entry.getKey(),entry.getValue());
                        }
                    }

                }catch (Exception e){

                    if(callBackListener!=null){
                        callBackListener.onError(e);
                    }

                }finally {

                    if(callBackListener!=null){         //结果/错误处理完了关闭连接

                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            }
        });

    }

    /**
     * POST方法，UrlPost就是我们要传入的网址，Params是body,Headers就是Headers，最后一个参数是回调接口。
     */
    public static void sendPostRequest (final String UrlPost, final Map<String,String> Params,
                                        final Map<String,String> Headers,final HttpCallBackListener callBackListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection =null;
                try {
                    //第一部分，指定URL资源并建立链接
                    URL url = new URL(UrlPost);
                    connection =(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);


                    StringBuilder response = new StringBuilder("");
                    Set<Map.Entry<String,String>> entrys =null;

                    //第二部分，如果存在参数，则放在HTTP请求体，这段是Params参数，也就是body部分。
                    if(Params!=null&&!Params.isEmpty()){
                        entrys = Params.entrySet();
                        for(Map.Entry<String,String> entry : entrys){
                            response.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8")).append("&");
                        }
                    }
                    response.deleteCharAt(response.length()-1);    //删掉末尾最后一个“&”.

                    //设置headers部分，以键值对形式连接。
                    if(Headers!=null&&!Headers.isEmpty()){
                        entrys = Params.entrySet();
                        for(Map.Entry<String,String> entry:entrys){
                            connection.setRequestProperty(entry.getKey(),entry.getValue());
                        }
                    }


                    //下面这段主要是数据的上传以及读取服务器返回结果。
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(response.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    InputStream inputStream =connection.getInputStream();
                    InputStreamReader inputStremReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStremReader);

                    StringBuilder inputLine = new StringBuilder("");
                    String resultData = null;
                    while((resultData=bufferedReader.readLine())!=null){
                        inputLine.append(resultData);
                    }
                    //将服务器返回给我们的参数传给这个回调方法，静候用户处置。
                    if(callBackListener!=null){
                        callBackListener.onRespone(inputLine.toString());
                    }


                }catch (Exception e){
                    if(callBackListener!=null){
                        callBackListener.onError(e);
                    }

                }finally {
                    if(callBackListener!=null){
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }

                }
            }
        });

    }

}
