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

// 网络编程库

public class AdventureModeMathProblem extends AppCompatActivity implements View.OnClickListener {
    // 按钮类
    Button btn_math_num0, btn_math_num1, btn_math_num2, btn_math_num3, btn_math_num4;
    Button btn_math_num5, btn_math_num6, btn_math_num7, btn_math_num8, btn_math_num9;
    Button btn_math_sub, btn_math_dot, btn_math_commit;
    ImageButton img_btn_math_back, img_btn_math_clear;

    // textview类
    TextView tv_math_problem;
    TextView tv_math_user_ans;

    // 数据类
    String problem, answer;
    StringBuffer user_ans;
    boolean isGot; // 是否已经获得了题目

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
        // find textview & 设置初始值
        tv_math_problem = findViewById(R.id.tv_math_problem);
        tv_math_user_ans = findViewById(R.id.tv_math_user_ans);
        tv_math_problem.setText(""); // 初始值设为空
        tv_math_user_ans.setText(""); // 初始值设为空
        /*网络编程 设置题目 to do*/
        // 删除呀
        isGot = true;
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

    @Override
    public void onClick(View v) {
        if (isGot) {
            /*只有在获得了题目的情况下 按击这些按钮才能生效*/
            if (v.getId() == R.id.img_btn_math_back) {
                // 返回键->直接销毁即可 回到"AdventureModeMathInit"界面
                finish();
            }
            else if (v.getId() == R.id.btn_math_num0) {
                // 按键0
                // Log.d("test", "0");
                user_ans.append('0');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num1) {
                // 按键1
                user_ans.append('1');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num2) {
                // 按键2
                user_ans.append('2');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num3) {
                // 按键3
                user_ans.append('3');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num4) {
                // 按键4
                user_ans.append('4');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num5) {
                // 按键5
                user_ans.append('5');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num6) {
                // 按键6
                user_ans.append('6');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num7) {
                // 按键7
                user_ans.append('7');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num8) {
                // 按键8
                user_ans.append('8');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_num9) {
                // 按键9
                user_ans.append('9');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_dot) {
                // 按键'.'
                user_ans.append('.');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_sub) {
                // 按键 '-'
                user_ans.append('-');
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.img_btn_math_clear) {
                // 按键delete->删除最后一个字符
                int len = user_ans.length();
                if (len > 0) { // 避免出错
                    user_ans.deleteCharAt(len - 1);
                }
                tv_math_user_ans.setText(user_ans.toString());
            }
            else if (v.getId() == R.id.btn_math_commit) {
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