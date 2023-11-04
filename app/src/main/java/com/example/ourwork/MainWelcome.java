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
import me.jessyan.autosize.internal.CustomAdapt;

// 总页面
public class MainWelcome extends AppCompatActivity implements View.OnClickListener, CustomAdapt {
    Button btn_main_weclome_start;
    TextView tv_main_weclome_to_help;

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
        setContentView(R.layout.activity_main_welcome);

        // 使用SharedPreferences
        SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 不存在该文件时,会自动生成该文件
        int user_num = user_info.getInt("user_num", -1); // 这样方便标记是这个软件第一次下载
        if (user_num == -1) {
            // 构建这个文件的信息
            SharedPreferences.Editor edit = user_info.edit();
            edit.putInt("user_num", 0); // 本地用户存储数量
            edit.putString("auto_login", "false"); // 是否自动登录
            edit.putString("now_user", ""); // 当前用户
            // edit.commit(); // 使用commit进行提交
            edit.apply(); // 使用apply进行提交
        }

        // find & 设置监听器
        btn_main_weclome_start = findViewById(R.id.btn_main_welcome_start);
        tv_main_weclome_to_help = findViewById(R.id.tv_main_welcome_to_help);
        btn_main_weclome_start.setOnClickListener(this);
        tv_main_weclome_to_help.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_main_welcome_start) {
            SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE);
            String auto_login = user_info.getString("auto_login", "false"); // 获取是否自动登录
            if (auto_login.equals("true")) {
                // 如果选择了自动登录的模式
                // 此时一定user_num不是0
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

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // do nothing 用于退出登录的返回响应
    }
}