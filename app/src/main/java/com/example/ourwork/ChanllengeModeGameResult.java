package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

// 挑战模式——结果界面
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;
import me.jessyan.autosize.internal.CustomAdapt;
import java.util.ArrayList;
import java.util.List;

public class ChanllengeModeGameResult extends AppCompatActivity implements View.OnClickListener{

    String[] data = {"第一名: 2号选手", "第二名：3号选手", "第三名：1号选手", "第四名：5号选手", "第5名：1号选手"};
    ListView lv;
    Button btn_game_result;
    int number; // 人数信息
    String[] result = {"第一名: 2号选手", "第二名：3号选手", "第三名：1号选手", "第四名：5号选手", "第5名：1号选手"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_game_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 横屏显示

        // 从上一活动中获得资源
        Intent intent = getIntent();
        if (intent != null) {
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                // 初始化人数信息
                number = dataBundle.getInt("number");
                for (int i = 1; i <= number; i++) {
                    int x = dataBundle.getInt(String.valueOf(i));
                    result[i - 1] = "第" + String.valueOf(i) + "名： " + String.valueOf(x) + "号选手";
                }
            }
        }
//        number = 3; // 调试
        // find适配器
        lv = findViewById(R.id.lv_game_result);

        // 调用show函数
        show();

        // 按钮find & 设置监听器
        btn_game_result = findViewById(R.id.btn_game_result_finish);
        btn_game_result.setOnClickListener(this);

    }

    public void show() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            data.add(result[i]);
        }
        // 创建自定义的 ArrayAdapter，并将数据与自定义的列表项布局关联
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listviewshow, R.id.lv_text, data);
        lv.setAdapter(adapter);
    }

    public void onClick(View v){
        if (v.getId() == R.id.btn_game_result_finish) {
            // 直接销毁本活动
            finish();
        }
    }

}