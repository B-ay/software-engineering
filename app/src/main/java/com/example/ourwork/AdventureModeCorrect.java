package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// 冒险模式下回答问题正确
import android.widget.ImageButton;
import android.view.View;
import me.jessyan.autosize.internal.CustomAdapt;

public class AdventureModeCorrect extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    ImageButton img_btn_adventure_mode_correct_right;
    ImageButton img_btn_adventure_mode_correct_back;
    String MathOrEnglish; // 目前的模式是数学还是英文
    String TheType; // 存储数学模式的add sub mul

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
        setContentView(R.layout.activity_adventure_mode_correct);
        // 从上一个界面传递过来的信息进行解析
        Intent GetIntent = getIntent();
        if  (GetIntent != null) {
            Bundle DataBundle = GetIntent.getExtras();
            if (DataBundle != null) {
                MathOrEnglish = DataBundle.getString("MathOrEnglish");
                if (MathOrEnglish.equals("Math")) {
                    TheType = DataBundle.getString("type");
                }
            }
        }
        // find
        img_btn_adventure_mode_correct_right = findViewById(R.id.img_btn_adventure_mode_correct_right);
        img_btn_adventure_mode_correct_back = findViewById(R.id.img_btn_adventure_mode_corect_back);
        // 设置监听器
        img_btn_adventure_mode_correct_back.setOnClickListener(this);
        img_btn_adventure_mode_correct_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_adventure_mode_corect_back) {
            // 注意此处的返回是直接finish 同时返回到的是开始答题界面
            finish();
        }
        else if (v.getId() == R.id.img_btn_adventure_mode_correct_right) {
            if (MathOrEnglish.equals("Math")) {
                // 处于数学模式
                Intent intent = new Intent(this, AdventureModeMathProblem.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("type", TheType);
                intent.putExtras(dataBundle); // 装入信息
                startActivity(intent);
                finish(); // 注意及时销毁
            }
            else if (MathOrEnglish.equals("English")) {
                // 处于英语模式
                Intent intent = new Intent(this, AdventureModeEnglishProblem.class);
                startActivity(intent);
                finish(); // 注意及时销毁
            }
        }
    }

}