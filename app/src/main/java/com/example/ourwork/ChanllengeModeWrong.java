package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
/*回答错误界面*/
// 导入需要的包
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChanllengeModeWrong extends AppCompatActivity implements View.OnClickListener{
    // 全局变量
    Button btn_continue_wrong;
    TextView tv_ans;
    String ans; // 从Bundle中解析出ans

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_wrong);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 横屏
        // 数据成员初始化
        ans = "";
        // find
        btn_continue_wrong = findViewById(R.id.btn_continue_wrong);
        tv_ans = findViewById(R.id.tv_ans);
        // 上一个界面传来的intent中获取Bundle
        Intent intent = getIntent();
        if (intent != null) {
            Bundle DataBundle = intent.getExtras();
            if (DataBundle != null) {
                ans = DataBundle.getString("ans");
            }
        }
        // 设置tv_ans的值
//        tv_ans.setText("正确答案为:" + ans);
        // 设置监听器
        btn_continue_wrong.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continue_wrong) {
            Intent intent = new Intent(this, ChanllengeModeQuestion.class); // 使用这个进行返回
            setResult(Activity.RESULT_OK, intent);
            finish(); // 销毁本活动
        }
    }

}