package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// 冒险模式 选择数学还是英语
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import me.jessyan.autosize.internal.CustomAdapt;
public class AdventureModeChoose extends AppCompatActivity implements View.OnClickListener,CustomAdapt {
    Button btn_adventure_mode_choose_math;
    Button btn_adventure_mode_choose_english;
    ImageButton img_btn_adventure_mode_choose_back;
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
        setContentView(R.layout.activity_adventure_mode_choose);
        // find
        btn_adventure_mode_choose_math = findViewById(R.id.btn_adventure_mode_math);
        btn_adventure_mode_choose_english = findViewById(R.id.btn_adventure_mode_english);
        img_btn_adventure_mode_choose_back = findViewById(R.id.img_btn_adventure_mode_back);
        // 设置监听器
        btn_adventure_mode_choose_math.setOnClickListener(this);
        btn_adventure_mode_choose_english.setOnClickListener(this);
        img_btn_adventure_mode_choose_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_adventure_mode_math) {
            // 进入数学init (不需要销毁本活动)
            Intent intent = new Intent(this, AdventureModeMathInit.class);
            startActivity(intent);
      }
        else if (v.getId() == R.id.btn_adventure_mode_english) {
            // 进入英语init (不需要销毁本活动)
            Intent intent = new Intent(this, AdventureModeEnglishInit.class);
            startActivity(intent);
        }
        else if (v.getId()== R.id.img_btn_adventure_mode_back) {
            // 返回到 "选择挑战模式还是冒险模式的地方" 即 "ModeChoose"
            // 直接finish即可
            finish();
        }
    }

}