package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

// 数学模式下的答题界面
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.view.Gravity;
import android.util.Log;
import java.util.Random;
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
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import me.jessyan.autosize.internal.CustomAdapt;

public class AdventureModeMathProblem extends AppCompatActivity implements View.OnClickListener, CustomAdapt{
    // 按钮类
    Button btn_math_num0, btn_math_num1, btn_math_num2, btn_math_num3, btn_math_num4;
    Button btn_math_num5, btn_math_num6, btn_math_num7, btn_math_num8, btn_math_num9;
    Button btn_math_sub, btn_math_dot, btn_math_commit;
    ImageButton img_btn_math_back, img_btn_math_clear;

    // textview类
    TextView tv_math_problem;
    TextView tv_math_user_ans;

    // 数据类
    String problem, answer; // 存题目和答案
    StringBuffer user_ans;
    boolean isGot; // 是否已经获得了题目
    String TheType; // 模式->从上一个活动传递的信息中进行解析
    String path; // 网络编程获取远程的路径
    int ProblemNum; // 获取远程的题号
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
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_mode_math_problem);
        // 设置初始值
        problem = "";
        answer = "";
        user_ans = new StringBuffer(); // 建立一个空的StringBuffer对象
        isGot = false; // 还没有收到题目
        TheType = "";

        // 从上一个活动中进行解析
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                TheType = data.getString("type");
            }
        }

        // find textview & 设置初始值
        tv_math_problem = findViewById(R.id.tv_math_problem);
        tv_math_user_ans = findViewById(R.id.tv_math_user_ans);
        tv_math_problem.setText(""); // 初始值设为空
        tv_math_user_ans.setText(""); // 初始值设为空

        /*网络编程 设置题目 to do*/
        MathQAGet();

        // find button
        img_btn_math_back = findViewById(R.id.img_btn_math_back);
        btn_math_num0 = findViewById(R.id.btn_math_num0);
        btn_math_num1 = findViewById(R.id.btn_math_num1);
        btn_math_num2 = findViewById(R.id.btn_math_num2);
        btn_math_num3 = findViewById(R.id.btn_math_num3);
        btn_math_num4 = findViewById(R.id.btn_math_num4);
        btn_math_num5 = findViewById(R.id.btn_math_num5);
        btn_math_num6 = findViewById(R.id.btn_math_num6);
        btn_math_num7 = findViewById(R.id.btn_math_num7);
        btn_math_num8 = findViewById(R.id.btn_math_num8);
        btn_math_num9 = findViewById(R.id.btn_math_num9);
        btn_math_sub = findViewById(R.id.btn_math_sub);
        btn_math_commit = findViewById(R.id.btn_math_commit);
        btn_math_dot = findViewById(R.id.btn_math_dot);
        img_btn_math_clear = findViewById(R.id.img_btn_math_clear);
        // 设置监听器
        img_btn_math_back.setOnClickListener(this);
        btn_math_num0.setOnClickListener(this);
        btn_math_num1.setOnClickListener(this);
        btn_math_num2.setOnClickListener(this);
        btn_math_num3.setOnClickListener(this);
        btn_math_num4.setOnClickListener(this);
        btn_math_num5.setOnClickListener(this);
        btn_math_num6.setOnClickListener(this);
        btn_math_num7.setOnClickListener(this);
        btn_math_num8.setOnClickListener(this);
        btn_math_num9.setOnClickListener(this);
        btn_math_sub.setOnClickListener(this);
        btn_math_commit.setOnClickListener(this);
        btn_math_dot.setOnClickListener(this);
        img_btn_math_clear.setOnClickListener(this);
    }

    public void MathQAGet() {
        // 初始化读取文件路径 & 题号
        PathAndNumberInit();
        // 涉及网络编程 注册的问题了
        latch = new CountDownLatch(1); // 每次都重新new
        Thread RegistrationVerificationThread = new Thread(new AdventureModeMathProblem.RegistrationVerification()); // 创建线程
        RegistrationVerificationThread.start(); // 启动线程
        try {
            latch.await(); // 主线程等待子线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 标识获得了题目
        isGot = true;
    }

    public void PathAndNumberInit() {
        if (TheType.equals("add")) {
            long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
            Random random = new Random(seed);
            ProblemNum = random.nextInt(100 - 1 + 1) + 1; // 生成1-100的随机数 将其赋给题号
            int Mode = random.nextInt(3 - 1 + 1) + 1; // 生成1-3的随机数
            if (Mode == 1) {
                path = "add_decimal";
            }
            else if (Mode == 2) {
                path = "add_under_100";
            }
            else if (Mode == 3) {
                path = "add_under_0";
            }
        }
        else if (TheType.equals("sub")) {
            long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
            Random random = new Random(seed);
            ProblemNum = random.nextInt(100 - 1 + 1) + 1; // 生成1-100的随机数 将其赋给题号
            int Mode = random.nextInt(3 - 1 + 1) + 1; // 生成1-3的随机数
            if (Mode == 1) {
                path = "sub_decimal";
            }
            else if (Mode == 2) {
                path = "sub_under_100";
            }
            else if (Mode == 3) {
                path = "sub_under_0";
            }
        }
        else if (TheType.equals("mul")) {
            long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
            Random random = new Random(seed);
            int Mode = random.nextInt(3 - 1 + 1) + 1; // 生成1-3的随机数
            if (Mode == 1) {
                path = "mul_easy";
                ProblemNum = random.nextInt(30 - 1 + 1) + 1; // 生成1-30的随机数
            }
            else if (Mode == 2) {
                path = "mul_hard";
                ProblemNum= random.nextInt(30 - 1 + 1) + 1; // 生成1-30的随机数
            }
            else if (Mode == 3) {
                path = "nine_to_nine";
                ProblemNum = random.nextInt(81 - 1 + 1) + 1; // 生成1-81的随机数
            }
        }
    }

    public class RegistrationVerification implements Runnable {
        // 重写run函数
        @Override
        public void run() {
            String url = "http://47.120.38.204:110/math"; // 远程服务器 IP地址 + 端口号 + 路由
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
                String jsonInputString = "{" + "\"path\":\"" + path + "\"" + "," + "\"number\":\"" + String.valueOf(ProblemNum) + "\"" + "}";
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
                                problem = subString;
                            }
                            else if (num == 1) {
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
        if (v.getId() == R.id.img_btn_math_back) {
            // 自定义返回键 直接返回
            finish();
        }
        else {
            if (isGot) {
                /*只有在获得了题目的情况下 按击这些按钮才能生效*/
                if (v.getId() == R.id.btn_math_num0) {
                    // 按键0
                    // Log.d("test", "0");
                    user_ans.append('0');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num1) {
                    // 按键1
                    user_ans.append('1');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num2) {
                    // 按键2
                    user_ans.append('2');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num3) {
                    // 按键3
                    user_ans.append('3');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num4) {
                    // 按键4
                    user_ans.append('4');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num5) {
                    // 按键5
                    user_ans.append('5');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num6) {
                    // 按键6
                    user_ans.append('6');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num7) {
                    // 按键7
                    user_ans.append('7');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num8) {
                    // 按键8
                    user_ans.append('8');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_num9) {
                    // 按键9
                    user_ans.append('9');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_dot) {
                    // 按键'.'
                    user_ans.append('.');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_sub) {
                    // 按键 '-'
                    user_ans.append('-');
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.img_btn_math_clear) {
                    // 按键delete->删除最后一个字符
                    int len = user_ans.length();
                    if (len > 0) { // 避免出错
                        user_ans.deleteCharAt(len - 1);
                    }
                    tv_math_user_ans.setText(user_ans.toString());
                } else if (v.getId() == R.id.btn_math_commit) {
                    // 提交 检查答案
                    String use_ans_string = user_ans.toString(); // 转成String
                    if (use_ans_string.equals(answer)) {
                        // 若与答案相同,则跳转到"adventure_mode_correct"
                        Intent intent = new Intent(this, AdventureModeCorrect.class);
                        Bundle DataBundle = new Bundle();
                        DataBundle.putString("MathOrEnglish", "Math"); // 携带"Math"信息
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

}

/*
1、仅在onCreate中网络编程，故而答对时需要销毁本活动finish()
* */