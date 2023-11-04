package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

// 挑战模式——抽卡界面
// 导入需要的包
import android.provider.ContactsContract;
import android.widget.ImageButton;
import android.view.View;
import me.jessyan.autosize.internal.CustomAdapt;

public class ChanllengeModeLottery extends AppCompatActivity implements View.OnClickListener{
    // 全局变量设置
    ImageButton img_btn_lottery_card1, img_btn_lottery_card2, img_btn_lottery_card3, img_btn_lottery_card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_lottery);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 显示横屏

        // find
        img_btn_lottery_card1 = findViewById(R.id.img_btn_lottery_card1);
        img_btn_lottery_card2 = findViewById(R.id.img_btn_lottery_card2);
        img_btn_lottery_card3 = findViewById(R.id.img_btn_lottery_card3);
        img_btn_lottery_card4 = findViewById(R.id.img_btn_lottery_card4);
        // 设置监听器
        img_btn_lottery_card1.setOnClickListener(this);
        img_btn_lottery_card2.setOnClickListener(this);
        img_btn_lottery_card3.setOnClickListener(this);
        img_btn_lottery_card4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_btn_lottery_card1) {
            Intent intent = new Intent(this, ChanllengeModeLotteryResult.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("type", 1); // 传递类型为1, 使其从1号文件中读取
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 及时销毁本活动
        }
        else if (v.getId() == R.id.img_btn_lottery_card2) {
            Intent intent = new Intent(this, ChanllengeModeLotteryResult.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("type", 2); // 传递类型为1, 使其从1号文件中读取
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 及时销毁本活动
        }
        else if (v.getId() == R.id.img_btn_lottery_card3) {
            Intent intent = new Intent(this, ChanllengeModeLotteryResult.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("type", 3); // 传递类型为1, 使其从1号文件中读取
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 及时销毁本活动
        }
        else if (v.getId() == R.id.img_btn_lottery_card4) {
            Intent intent = new Intent(this, ChanllengeModeLotteryResult.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("type", 4); // 传递类型为1, 使其从1号文件中读取
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 及时销毁本活动
        }
    }

}