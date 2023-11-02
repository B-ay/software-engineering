package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

/*挑战模式答题界面*/

// 导入需要的包
// 基础控件包
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

// 网络通信包
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
// 线程通信
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import android.content.pm.ActivityInfo;
import android.util.Log;

public class ChanllengeModeQuestion extends AppCompatActivity implements View.OnClickListener{

    Button btn_option_A, btn_option_B, btn_option_C, btn_option_D;
    TextView tv_chanllenge_problem;
    boolean QAGoted; // 网络交互结束
    String ans; // 答案
    String A, B, C, D; // A,B,C,D选项的文本
    String Problem; // 问题文本
    int number; // 题目数量
    int[] question_number = new int[10]; // 存储题号
    int HaveSolveCount; // 已经做了的题目数量
    boolean isAllRight; // 是否全对
    CountDownLatch latch; // 主线程强制等待子线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类 & layout初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_question); // 设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 从上一个界面中获取信息
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                number = dataBundle.getInt("number");
                for (int i = 1; i <= number; i++) {
                    question_number[i] = dataBundle.getInt(String.valueOf(i));
                }
            }
        }
        else { // 用于调试单个界面
            number = 2;
            for (int i = 1; i <= number; i++) {
                question_number[i] = i + 1;
            }
        }
        // 数据成员初始化
        isAllRight = true; // 全对
        // find相应组件
        btn_option_A = findViewById(R.id.btn_option_A);
        btn_option_B = findViewById(R.id.btn_option_B);
        btn_option_C = findViewById(R.id.btn_option_C);
        btn_option_D = findViewById(R.id.btn_option_D);
        tv_chanllenge_problem = findViewById(R.id.tv_chanllenge_problem);
        // 初始化(所有内容都为空,直到网络交互结束)
        btn_option_A.setText("");
        btn_option_B.setText("");
        btn_option_C.setText("");
        btn_option_D.setText("");
        tv_chanllenge_problem.setText("");
        // 全局变量初始化
        ans = "";
        A = "";
        B = "";
        C = "";
        D = "";
        Problem = "";
        QAGoted = false;
        // 网络编程获取题目
        QAGetByWeb();
        // 设置监听器
        btn_option_A.setOnClickListener(this);
        btn_option_B.setOnClickListener(this);
        btn_option_C.setOnClickListener(this);
        btn_option_D.setOnClickListener(this);

    }

    public void QAGetByWeb() {
        latch = new CountDownLatch(1); // 每次都重新new
        QAGet QA = new QAGet();
        Thread thread = new Thread(QA);
        thread.start(); // 启动线程
        Log.d("here", "get here");
        try {
            latch.await(); // 主线程等待子线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设置题面
        tv_chanllenge_problem.setText(Problem);
        btn_option_A.setText(A);
        btn_option_B.setText(B);
        btn_option_C.setText(C);
        btn_option_D.setText(D);
        QAGoted = true; // 标识获得了题目
    }

    public class QAGet implements Runnable {
        @Override
        public void run() {
            // 从HaveSolvedCount + 1中获得题目序号
            // 线程执行的代码
            String url = "http://47.120.38.204:103/qa"; // 远程服务器 IP地址 + 端口号 + 路由
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
//                String jsonInputString = "{" + "\"number\":\"" + "1\"" + "}";
                String jsonInputString = "{" + "\"number\":\"" + String.valueOf(question_number[HaveSolveCount + 1]) + "\"" + "}";
                System.out.println(jsonInputString);
                // 将JSON数据写入连接的输出流
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                // 获取响应码
                int responseCode = connection.getResponseCode();
                System.out.println("responseCode: " + responseCode);
                // 读取响应
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // 打印响应内容
                    System.out.println("body: " + response);
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
                            System.out.println(subString);
                            if (num == 0) {
                                Problem = subString;
                            }
                            else if (num == 1) {
                                A = subString;
                            }
                            else if (num == 2) {
                                B = subString;
                            }
                            else if (num == 3) {
                                C = subString;
                            }
                            else if (num == 4) {
                                D = subString;
                            }
                            else if (num == 5) {
                                ans = subString;
                            }
                            num++;
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
        if (v.getId() == R.id.btn_option_A) {
            if (QAGoted) { // 必须在获得题目之后按击才有效
                if (ans.equals("A")) {
                    Intent intent = new Intent(this, ChanllengeModeCorrect.class); // 跳转正确界面
                    startActivity(intent);
                }
                else {
                    isAllRight = false;
                    Intent intent = new Intent(this, ChanllengeModeWrong.class); // 跳转错误界面
                    Bundle DataBundle = new Bundle(); // 创建一个包
                    DataBundle.putString("ans", ans);
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                }
            }
        }
        else if (v.getId() == R.id.btn_option_B) {
            if (QAGoted) { // 必须在获得题目之后按击才有效
                if (ans.equals("B")) {
                    Intent intent = new Intent(this, ChanllengeModeCorrect.class); // 跳转正确界面
                    startActivity(intent);
                }
                else {
                    isAllRight = false;
                    Intent intent = new Intent(this, ChanllengeModeWrong.class); // 跳转错误界面
                    Bundle DataBundle = new Bundle(); // 创建一个包
                    DataBundle.putString("ans", ans);
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                }
            }
        }
        else if (v.getId() == R.id.btn_option_C) {
            if (QAGoted) { // 必须在获得题目之后按击才有效
                if (ans.equals("C")) {
                    Intent intent = new Intent(this, ChanllengeModeCorrect.class); // 跳转正确界面
                    startActivity(intent);
                }
                else {
                    isAllRight = false;
                    Intent intent = new Intent(this, ChanllengeModeWrong.class); // 跳转错误界面
                    Bundle DataBundle = new Bundle(); // 创建一个包
                    DataBundle.putString("ans", ans);
                    intent.putExtras(DataBundle);
                    startActivity(intent);
                }
            }
        }
        else if (v.getId() == R.id.btn_option_D) {
            if (QAGoted) { // 必须在获得题目之后按击才有效
                if (ans.equals("D")) {
                    Intent intent = new Intent(this, ChanllengeModeCorrect.class); // 跳转正确界面
                    startActivityForResult(intent, 0);
                }
                else {
                    isAllRight = false;
                    Intent intent = new Intent(this, ChanllengeModeWrong.class); // 跳转错误界面
                    Bundle DataBundle = new Bundle(); // 创建一个包
                    DataBundle.putString("ans", ans);
                    intent.putExtras(DataBundle);
                    startActivityForResult(intent, 0);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Log.d("result", "reslut");
            HaveSolveCount ++;
            if (HaveSolveCount == number) {
                // 答完了所有题目
                Intent in = new Intent(this, ChanllegeModeMap.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置返回到原先的map界面
                // 建立Bundle进行信息传递
                Bundle DataBundle = new Bundle();
                if (isAllRight) {
                    DataBundle.putString("isAllRight", "Yes");
                }
                else {
                    DataBundle.putString("isAllRight", "No");
                }
                intent.putExtras(DataBundle); // 放入
                startActivity(in);
                finish(); // 此处应该是不需要使用finish 因为有FLAG_ACTIVITY_CLEAR_TOP
            }
            else {
                // 网络编程获得下一道题目
                QAGetByWeb();
                Log.d("repeat", "repeat");
            }
        }
    }


}