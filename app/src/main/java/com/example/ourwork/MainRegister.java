package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

// 需要导入的包
import android.os.Environment;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

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

public class MainRegister extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    Button btn_register_next;
    ImageView iv_register_back;
    TextView tv_register_back;
    EditText et_register_username;
    EditText et_register_password;
    EditText et_register_password_check;
    String username, password, password_check;
    boolean RegisterSuccessful; // 注册是否成功
    CountDownLatch latch; // 主线程强制等待子线程
    String uid; // 存储uid

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
        setContentView(R.layout.activity_main_register);
        // 数据类
        RegisterSuccessful = false;
        // find
        btn_register_next = findViewById(R.id.btn_register_next);
        iv_register_back = findViewById(R.id.iv_register_back);
        tv_register_back = findViewById(R.id.tv_register_back);
        et_register_username = findViewById(R.id.et_register_username);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_password_check = findViewById(R.id.et_register_password_check);
        // 设置监听器
        btn_register_next.setOnClickListener(this);
        iv_register_back.setOnClickListener(this);
        tv_register_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register_next) {
            // 获取三个信息
            username = et_register_username.getText().toString();
            password = et_register_password.getText().toString();
            password_check = et_register_password_check.getText().toString();
            if (username.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else if (password.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else if (password_check.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "请再次输入密码", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else {
                if (password.equals(password_check)) {
                    // 涉及网络编程 注册的问题了
                    latch = new CountDownLatch(1); // 每次都重新new
                    Thread RegistrationVerificationThread = new Thread(new RegistrationVerification()); // 创建线程
                    RegistrationVerificationThread.start(); // 启动线程
                    try {
                        latch.await(); // 主线程等待子线程结束
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 等待结束
                    if (RegisterSuccessful) { // 成功注册
                        // 去修改user_info文件
                        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
                        int user_num = user_info.getInt("user_num", 0); // 这个地方按理是不会出现找不到的情形的
                        user_num ++; // 自加 表示注册多了一个用户
                        SharedPreferences.Editor edit = user_info.edit();
                        edit.putInt("user_num", user_num); // user_num进行修改
                        edit.putString("now_user", username); // 修改为当前用户
                        edit.putString("auto_login", "true"); // 设置为自动登录
                        edit.commit(); // 使用commit进行提交
                        //edit.apply(); // 使用apply进行提交

                        // 去修改user_all文件
                        SharedPreferences user_all = getSharedPreferences("user_all", MODE_PRIVATE); // 获得共享文件
                        SharedPreferences.Editor all_edit = user_all.edit();
                        all_edit.putString(username, password);
                        // all_edit.commit(); // 使用commit进行提交
                        all_edit.apply(); // 使用apply进行提交

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

                        // 跳转界面
                        Intent intent = new Intent(this, ModeChoose.class);
                        startActivity(intent);
                        finish(); // 及时销毁本活动
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "两次密码不相同", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        }
        else if (v.getId() == R.id.iv_register_back) {
            // 直接销毁本活动
            finish();
        }
        else if (v.getId() == R.id.tv_register_back) {
            // 直接销毁本活动
            finish();
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
        SharedPreferences share = getSharedPreferences(user_personal, MODE_PRIVATE); // 获得共享文件
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
            String url = "http://47.120.38.204:101/register"; // 远程服务器 IP地址 + 端口号 + 路由
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
                        RegisterSuccessful = true; // 修改此处为true;
                        System.out.println("come here");
                    }
                    else {
                        // 调用runOnUiThread函数实现主线程弹出提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(MainRegister.this, "用户名已存在", Toast.LENGTH_SHORT);
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