package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// 冒险模式下英语的答题界面
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class AdventureModeEnglishProblem extends AppCompatActivity implements View.OnClickListener{
    TextView tv_english_A, tv_english_B, tv_english_C, tv_english_D;
    TextView tv_english_problem;
    ImageButton img_btn_english_back;
    // 数据类
    String A, B, C, D, Problem;
    String answer;
    Boolean isGot; // 是否已经获得题目

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
        // 删除
        isGot = true;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_english_back) {
            if (isGot) { // 此处设置了这个应该问题不大把
                // 直接销毁本活动,回到"AdventureModeEnglishInit"界面
                finish();
            }
        }
        else if (v.getId() == R.id.tv_english_A) {
            if (isGot) { // 只有当获得题目了之后按击这个才有效
                if (answer.equals("A")) {
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"Math"信息
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
                if (answer.equals("A")) {
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"Math"信息
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
                if (answer.equals("A")) {
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"Math"信息
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
                if (answer.equals("A")) {
                    // 若与答案相同,则跳转到"adventure_mode_correct"
                    Intent intent = new Intent(this, AdventureModeCorrect.class);
                    Bundle DataBundle = new Bundle();
                    DataBundle.putString("MathOrEnglish", "English"); // 携带"Math"信息
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