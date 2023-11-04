package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;


// 用户中心界面
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.jessyan.autosize.internal.CustomAdapt;
// 网络通信
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import android.util.Log;
public class MainPersonalCenter extends AppCompatActivity implements View.OnClickListener, CustomAdapt{
    ImageView avater; // 头像
    TextView nickname; // 昵称
    TextView uid; // 账号
    TextView Math;
    TextView MathRight;
    TextView English;
    TextView EnglishRight;
    TextView modify; // 修改资料
    TextView help; // 帮助说明
    TextView password_modify; // 修改密码
    TextView exit_login; // 退出登录
    String now_username; // 目前的用户名
    String NextPassword;

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
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal_center);
        // find
        avater = findViewById(R.id.iv_user_avatar); // 头像
        nickname = findViewById(R.id.tv_user_yonghuxingming); // 昵称
        uid = findViewById(R.id.tv_user_dengruyonghu); // uid
        Math = findViewById(R.id.tv_user_math_sum1); // 数学总数
        MathRight = findViewById(R.id.tv_user_math_right2); // 数学答对
        English = findViewById(R.id.tv_user_english_sum1); // 英语总数
        EnglishRight = findViewById(R.id.tv_user_english_right2); // 英语答对

        modify = findViewById(R.id.iv_user_modify);
        help = findViewById(R.id.tv_user_help);
        password_modify = findViewById(R.id.tv_user_password_modify);
        exit_login = findViewById(R.id.tv_user_exit_login);

        // 数据初始化
        // note:仅仅测试时注释
        InitFromFiles();

        // 设置监听器
        modify.setOnClickListener(this);
        help.setOnClickListener(this);
        password_modify.setOnClickListener(this);
        exit_login.setOnClickListener(this);
    }

    // 从用户文件获取信息
    public void InitFromFiles() {
        // 获取当前用户username
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
        now_username = user_info.getString("now_user", ""); // 使用数据成员
        // 获取当前用户的编号
        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
        int user_num = user_to_num.getInt(now_username, 0);
        // 获取他的共享文件名
        String personalFile = "user_personal" + String.valueOf(user_num);
        // 打开它的共享文件
        SharedPreferences personal = getSharedPreferences(personalFile, MODE_PRIVATE);
        // 从共享文件中获得参数
        int MathData = personal.getInt("Math", 0);
        int EnglishData = personal.getInt("English", 0);
        int MathRightData = personal.getInt("MathRight", 0);
        int EnglishRightData = personal.getInt("EnglishRight", 0);

        // 设置这些内容
        Math.setText(String.valueOf(MathData));
        English.setText(String.valueOf(EnglishData));
        MathRight.setText(String.valueOf(MathRightData));
        EnglishRight.setText(String.valueOf(EnglishRightData));

        // 从个人文件夹中获取图片
        // 获取头像
        File rootDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), String.valueOf(user_num));
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }

        File file = new File(rootDirectory, "avatar.jpg");
        String PicturePath = file.toString();
        if(file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(PicturePath);
            if (bitmap != null) {
                avater.setImageBitmap(bitmap);
            }
            else {
                Log.d("personal", "decodefail");
            }
        }
        else {
            Log.d("personal", "file not exist");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_user_modify) {
            // 跳转到修改界面
            Intent intent = new Intent(this, MainModify.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.tv_user_help) {
            // 跳转到帮助中心界面
            Intent intent = new Intent(this, MainHelp.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.tv_user_password_modify) {
            // 修改密码
            password_modify_fun();
        }
        else if(v.getId() == R.id.tv_user_exit_login) {
            // 修改user_info信息
            SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
            SharedPreferences.Editor edit = user_info.edit();
            edit.putBoolean("auto_login", false);
            edit.putString("now_user", "");
            edit.apply();
            // 删除所有的活动
            Intent intent = new Intent(this, MainWelcome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置返回到最开始的界面
            startActivity(intent);
            finish(); // 此处应该是不需要使用finish 因为有FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    public void password_modify_fun() {
        // 创建一个线性布局来包含两个 EditText 控件
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // 第一个 EditText，用于输入原密码
        EditText newPasswordEditText = new EditText(this);
        newPasswordEditText.setHint("请输入原密码");
        layout.addView(newPasswordEditText);

        // 第二个 EditText，用于输入重设密码
        EditText confirmPasswordEditText = new EditText(this);
        confirmPasswordEditText.setHint("请重设新密码");
        layout.addView(confirmPasswordEditText);

        // 第三个 EditText，用于再次确认密码
        EditText confirmPasswordEditText1 = new EditText(this);
        confirmPasswordEditText1.setHint("再次确认密码");
        layout.addView(confirmPasswordEditText1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("重设密码");

        // 设置对话框的自定义视图为包含两个 EditText 的布局
        builder.setView(layout);

        // 设置确定按钮动作
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String confirmPassword1 = confirmPasswordEditText1.getText().toString();
                // 在这里处理用户输入的密码信息
                setNewPassword(newPassword, confirmPassword, confirmPassword1);
            }

            public void setNewPassword(String newPassword, String confirmPassword, String confirmPassword1) {
                SharedPreferences user_all = getSharedPreferences("user_all", MODE_PRIVATE);
                String origin = user_all.getString(now_username, "");
                if (newPassword.equals(origin)) {
                    // 原密码输入正确
                    if (confirmPassword.equals(confirmPassword1)) {
                        if (confirmPassword.equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "重置密码为空", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                            toast.show();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "重设密码成功", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                            toast.show();
                            // 修改相应文件
                            // 修改user_all文件
                            SharedPreferences.Editor user_all_edit = user_all.edit();
                            user_all_edit.putString(now_username, confirmPassword);
                            user_all_edit.apply();
                            // 还要网络编程修改密码
                            NextPassword = confirmPassword;
                            Thread RegistrationVerificationThread = new Thread(new MainPersonalCenter.RegistrationVerification()); // 创建线程
                            RegistrationVerificationThread.start(); // 启动线程
                        }
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "重设密码两次不同", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "原密码输入错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        });

        // 设置取消按钮动作
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // note: to do 网络编程 // 修改密码
    public class RegistrationVerification implements Runnable {
        // 重写run函数
        @Override
        public void run() {
            String url = "http://47.120.38.204:102/ModifyPassword"; // 远程服务器 IP地址 + 端口号 + 路由
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
                String jsonInputString = "{" + "\"username\":\"" + now_username + "\"" + "," + "\"password\":\"" + NextPassword + "\"" + "}";
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

                        System.out.println("come here");
                    }
                    else {
                        // 调用runOnUiThread函数实现主线程弹出提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(MainPersonalCenter.this, "用户名已存在", Toast.LENGTH_SHORT);
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
        }
    }
}