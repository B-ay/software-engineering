package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
/*答题正确界面*/
// 导入需要的包
import android.widget.Button;
import android.view.View;

public class ChanllengeModeCorrect extends AppCompatActivity implements View.OnClickListener{
    // 全局变量
    Button btn_continue_correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_correct);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 横屏
        // find
        btn_continue_correct = findViewById(R.id.btn_continue_correct);
        // 设置监听器
        btn_continue_correct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continue_correct) {
            Intent intent = new Intent(this, ChanllengeModeQuestion.class); // 使用这个进行返回
            setResult(Activity.RESULT_OK, intent);
            finish(); // 销毁本活动
        }
    }

}