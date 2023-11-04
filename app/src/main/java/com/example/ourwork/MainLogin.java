package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

// 网络通信
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import android.util.Log;

// 线程通信
import java.util.concurrent.CountDownLatch;

import me.jessyan.autosize.internal.CustomAdapt;

public class MainLogin extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    Button btn_login_next;
    ImageView iv_login_back;
    TextView tv_login_back;
    EditText et_login_username;
    EditText et_login_password;
    TextView tv_login_to_register;
    String username;
    String password;
    CountDownLatch latch; // 主线程强制等待子线程
    String uid; // 存储uid
    boolean loginSuccess; // 登录是否成功

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 400;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        // find
        btn_login_next = findViewById(R.id.btn_login_next);
        iv_login_back = findViewById(R.id.iv_login_back);
        tv_login_back = findViewById(R.id.tv_login_back);
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        tv_login_to_register = findViewById(R.id.tv_login_to_register);
        // 设置监听器
        btn_login_next.setOnClickListener(this);
        iv_login_back.setOnClickListener(this);
        tv_login_back.setOnClickListener(this);
        tv_login_to_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login_next) {
            // 获得用户名和密码
            username = et_login_username.getText().toString(); // 使用全局变量
            password = et_login_password.getText().toString(); // 使用全局变量
            if (username.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "用户名为空", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else if (password.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "密码为空", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else {
                // 网络编程部分
                // 涉及网络编程 注册的问题了
                latch = new CountDownLatch(1); // 每次都重新new
                Thread RegistrationVerificationThread = new Thread(new MainLogin.RegistrationVerification()); // 创建线程
                RegistrationVerificationThread.start(); // 启动线程
                try {
                    latch.await(); // 主线程等待子线程结束
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 等待结束
                if (loginSuccess) { // 成功注册
                    // 去修改user_all文件
                    SharedPreferences user_all = getSharedPreferences("user_all", MODE_PRIVATE); // 获得共享文件
                    String isOwn = user_all.getString(username, "");
                    if (isOwn.equals("")) {
                        // 只有在此情形表示 在本机第一次登录 那么就需要去写下列文件
                        SharedPreferences.Editor all_edit = user_all.edit();
                        all_edit.putString(username, password);
                        // all_edit.commit(); // 使用commit进行提交
                        all_edit.apply(); // 使用apply进行提交

                        // 去修改user_info文件
                        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
                        int user_num = user_info.getInt("user_num", 0); // 这个地方按理是不会出现找不到的情形的
                        user_num ++; // 自加 表示本地信息多了一个用户
                        SharedPreferences.Editor edit = user_info.edit();
                        edit.putInt("user_num", user_num); // user_num进行修改
                        edit.putString("now_user", username); // 修改为当前用户
                        // edit.commit(); // 使用commit进行提交
                        edit.apply(); // 使用apply进行提交

                        // 去修改user_to_num文件
                        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE); // 获得共享文件
                        SharedPreferences.Editor user_to_num_edit = user_to_num.edit();
                        user_to_num_edit.putInt(username, user_num);
                        // all_edit.commit(); // 使用commit进行提交
                        user_to_num_edit.apply(); // 使用apply进行提交

                        // 创建这个用户的个人文件夹用于存放头像
                        createPersonalFile(user_num);

                        // 创建个人共享文件
                        createPersonalShare(user_num);
                    }

                    //无论何种情形 只要登录成功 自动登录和当前用户名都要更新 // note:important
                    // 去修改user_info文件
                    SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
                    SharedPreferences.Editor edit = user_info.edit();
                    edit.putString("now_user", username); // 修改为当前用户
                    edit.putString("auto_login", "true"); // 设置为自动登录
                    edit.apply(); // 使用apply进行提交

                    // 跳转界面
                    Intent intent = new Intent(this, ModeChoose.class);
                    startActivity(intent);
                    finish(); // 及时销毁本活动
                }
            }
        }
        else if (v.getId() == R.id.iv_login_back) {
            // 直接销毁本活动
            finish();
        }
        else if (v.getId() == R.id.tv_login_back) {
            // 直接销毁本活动
            finish();
        }
        else if (v.getId() == R.id.tv_login_to_register) {
            // 跳转到注册界面
            Intent intent = new Intent(this, MainRegister.class);
            startActivity(intent);
            finish(); // 注意不销毁本活动 因为存在可能不小心点到这个按钮的  //  -> 还是销毁了好
        }
    }

    public void createPersonalFile(int user_num) {
        // 该文件夹用于存储图片
        // 在documents文件夹下
        File path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (path != null) {
            String folderName = String.valueOf(user_num); // 使用数字来标识用户
            File folder = new File(path, folderName);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    // 文件夹创建成功
                    Log.d("create files", "success");
                } else {
                    // 文件夹创建失败
                    Log.d("create files", "fail");
                }
            }
        }
    }

    public void createPersonalShare(int user_num) {
        String user_personal = "user_personal" + String.valueOf(user_num);
        Log.d("user_personal", user_personal);
        SharedPreferences share = getSharedPreferences(user_personal, MODE_PRIVATE); // 获得共享文件()
        SharedPreferences.Editor share_edit = share.edit();
        // 初始化内容
        share_edit.putInt("Math", 0);
        share_edit.putInt("MathRight", 0);
        share_edit.putInt("English", 0);
        share_edit.putInt("EnglishRight", 0);
        share_edit.putString("uid", uid); // 此处使用了网络编程获取uid
        // all_edit.commit(); // 使用commit进行提交
        share_edit.apply(); // 使用apply进行提交
    }

    public class RegistrationVerification implements Runnable {
        // 重写run函数
        @Override
        public void run() {
            String url = "http://47.120.38.204:100/login"; // 远程服务器 IP地址 + 端口号 + 路由
            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                // 设置请求方法为POST
                connection.setRequestMethod("POST");
                // 设置发送JSON数据的请求属性
                connection.setRequestProperty("Content-Type", "application/json");
                // 启用输入/输出流
                connection.setDoOutput(true);
                // 要发送的JSON数据
                String jsonInputString = "{" + "\"username\":\"" + username + "\"" + "," + "\"password\":\"" + password + "\"" + "}";
                // 将JSON数据写入连接的输出流
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                // 获取响应码
                int responseCode = connection.getResponseCode();
                System.out.println("响应码: " + responseCode);
                // 读取响应
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // 打印响应内容
                    System.out.println("响应体: " + response.toString());
                    String data = response.toString();
                    String state = "";

                    int l = 0, r = 0;
                    int num = 0;
                    for (int i = 0; i < data.length(); i++) {
                        if (data.charAt(i) == ':') {
                            l = i + 3;
                        }
                        else if (data.charAt(i) == ',' || data.charAt(i) == '}') {
                            r = i - 1;
                            String subString = data.substring(l, r);
                            Log.d("state", subString);
                            if (num == 0) {
                                state = subString;
                            }
                            else if (num == 1) {
                                uid = subString;
                            }
                            num ++;
                        }
                    }

                    if (state.equals("true")) {
                        loginSuccess = true; // 修改此处为true;
                        System.out.println("come here");
                    }
                    else {
                        // 调用runOnUiThread函数实现主线程弹出提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(MainLogin.this, "用户名或密码错误", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                toast.show();
                            }
                        });
                    }
                }
                // 关闭连接
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown(); // 锁 自减
        }
    }

}
/*
note
1、v.getId() == R.id.tv_login_to_register时 没有finish
* */