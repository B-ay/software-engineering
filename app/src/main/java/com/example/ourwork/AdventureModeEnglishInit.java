package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// 冒险模式下英语准备答题
// 导入需要的库
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import me.jessyan.autosize.internal.CustomAdapt;
public class AdventureModeEnglishInit extends AppCompatActivity implements View.OnClickListener,CustomAdapt {
    Button btn_english_init_next;
    ImageButton img_btn_english_init_back;

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
        setContentView(R.layout.activity_adventure_mode_english_init);
        // find
        btn_english_init_next = findViewById(R.id.btn_english_init_next);
        img_btn_english_init_back =findViewById(R.id.img_btn_english_init_back);
        // 设置监听器
        btn_english_init_next.setOnClickListener(this);
        img_btn_english_init_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_english_init_back) {
            // 返回上一层活动 直接销毁 使用活动栈即可
            finish();
        }
        else if (v.getId() == R.id.btn_english_init_next) {
            Intent intent = new Intent(this, AdventureModeEnglishProblem.class);
            startActivity(intent);
            // finish();  // 不销毁此活动
        }
    }
}