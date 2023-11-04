package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

// 数学模式下 准备答题
// 导入需要的库
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import me.jessyan.autosize.internal.CustomAdapt;

public class AdventureModeMathInit extends AppCompatActivity implements View.OnClickListener, CustomAdapt{
    ImageButton btn_math_init_add;
    ImageButton btn_math_init_sub;
    ImageButton btn_math_init_mul;
    ImageButton btn_math_init_back;

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
        setContentView(R.layout.activity_adventure_mode_math_init);
        // find
        btn_math_init_add = findViewById(R.id.img_btn_math_init_add);
        btn_math_init_sub = findViewById(R.id.img_btn_math_init_sub);
        btn_math_init_mul = findViewById(R.id.img_btn_math_init_mul);
        btn_math_init_back = findViewById(R.id.iv_math_init_back);

        // 设置监听器
        btn_math_init_back.setOnClickListener(this);
        btn_math_init_add.setOnClickListener(this);
        btn_math_init_sub.setOnClickListener(this);
        btn_math_init_mul.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_math_init_back) {
            // 自定义返回按钮
            finish(); // 直接销毁即可
        }
        else if (v.getId() == R.id.img_btn_math_init_add) {
            Intent intent = new Intent(this, AdventureModeMathProblem.class);
            Bundle data = new Bundle();
            data.putString("type", "add");
            intent.putExtras(data);
            startActivity(intent);
            // 注意 不销毁本活动
        }
        else if (v.getId() == R.id.img_btn_math_init_sub) {
            Intent intent = new Intent(this, AdventureModeMathProblem.class);
            Bundle data = new Bundle();
            data.putString("type", "sub");
            intent.putExtras(data);
            startActivity(intent);
            // 注意 不销毁本活动
        }
        else if (v.getId() == R.id.img_btn_math_init_mul) {
            Intent intent = new Intent(this, AdventureModeMathProblem.class);
            Bundle data = new Bundle();
            data.putString("type", "mul");
            intent.putExtras(data);
            startActivity(intent);
            // 注意 不销毁本活动
        }
    }
}