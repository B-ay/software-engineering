package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// 数学模式下 准备答题
// 导入需要的库
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;

public class AdventureModeMathInit extends AppCompatActivity implements View.OnClickListener{
    Button btn_math_init_next;
    ImageButton img_btn_math_init_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_mode_math_init);
        // find
        btn_math_init_next = findViewById(R.id.btn_math_init_next);
        img_btn_math_init_back = findViewById(R.id.img_btn_math_init_back);
        // 设置监听器
        btn_math_init_next.setOnClickListener(this);
        img_btn_math_init_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_math_init_back) {
            // 自定义返回按钮
            finish(); // 直接销毁本活动即可
        }
        else if (v.getId() == R.id.btn_math_init_next) {
            Intent intent = new Intent(this, AdventureModeMathProblem.class);
            startActivity(intent);
            // 此时这个界面不销毁
            // finish();
        }
    }
}