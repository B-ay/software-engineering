package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.os.Bundle;

// 冒险模式下英语的答题界面
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

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
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import me.jessyan.autosize.internal.CustomAdapt;

public class AdventureModeEnglishProblem extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    TextView tv_english_A, tv_english_B, tv_english_C, tv_english_D;
    TextView tv_english_problem;
    ImageButton img_btn_english_back;
    // 数据类
    String A, B, C, D, Problem;
    String answer;
    int ProbelmNumber; // 选择的题号
    Boolean isGot; // 是否已经获得题目
    CountDownLatch latch; // 主线程强制等待子线程

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
        // 父类构造 & layout初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_mode_english_problem);
        // 数据类初始化
        isGot = false; // 还没有获得题目
        A = "A.";
        B = "B.";
        C = "C.";
        D = "D.";
        Problem = "";
        answer = ""; // 答案也为空

        // 网络通信
        EnglishQAGet();

        // find
        tv_english_A = findViewById(R.id.tv_english_A);
        tv_english_B = findViewById(R.id.tv_english_B);
        tv_english_C = findViewById(R.id.tv_english_C);
        tv_english_D = findViewById(R.id.tv_english_D);
        tv_english_problem = findViewById(R.id.tv_english_problem);
        img_btn_english_back = findViewById(R.id.img_btn_english_back);
        // 初始化
        tv_english_A.setText(A);
        tv_english_B.setText(B);
        tv_english_C.setText(C);
        tv_english_D.setText(D);
        tv_english_problem.setText(Problem);
        // 设置监听器
        tv_english_A.setOnClickListener(this);
        tv_english_B.setOnClickListener(this);
        tv_english_C.setOnClickListener(this);
        tv_english_D.setOnClickListener(this);
        img_btn_english_back.setOnClickListener(this); // 返回按钮设置监听器
    }

    // 修改用户的英文答题数量
    public void EnglishModify() {
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = user_info.getString("now_user", "root");
        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
        int user_num = user_to_num.getInt(username, 0);
        String filename = "user_personal" + String.valueOf(user_num);
        SharedPreferences modify = getSharedPreferences(filename, MODE_PRIVATE);
        int origin = modify.getInt("English", 0);
        int nxt = origin + 1;
        SharedPreferences.Editor edit = modify.edit();
        edit.putInt("English", nxt);
        edit.apply();
    }

    // 修改用户的英文答题正确数量
    public void EnglishRightModify() {
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = user_info.getString("now_user", "root");
        SharedPreferences user_to_num = getSharedPreferences("user_to_num", MODE_PRIVATE);
        int user_num = user_to_num.getInt(username, 0);
        String filename = "user_personal" + String.valueOf(user_num);
        SharedPreferences modify = getSharedPreferences(filename, MODE_PRIVATE);
        int origin = modify.getInt("EnglishRight", 0);
        int nxt = origin + 1;
        SharedPreferences.Editor edit = modify.edit();
        edit.putInt("EnglishRight", nxt);
        edit.apply();
    }

    public void EnglishQAGet() {
        // ProbelmNumber初始化
        long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
        Random random = new Random(seed);
        ProbelmNumber = random.nextInt(300 - 1 + 1) + 1; // 生成1-300的随机数 将其赋给题号

        // 涉及网络编程 注册的问题了
        latch = new CountDownLatch(1); // 每次都重新new
        Thread RegistrationVerificationThread = new Thread(new AdventureModeEnglishProblem.RegistrationVerification()); // 创建线程
        RegistrationVerificationThread.start(); // 启动线程
        try {
            latch.await(); // 主线程等待子线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 标识获得了题目
        isGot = true;

        // 英语答题数量+1
        EnglishModify();
    }

    public class RegistrationVerification implements Runnable {
        // 重写run函数
        @Override
        public void run() {
            String url = "http://47.120.38.204:111/English"; // 远程服务器 IP地址 + 端口号 + 路由
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
                String jsonInputString = "{" + "\"number\":\"" + String.valueOf(ProbelmNumber) +  "\"" + "}";
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
                                Problem = subString;
                            }
                            else if (num == 1) {
                                A = "A. " + subString;
                            }
                            else if (num == 2) {
                                B = "B. " + subString;
                            }
                            else if (num == 3) {
                                C = "C. " + subString;
                            }
                            else if (num == 4) {
                                D = "D. " + subString;
                            }
                            else if (num == 5) {
                                answer = subString;
                            }
                            num ++;
                        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_english_back) {
            finish(); // 直接销毁
        }
        else if (v.getId() == R.id.tv_english_A) {
            if (isGot) { // 只有当获得题目了之后按击这个才有效
                if (answer.equals("A")) {
                    // 英语答题正确数量 + 1
                    EnglishRightModify();
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"English"信息
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                    finish(); // 销毁本活动
                }
                else {
                    // 答案错误,弹出toast
                    Toast toast = Toast.makeText(getApplicationContext(), "答案错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        }
        else if (v.getId() == R.id.tv_english_B) {
            if (isGot) {
                if (answer.equals("B")) {
                    // 英语答题正确数量 + 1
                    EnglishRightModify();
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"English"信息
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                    finish(); // 销毁本活动
                }
                else {
                    // 答案错误,弹出toast
                    Toast toast = Toast.makeText(getApplicationContext(), "答案错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        }
        else if (v.getId() == R.id.tv_english_C) {
            if (isGot) {
                if (answer.equals("C")) {
                    // 英语答题正确数量 + 1
                    EnglishRightModify();
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"English"信息
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                    finish(); // 销毁本活动
                }
                else {
                    // 答案错误,弹出toast
                    Toast toast = Toast.makeText(getApplicationContext(), "答案错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        }
        else if (v.getId() == R.id.tv_english_D) {
            if (isGot) {
                if (answer.equals("D")) {
                    // 英语答题正确数量 + 1
                    EnglishRightModify();
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"English"信息
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                    finish(); // 销毁本活动
                }
                else {
                    // 答案错误,弹出toast
                    Toast toast = Toast.makeText(getApplicationContext(), "答案错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        }
    }

}