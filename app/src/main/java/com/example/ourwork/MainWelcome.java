package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

// 第一个界面
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import org.w3c.dom.Text;

public class MainWelcome extends AppCompatActivity implements View.OnClickListener{
    Button btn_main_weclome_start;
    TextView tv_main_weclome_to_help;
    int user_num; // 已经存储的用户数量
    boolean auto_login; // 是否自动登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);

        // 使用SharedPreferences
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 不存在该文件时,会自动生成该文件
        user_num = user_info.getInt("user_num", -1); // 不存在该值的时候就是用defValue 即-1
        auto_login = user_info.getBoolean("auto_login", false); // 不存在该key时 使用defValue的值
        if (user_num == -1) { // 第一次创建这个文件时
            user_num = 0; // 同时将该值设为0
            SharedPreferences.Editor edit = user_info.edit();
            edit.putInt("user_num", 0); // 新建user_num
            edit.putBoolean("auto_login", false); // 新建auto_login
            edit.commit(); // 使用commit进行提交
        }

        // find & 设置监听器
        btn_main_weclome_start = findViewById(R.id.btn_main_welcome_start);
        tv_main_weclome_to_help = findViewById(R.id.tv_main_weclome_prompt);
        btn_main_weclome_start.setOnClickListener(this);
        tv_main_weclome_to_help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_main_welcome_start) {
            SharedPreferences use_info = getSharedPreferences("use_info", MODE_PRIVATE);
            auto_login = use_info.getBoolean("auto_login", false);
            if (auto_login) {
                // 如果选择了自动登录的模式
                // 此时一定user_num不是0
                // 则根据user_key去Get到user_name
                // 然后根据user_name去从远程拷贝数据到用户中心的存储文件中
                String user_key = "user_key" + String.valueOf(user_num); // 每次都是取最后一个注册的人
                String user_name = use_info.getString("user_key", ""); // 设置一个空的,以便服务端进行特判
                // 然后根据user_name去访问网络，获取数据 (其实也可以直接就去本地存储卡去获取)(主要是Personal Center到底设置什么的问题)
                // 确定了就是直接从本地进行获得
                // to do
                // 直接跳转到"ModeChoose"界面
                Intent intent = new Intent(this, ModeChoose.class);
                startActivity(intent);
                // 不销毁本活动
            }
            else {
                // 没有选择自动登录的模式
                // 直接进入login活动
                Intent intent = new Intent(this, MainLogin.class);
                startActivity(intent);
                // 不销毁本活动
            }
        }
        else if (v.getId() == R.id.tv_main_welcome_to_help) {
            // 进入帮助中心的界面
            Intent intent = new Intent(this, MainHelp.class);
            startActivity(intent);
            // 不销毁本活动
        }
    }

}