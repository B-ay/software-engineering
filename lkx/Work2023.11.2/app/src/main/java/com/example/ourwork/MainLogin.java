package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;
import me.jessyan.autosize.internal.CustomAdapt;
public class MainLogin extends AppCompatActivity implements View.OnClickListener,CustomAdapt{
    Button btn_login_next;
    ImageView iv_login_back;
    TextView tv_login_back;
    EditText et_login_username;
    EditText et_login_password;
    TextView tv_login_to_register;
    String username;
    String password;
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
        setContentView(R.layout.activity_main_login);
        // find
        btn_login_next = findViewById(R.id.btn_login_next);
        iv_login_back = findViewById(R.id.iv_login_back);
        tv_login_back = findViewById(R.id.tv_login_back);
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        tv_login_to_register = findViewById(R.id.tv_login_back);
        // 设置监听器
        btn_login_next.setOnClickListener(this);
        iv_login_back.setOnClickListener(this);
        tv_login_back.setOnClickListener(this);
        tv_login_to_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login_next) {
            // 直接从本地进行读取
            // 获得用户名和密码
            username = et_login_username.getText().toString();
            password = et_login_password.getText().toString();
            if (username.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "用户名为空", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else if (password.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "密码为空", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else {
                SharedPreferences user_info = getSharedPreferences("user_info", MODE_PRIVATE); // 获得共享文件
                String RealPassword = user_info.getString(username, "");
                if (RealPassword.equals("")) {
                    // 说明用户名不存在
                    Toast toast = Toast.makeText(getApplicationContext(), "用户名不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
                else {
                    if (RealPassword.equals(password)) {
                        // 用户名和密码都正确
                        // to do 需要从本地存储卡将该用户的"Personal Center"数据找出

                        /*to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do */









                        /*to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do to do */
                        // 进入"ModeChoose"界面
                        Intent intent = new Intent(this, ModeChoose.class);
                        startActivity(intent);
                        finish(); // 销毁本活动
                    }
                    else {
                        // 密码错误
                        Toast toast = Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            }
        }
        else if (v.getId() == R.id.iv_login_back) {
            // 直接销毁本活动
            finish();
        }
        else if (v.getId() == R.id.tv_login_back) {
            // 直接销毁本活动
            finish();
        }
        else if (v.getId() == R.id.tv_login_to_register) {
            // 跳转到注册界面
            Intent intent = new Intent(this, MainRegister.class);
            startActivity(intent);
            // finish(); // 及时销毁本活动 // 注意不销毁本活动
        }
    }

}