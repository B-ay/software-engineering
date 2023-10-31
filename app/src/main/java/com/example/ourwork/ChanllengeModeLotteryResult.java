package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// 挑战模式——抽卡结果界面
// 引入需要的库
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap; // 用于hash
import java.util.Random; // 用于生成随机数
import android.content.Intent;

public class ChanllengeModeLotteryResult extends AppCompatActivity implements View.OnClickListener {
    // 全局变量
    TextView tv_lottery_result_context;
    Button btn_continue_lottery_result;
    HashMap<Integer, String> result; // 用于传递信息的结果
    HashMap<Integer, String> print; // 用于打印的结果
    int choice; // 实际的选择
    String card_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllenge_mode_lottery_result);
        // result初始化
        result = new HashMap<>();
        ResultInit();
        // print初始化
        print = new HashMap<>();
        PrintInit();
        // find
        tv_lottery_result_context = findViewById(R.id.tv_lottery_result_context);
        btn_continue_lottery_result = findViewById(R.id.btn_continue_lottery_result);
        // 从Intent中获取Bundle
        Bundle DataBundle = getIntent().getExtras();
        int lastChoice = 0;
        if (DataBundle != null) {
            lastChoice = DataBundle.getInt("type");
        }
        // 获得随机数
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        choice = (random.nextInt(1000) + lastChoice) % result.size();
        // 打印结果
        card_print = print.get(choice);
        tv_lottery_result_context.setText(card_print);
        // 设置监听器
        btn_continue_lottery_result.setOnClickListener(this);
    }

    // 所有情况初始化
    public void ResultInit() {
        result.put(0, "0 forward 2");
        result.put(1, "0 forbidden 0");
        result.put(2, "0 forward 1");
        result.put(3, "0 backward 2");
        result.put(4, "0 twofold 0");
        result.put(5, "1 forward 2");
        result.put(6, "1 forbidden 0");
        result.put(7, "0 backward 1");
        result.put(8, "1 backward 2");
        result.put(9, "0 backward 2");

        result.put(10, "0 skip 0");
        result.put(11, "1 skip 0");
        result.put(12, "0 forward 2");
        result.put(13, "0 forbidden 0");
        result.put(14, "0 forward 3");
        result.put(15, "1 twofold 2");
        result.put(16, "0 forward 1");
        result.put(17, "0 backward 1");
        result.put(18, "0 forward 1");
        result.put(19, "0 forbidden 0");

        result.put(20, "1 half 0");
        result.put(21, "1 skip 0");
        result.put(22, "0 skip 0");
        result.put(23, "0 half 0");
        result.put(24, "2 swap 0");
        result.put(25, "0 noAward 0");
        result.put(26, "0 forward 2");
        result.put(27, "0 backword 1");
        result.put(28, "2 isYourForward 0");
        result.put(29, "2 isYourBackward 0");

        result.put(30, "0 forward 1");
        result.put(31, "0 backward 2");
        result.put(32, "0 forbidden 0");
        result.put(33, "0 twofold 0");
        result.put(34, "0 forward 3");
        result.put(35, "0 half 0");
        result.put(36, "1 forbidden 0");
        result.put(37, "2 isYourForward 0");
        result.put(38, "2 isYourBackward 0");
        result.put(39, "2 swap 0");
    }

    public void PrintInit() {
        print.put(0,"前进两格");
        print.put(1,"禁用骰子一回合");
        print.put(2,"前进一格");
        print.put(3,"后退两格");
        print.put(4,"下一回合投中的点数翻倍");
        print.put(5,"下一位角色前进两格");
        print.put(6,"禁用下一位角色的骰子一回合");
        print.put(7,"后退一格");
        print.put(8,"下一位角色后退两格");
        print.put(9,"后退两格");
        print.put(10,"免答题一回合");
        print.put(11,"下一位角色免答题");
        print.put(12,"前进两格");
        print.put(13,"禁用骰子一回合");
        print.put(14,"前进三格");
        print.put(15,"下一位角色投中的点数翻倍");
        print.put(16,"前进一格");
        print.put(17,"后退一格");
        print.put(18,"前进一格");
        print.put(19,"禁用骰子一回合");
        print.put(20,"下一位角色投中的点数减半");
        print.put(21,"下一位角色免答题");
        print.put(22,"免答题一回合");
        print.put(23,"下一回合投中点数减半");
        print.put(24,"和下一位角色互换位置");
        print.put(25,"问题全部回答正确不奖励");
        print.put(26,"前进两格");
        print.put(27,"后退一格");
        print.put(28,"下一位角色投中的点数为你前进的点数");
        print.put(29,"下一位角色投中的点数为你后退的点数");
        print.put(30,"前进一格");
        print.put(31,"后退两格");
        print.put(32,"禁用骰子一回合");
        print.put(33,"下一回合投中的点数翻倍");
        print.put(34,"前进三格");
        print.put(35,"下一回合投中点数减半");
        print.put(36,"禁用下一位角色的骰子一回合");
        print.put(37,"下一位角色投中的点数为你前进的点数");
        print.put(38,"下一位角色投中的点数为你后退的点数");
        print.put(39,"和下一位角色互换位置");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_continue_lottery_result) { // 点击continue按钮->跳转到原先的map界面
            Intent intent = new Intent(this, ChanllegeModeMap.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置返回到原先的map界面
            // 建立Bundle进行信息传递
            Bundle DataBundle = new Bundle();
            DataBundle.putString("LotteryOrProblem", "Lottery"); // 抽卡还是答题
            DataBundle.putString("LotteryResult", result.get(choice)); // 放置结果
            intent.putExtras(DataBundle); // 放入
            startActivity(intent);
            finish(); // 此处应该是不需要使用finish 因为有FLAG_ACTIVITY_CLEAR_TOP
        }
    }

}