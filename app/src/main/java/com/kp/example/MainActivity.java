package com.kp.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kp.core.GetJsonBsicTask;
import com.kp.core.ICallBack;
import com.kp.core.MultipartTask;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
        Content-Disposition: form-data; name=\"tag\"\r\n\r\nsign_in\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
        Content-Disposition: form-data; name=\"email\"\r\n\r\njeet\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
        Content-Disposition: form-data; name=\"password\"\r\n\r\n1234\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://ctp.aptoms.com/login.php")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "f7246f1c-60a5-fadd-2656-7256fe81e8f0")
                .build();

        Response response = client.newCall(request).execute();
        */
        Map<String,String> parameter = new HashMap<>();
        parameter.put("tag","sign_in");
        parameter.put("email","jeet");
        parameter.put("password","1234");

        new MultipartTask(this, "http://ctp.aptoms.com/login.php",parameter, new ICallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("onsuccess"," onSuccess result " + result);
            }
            @Override
            public void onFailure(Throwable throwable, String error) {
                Log.d("onsuccess","onFailure error " + error);
            }
        }).execute();
    }
}
