package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.IOException;
import android.graphics.Bitmap; // 用于图片的显示
import android.content.Intent;
import android.util.Log;
import java.io.FileOutputStream;
import android.content.Context;
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

public class MainModify extends AppCompatActivity implements View.OnClickListener, CustomAdapt{
    int MY_PICK_IMAGE_REQUEST = 1;
    EditText et_usr;
    EditText et_sex;
    EditText et_phone;
    ImageView img;
    TextView tv_submit;
    TextView tv_touxiang;
    ImageView modify_back;
    String now_user;
    int num;
    String sex;
    String phone;
    CountDownLatch latch; // 主线程强制等待子线程
    Boolean usernameModifyCan;

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
        setContentView(R.layout.activity_main_modify);
        // find
        et_usr = findViewById(R.id.tv_rename_name);
        et_sex = findViewById(R.id.tv_rename_sex);
        et_phone = findViewById(R.id.tv_rename_dianhua);
        img = findViewById(R.id.iv_rename_avatar);
        tv_submit = findViewById(R.id.tv_rename_baocun);
        modify_back = findViewById(R.id.iv_rename_back); // 返回键
        tv_touxiang = findViewById(R.id.tv_touxiang);

        // 数据初始化
        usernameModifyCan = false;
        // 初始化部分内容
        Init();
        // Log.d("init", "da");

        // 设置监听器
        tv_submit.setOnClickListener(this);
        modify_back.setOnClickListener(this);
        tv_touxiang.setOnClickListener(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里定义您想要执行的操作
                // 例如，处理单击事件的逻辑
                Log.d("avator", "conme here");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, MY_PICK_IMAGE_REQUEST); // MY_PICK_IMAGE_REQUEST这个变量是自己设的
            }
        });
    }

    public void Init() {
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
        now_user = user_info.getString("now_user", "root"); // default设置为"root" 方便页面调试
        et_usr.setText(now_user); // 设置用户名
        // 获得该用户的编号
        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
        num = user_to_num.getInt(now_user, 0); // 使用类的数据成员
        // 根据编号取得个人共享文件
        String user_person = "user_personal" + String.valueOf(num);
        SharedPreferences user_personal = getSharedPreferences(user_person, MODE_PRIVATE);
        // 取出 sex & phone
        sex = user_personal.getString("sex", "");
        phone = user_personal.getString("phone", "");
        et_sex.setText(sex);
        et_phone.setText(phone);
        // 获取头像
        File rootDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), String.valueOf(num));
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }

        File file = new File(rootDirectory, "avatar.jpg");
        String PicturePath = file.toString();
        if(file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(PicturePath);
            if (bitmap != null) {
                img.setImageBitmap(bitmap);
            }
            else {
                Log.d("rename", "decodefail");
            }
        }
        else {
            Log.d("rename", "file not exist");
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_rename_back) {
            // 按倒返回键 直接结束
            finish();
        }
        else if (v.getId() == R.id.tv_rename_baocun) {
            // 按到保存键
            // 一条一条信息进行保存
            // 首先保存sex & phone
            ModifySexAndPhone(); // 这个是一定可以保存成功的
            String origin = now_user;
            String next = et_usr.getText().toString();
            if (origin.equals(next)) {
                // 如果没有修改用户名
                usernameModifyCan = true;
            }
            else {
                // 然后根据网络编程看是否更改
                latch = new CountDownLatch(1); // 每次都重新new
                Thread RegistrationVerificationThread = new Thread(new MainModify.RegistrationVerification()); // 创建线程
                RegistrationVerificationThread.start(); // 启动线程
                try {
                    latch.await(); // 主线程等待子线程结束
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ModifyUsername(usernameModifyCan);
            }
            // 根据这个信息确定是否保存成功
            if (usernameModifyCan) {
                Toast toast = Toast.makeText(MainModify.this, "保存成功", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            finish(); // 保存完后finih
        }
        else if (v.getId() == R.id.tv_touxiang) {
            // 按击头像 直接替换
            Log.d("avator", "conme here");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, MY_PICK_IMAGE_REQUEST); // MY_PICK_IMAGE_REQUEST这个变量是自己设的
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 父类构造
        super.onActivityResult(requestCode, resultCode, intent);
        // 后续逻辑
        if (requestCode == MY_PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null) {
            Uri selectedImageUri = intent.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                img.setImageBitmap(bitmap);
                // 同时还需要将这个bitmap存到私有存储空间中
                File rootDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), String.valueOf(num));
                if (!rootDirectory.exists()) {
                    rootDirectory.mkdirs();
                }

                File file = new File(rootDirectory, "avatar.jpg");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    // 将 Bitmap 保存为 JPEG 文件，这里使用覆盖模式
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Log.d("sucess", "suces");
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ModifySexAndPhone() {
        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
        int num = user_to_num.getInt(now_user, 0);
        // 根据编号取得个人共享文件
        String user_person = "user_personal" + String.valueOf(num);
        SharedPreferences user_personal = getSharedPreferences(user_person, MODE_PRIVATE);
        // 修改共享文件夹的sex&phone
        SharedPreferences.Editor edit = user_personal.edit();
        sex = et_sex.getText().toString();
        phone = et_phone.getText().toString();
        edit.putString("sex", sex);
        edit.putString("phone", phone);
        edit.apply();
    }

    public void ModifyUsername(boolean need) {
        if (need) {
            // 如果需要修改用户名
            // 需要修改user_all和user_to_num
            String origin_name = now_user;
            String next_name = et_usr.getText().toString();
            // 获取use_all共享文件
            SharedPreferences user_all = getSharedPreferences("user_all", MODE_PRIVATE);
            String password = user_all.getString(origin_name, "");
            SharedPreferences.Editor edit_user_all = user_all.edit();
            edit_user_all.remove(origin_name);
            edit_user_all.putString(next_name, password);
            edit_user_all.apply();
            // 获取user_to_num
            SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
            int user_num = user_to_num.getInt(origin_name, 0);
            SharedPreferences.Editor edit_user_to_num = user_to_num.edit();
            edit_user_to_num.remove(origin_name);
            edit_user_to_num.putInt(next_name, user_num);
            edit_user_to_num.apply();
        }
        else {
            return;
        }
    }

    public class RegistrationVerification implements Runnable {
        // 重写run函数
        @Override
        public void run() {
            String url = "http://47.120.38.204:104/modifyusername"; // 远程服务器 IP地址 + 端口号 + 路由
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
                String origin_name = now_user;
                String next_name = et_usr.getText().toString();
                String jsonInputString = "{" + "\"origin\":\"" + now_user + "\"" + "," + "\"next\":\"" + next_name + "\"" + "}";
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

                            }
                            num ++;
                        }
                    }

                    if (state.equals("true")) {
                        usernameModifyCan = true; // 修改此处为true;
                        System.out.println("come here");
                    }
                    else {
                        // 调用runOnUiThread函数实现主线程弹出提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(MainModify.this, "用户名已存在", Toast.LENGTH_SHORT);
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