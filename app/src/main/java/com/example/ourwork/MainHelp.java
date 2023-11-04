package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// 帮助文档
import android.view.View;
import android.widget.ImageView;
import me.jessyan.autosize.internal.CustomAdapt;

public class MainHelp extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    ImageView iv_mainhelp_back;

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
        setContentView(R.layout.activity_main_help);
        // find
        iv_mainhelp_back = findViewById(R.id.iv_mainhelp_back);
        // 设置监听器
        iv_mainhelp_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_mainhelp_back) {
            // 直接销毁本活动
            finish();
        }
    }

}