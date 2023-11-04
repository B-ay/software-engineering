package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

// 选择冒险模式还是挑战模式
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import me.jessyan.autosize.internal.CustomAdapt;

public class ModeChoose extends AppCompatActivity implements View.OnClickListener, CustomAdapt{
    Button btn_mode_choose_adventure;
    Button btn_mode_choose_chanllenge;
    ImageButton img_btn_mode_choose_back;
    ImageButton img_btn_mode_choose_to_home;
    ImageButton img_btn_mode_choose_to_personal_center;

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
        setContentView(R.layout.activity_mode_choose);
        // find
        btn_mode_choose_adventure = findViewById(R.id.btn_mode_choose_adventure);
        btn_mode_choose_chanllenge = findViewById(R.id.btn_mode_choose_chanllenge);
        img_btn_mode_choose_back = findViewById(R.id.img_btn_mode_choose_back);
        img_btn_mode_choose_to_home = findViewById(R.id.img_btn_mode_choose_to_home);
        img_btn_mode_choose_to_personal_center = findViewById(R.id.img_btn_mode_choose_to_personal_center);
        // 设置监听器
        btn_mode_choose_adventure.setOnClickListener(this);
        btn_mode_choose_adventure.setOnClickListener(this);
        img_btn_mode_choose_back.setOnClickListener(this);
        img_btn_mode_choose_to_home.setOnClickListener(this);
        img_btn_mode_choose_to_personal_center.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_mode_choose_adventure) {
            // 进入冒险模式(本活动不销毁) No
            Intent intent = new Intent(this, AdventureModeChoose.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.btn_mode_choose_chanllenge) {
            // 进入挑战模式(本活动不销毁) No
            Intent intent = new Intent(this, ChanllengeModePersonNumber.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.img_btn_mode_choose_back) {
            // 回到主页(销毁本活动) Yes
            // 直接finish
            finish();
        }
        else if (v.getId() == R.id.img_btn_mode_choose_to_home) {
            // 回到主页(销毁本活动) Yes
            // 直接finish
            finish();
        }
        else if (v.getId() == R.id.img_btn_mode_choose_to_personal_center) {
            // 进入个人中心(本活动不销毁) No
            Intent intent = new Intent(this, MainPersonalCenter.class);
            startActivity(intent);
        }
    }

}