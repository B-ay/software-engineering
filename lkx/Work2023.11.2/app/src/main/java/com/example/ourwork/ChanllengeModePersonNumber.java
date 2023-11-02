package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ChanllengeModePersonNumber extends AppCompatActivity implements View.OnClickListener{
    Button btn_person_num_2;
    Button btn_person_num_3;
    Button btn_person_num_4;
    Button btn_person_num_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(R.layout.activity_chanllenge_mode_person_number);
        // find
        btn_person_num_2 = findViewById(R.id.btn_person_number_2);
        btn_person_num_3 = findViewById(R.id.btn_person_number_3);
        btn_person_num_4 = findViewById(R.id.btn_person_num_4);
        btn_person_num_5 = findViewById(R.id.btn_person_number_5);
        // 设置监听器
        btn_person_num_2.setOnClickListener(this);
        btn_person_num_3.setOnClickListener(this);
        btn_person_num_4.setOnClickListener(this);
        btn_person_num_5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_person_number_2) {
            Intent intent = new Intent(this, ChanllegeModeMap.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("person_num", 2);
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 销毁本活动
        }
        else if (v.getId() == R.id.btn_person_number_3) {
            Intent intent = new Intent(this, ChanllegeModeMap.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("person_num", 3);
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 销毁本活动
        }
        else if (v.getId() == R.id.btn_person_num_4) {
            Intent intent = new Intent(this, ChanllegeModeMap.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("person_num", 4);
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 销毁本活动
        }
        else if (v.getId() == R.id.btn_person_number_5) {
            Intent intent = new Intent(this, ChanllegeModeMap.class);
            Bundle DataBundle = new Bundle();
            DataBundle.putInt("person_num", 5);
            intent.putExtras(DataBundle);
            startActivity(intent);
            finish(); // 销毁本活动
        }
    }

}

















