package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// 挑战模式——总地图
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.Gravity;
import android.os.Handler;
import java.util.HashMap; // 用于hash

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.content.pm.ActivityInfo;
import java.util.Random;

import android.widget.ViewFlipper;
import android.util.Log;

import me.jessyan.autosize.internal.CustomAdapt;
// 线程通信


public class ChanllegeModeMap extends AppCompatActivity implements View.OnClickListener {
    int person_num; // 用户人数
    int person_now; // 目前操作的用户
    int complete_num; // 完成游戏的人数
    HashMap<Integer, Integer> grid = new HashMap<>(); // 用于判定这个格子是 抽卡 or 答题 or 陷阱 or 开始 (分别用0, 题目数, -1, -2)表示
    boolean[] ProblemUsed = new boolean[500];


    // 定义player
    class Player {
        public int forward; // 前进格数
        public int backward; // 后退格数
        public boolean forbidden; // 是否被禁用
        public boolean twofold; // 是否翻倍
        public boolean half; // 是否减半
        public boolean skip; // 是否免答题
        public boolean noAward; // 下一次答题全部正确无收益
        public int pos; // 现在的位置
        public int diceBook; // 骰子的预定点数(如果不是-1, 表示已经被"下一位角色投中的点数为你前进的点数","下一位角色投中的点数为你后退的点数"而修改)
        public int rank; // 排名
        boolean completed; // 是否已经结束比赛
        public void FirstInit() {
            forward = backward = 0;
            forbidden = twofold = half = skip = noAward = false;
            pos = 0;
            diceBook = -1;
            rank = 0;
            completed = false;
        }

        // 每次移动的时候 都需要修改这两个信息
        public void SetWard(int f, int b) {
            forward = f;
            backward = b;
        }

        public void refresh(String Type, int res) { // 刷新
            if (Type.equals("forward")) {

            }
            else if (Type.equals("backword")) {

            }
            else if (Type.equals("forbidden")) {
                forbidden = true;
                forward = backward = 0;
                twofold = half = skip = noAward = false;
                diceBook = -1;
            }
            else if (Type.equals("twofold")) {

            }
            else if (Type.equals("half")) {

            }
            else if (Type.equals("skip")) {

            }
            else if (Type.equals("noAward")) {

            }
            else if (Type.equals("diceBook")) {

            }
        }

        public void ResetAll() {
            forward = backward = 0;
            forbidden = twofold = half = skip = noAward = false;
            diceBook = -1;
        }
    }
    // 定义player对象
    public Player[] players = new Player[5];

    // imageview数组类型
    ImageView[] iv_grid = new ImageView[40];
    // 按钮
    Button diceClick;
    ImageView iv_map_dice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllege_mode_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 从"AdventureModePersonNumber"活动中获取人数信息
        Intent InitIntent = getIntent();
        if (InitIntent != null) {
            Bundle InitBundle = InitIntent.getExtras();
            if (InitBundle != null) {
                person_num = InitBundle.getInt("person_num");
            }
        }
        // 初始化地图
        GridInit();
        // players初始化
        for (int i = 0; i < 5; i++) {
            players[i] = new Player(); // !!! 注意这里初始化
            players[i].FirstInit(); // 初始化
        }
        // 初始化数据成员
        person_now = 0; // 第一位玩家为0号玩家
        complete_num = 0; // 已完成人数为0
        for (int i = 0; i < 499; i++) {
            ProblemUsed[i] = false; // 还没有使用这些题目
        }
        // imageview控件
        // find
        iv_grid[0] = findViewById(R.id.r0);
        iv_grid[1] = findViewById(R.id.r1);
        iv_grid[2] = findViewById(R.id.r2);
        iv_grid[3] = findViewById(R.id.r3);
        iv_grid[4] = findViewById(R.id.r4);
        iv_grid[5] = findViewById(R.id.r5);
        iv_grid[6] = findViewById(R.id.r6);
        iv_grid[7] = findViewById(R.id.r7);
        iv_grid[8] = findViewById(R.id.r8);
        iv_grid[9] = findViewById(R.id.r9);
        iv_grid[10] = findViewById(R.id.r10);
        iv_grid[11] = findViewById(R.id.r11);
        iv_grid[12] = findViewById(R.id.r12);
        iv_grid[13] = findViewById(R.id.r13);
        iv_grid[14] = findViewById(R.id.r14);
        iv_grid[15] = findViewById(R.id.r15);
        iv_grid[16] = findViewById(R.id.r16);
        iv_grid[17] = findViewById(R.id.r17);
        iv_grid[18] = findViewById(R.id.r18);
        iv_grid[19] = findViewById(R.id.r19);
        iv_grid[20] = findViewById(R.id.r20);
        iv_grid[21] = findViewById(R.id.r21);
        iv_grid[22] = findViewById(R.id.r22);
        iv_grid[23] = findViewById(R.id.r23);
        iv_grid[24] = findViewById(R.id.r24);
        iv_grid[25] = findViewById(R.id.r25);
        iv_grid[26] = findViewById(R.id.r26);
        iv_grid[27] = findViewById(R.id.r27);
        iv_grid[28] = findViewById(R.id.r28);
        iv_grid[29] = findViewById(R.id.r29);
        iv_grid[30] = findViewById(R.id.r20);
        iv_grid[31] = findViewById(R.id.r31);
        iv_grid[32] = findViewById(R.id.r32);
        iv_grid[33] = findViewById(R.id.r33);
        iv_grid[34] = findViewById(R.id.r34);
        iv_grid[35] = findViewById(R.id.r35);
        iv_grid[36] = findViewById(R.id.r36);
        iv_grid[37] = findViewById(R.id.r37);
        iv_grid[38] = findViewById(R.id.r38);
        iv_grid[39] = findViewById(R.id.r39);

        // diceClick控件
        diceClick = findViewById(R.id.btn_map_dice);
        diceClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_map_dice) {
            if (players[person_now].forbidden) {
                // 该玩家被禁用
                // 给出提示信息
                String prompt = "玩家" + String.valueOf(person_now) + "本回合被跳过,请由下一位玩家投掷骰子";
                Toast toast = Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
                // 调整person_now
                person_now = GetNextPerson(); // 通过此函数获得下一个人
                // 刷新本人信息
                players[person_now].forbidden = false; // 该效果结束
            }
            else {
                int step = 0; // 本次骰子的数
                // 没被禁用
                if (players[person_now].diceBook != -1) {
                    // 已经被上一位玩家设置了点数,此时不投筛子
                    // 给出提示信息
                    String prompt = "玩家" + String.valueOf(person_now) + "本回合点数已被确定为" + String.valueOf(players[person_now].diceBook);
                    Toast toast = Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 玩家的diceBook效果清除
                    players[person_now].diceBook = -1;
                    // 修改step
                    step = players[person_now].diceBook;
                }
                else {
                    int rollResult = rollDice();
                    if (players[person_now].twofold) { // 翻倍
                        rollResult *= 2;
                    }
                    else if (players[person_now].half) { // 减半
                        rollResult /= 2;
                    }
                    step = rollResult;
                }
                // 得出骰子数后 开始移动
                players[person_now].pos += step;
                players[person_now].SetWard(step, 0);
                // 设置它的发光轮廓
                // to do
                // 判断是否结束
                if (isFinish(person_now)) {
                    // 完成了将不会再答题or抽卡,直接结束
                    // 打印出完成挑战信息
                    Toast toast = Toast.makeText(getApplicationContext(), "恭喜你完成挑战", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_now].completed = true;
                    players[person_now].rank = complete_num + 1;
                    complete_num++;
                    // 消失ta的发光轮廓
                    // to do
                }
                else { // 没有结束
                    int gridType = grid.get(players[person_now].pos); // 获取格子类型
                    if (gridType == -2) {
                        // 起点 不做处理 (按理不会出现这种情况)
                        Toast toast = Toast.makeText(getApplicationContext(), "游戏结束", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                    else if (gridType == -1) {
                        // 陷阱 直接禁用骰子
                        // 打印信息
                        Toast toast = Toast.makeText(getApplicationContext(), "触发陷阱，下一回合禁用骰子", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast.show();
                        // 刷新属性
                        players[person_now].refresh("forbidden", 0);
                        // 得出下一个人
                        person_now = GetNextPerson(); // 通过本函数获得下一个人
                    }
                    else if (gridType == 0) {
                        // 抽卡界面
                        Intent intent = new Intent(this, ChanllengeModeLottery.class);
                        startActivity(intent);
                    }
                    else {
                        // 答题界面
                        Intent intent = new Intent(this, ChanllengeModeQuestion.class);
                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("number", gridType);
                        long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
                        Random random = new Random(seed);
                        for (int i = 1; i <= gridType; i++) {
                            while (true) {
                                int x = random.nextInt(400 - 1 + 1) + 1; // 生成1-400的随机数
                                if (!ProblemUsed[x]) {
                                    ProblemUsed[x] = true; // 表示使用了这个题目
                                    dataBundle.putInt(String.valueOf(i), x);
                                    break;
                                }
                            }
                        }
                        intent.putExtras(dataBundle);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    public boolean isFinish(int id) {
        if (players[id].pos >= 40)
            return true;
        else
            return false;
    }

    public int rollDice() {
        // 首先强制从第0张图片开始
        ViewFlipper mFilper = findViewById(R.id.flipper);
        mFilper.setDisplayedChild(0);

        long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
        Random random = new Random(seed);
        int rollRandom = random.nextInt(6 - 1 + 1) + 1; // 生成1-6的随机数
        int TimeToRoll = 1200 + rollRandom * 100; // 设置总时长

        mFilper.startFlipping();
        Handler handler = new Handler();
        // 在3秒后停止ViewFlipper的自动播放
        handler.postDelayed(new Runnable() { // 子线程跑
            @Override
            public void run() {
                mFilper.stopFlipping(); // 停止自动切换
            }
        }, TimeToRoll); // 3秒后执行

        Log.d("number", String.valueOf(rollRandom)); // 调试打印日志
        return rollRandom;
    }

    // use
    private void rollDiceOrigin() {
        // 设置旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000); // 设置动画时长为1秒
        iv_map_dice.startAnimation(rotateAnimation);

        // 在2秒后停止动画并显示随机面
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopDiceAnimation();
            }
        }, 2000);
    }

    // use
    private void stopDiceAnimation() {
        iv_map_dice.clearAnimation();
        int[] diceFaces = {R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};
        int randomIndex = new Random().nextInt(diceFaces.length);
        iv_map_dice.setImageResource(diceFaces[randomIndex]);
    }

    // 一旦进入了答题  or  抽卡  将在此处进行修正
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle dataBundle = intent.getExtras();
        if (dataBundle != null) {
            String LotteryOrProblem = dataBundle.getString("LotteryOrProblem");
            if (LotteryOrProblem.equals("Lottery")) {
                // 从抽卡界面返回
                String LotteryResult = dataBundle.getString("LotteryResult");
                LotteryResultDeal(LotteryResult);  // 调用函数进行修改即可
            }
            else if (LotteryOrProblem.equals("Problem")) {
                // 从答题界面返回
            }
        }

        // 判定游戏是否结束
        if (complete_num == person_num - 1) {
            Intent in = new Intent(this, ChanllengeModeGameResult.class);
            Bundle dat = new Bundle();
            for (int i = 1; i <= person_num - 1; i++) {
                for (int j = 0; j < person_num; j++) {
                    if (players[j].rank == i) {
                        dat.putInt(String.valueOf(i), j + 1);
                        break;
                    }
                }
            }
            for (int i = 0; i < person_num; i++) {
                if (players[i].rank == 0) {
                    dat.putInt(String.valueOf(person_num), i);
                    break;
                }
            }
            in.putExtras(dat);
            startActivity(in);
            finish(); // 结束本活动
        }
    }

    // 地图表项的初始化
    public void GridInit() {
        grid.put(0, -2);
        grid.put(1, 3);
        grid.put(2, 4);
        grid.put(3, 5);
        grid.put(4, 0);
        grid.put(5, 4);
        grid.put(6, 5);
        grid.put(7, 4);
        grid.put(8, 0);
        grid.put(9, 3);

        grid.put(10, 4);
        grid.put(11, 0);
        grid.put(12, 3);
        grid.put(13, -1);
        grid.put(14, 2);
        grid.put(15, 3);
        grid.put(16, 0);
        grid.put(17, 4);
        grid.put(18, 5);
        grid.put(19, 3);

        grid.put(20, 0);
        grid.put(21, 5);
        grid.put(22, 5);
        grid.put(23, 4);
        grid.put(24, 2);
        grid.put(25, 0);
        grid.put(26, 3);
        grid.put(27, 4);
        grid.put(28, 0);
        grid.put(29, 5);

        grid.put(30, 4);
        grid.put(31, 3);
        grid.put(32, 0);
        grid.put(33, 4);
        grid.put(34, 3);
        grid.put(35, 3);
        grid.put(36, 0);
        grid.put(37, 4);
        grid.put(38, 3);
        grid.put(39, 5);
    }

    // 得出下一个人
    public int GetNextPerson() {
        int person_next = (person_now + 1) % person_num;
        while (players[person_next].completed) {
            person_next = (person_next + 1) % person_num;
        }
        return  person_next;
    }

    // 处理抽卡返回信息
    public void LotteryResultDeal(String s) {
        String[] words = s.split(" "); // 根据空格进行分开
        if (words[0].equals("0")) {
            // 对自身的影响
            if (words[1].equals("forward")) {
                int step = Integer.parseInt(words[2]);
                players[person_now].pos += step; // 加上增量
                // 清除所有效果
                players[person_now].ResetAll();
                // 如果结束了
                if (isFinish(person_now)) {
                    // 打印出完成挑战信息
                    Toast toast = Toast.makeText(getApplicationContext(), "恭喜你完成挑战", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_now].completed = true;
                    players[person_now].rank = complete_num + 1;
                    complete_num++;
                    // 消失ta的发光轮廓
                    // to do
                }
                else {
                    // 显示更新的发光轮廓
                    //  to do
                }
            }
            else if (words[1].equals("backward")) {
                int step = Integer.parseInt(words[2]);
                players[person_now].pos -= step;
                // 显示自己
                // to do
                // 清除所有效果
                players[person_now].ResetAll();
            }
            else if (words[1].equals("forbidden")) {
                // 清除所有效果
                players[person_now].ResetAll();
                // 再设置这个属性
                players[person_now].forbidden = true;
            }
            else if (words[1].equals("twofold")) {
                // 清除所有效果
                players[person_now].ResetAll();
                // 再设置这个属性
                players[person_now].twofold = true;
            }
            else if (words[1].equals("half")) {
                // 清除所有效果
                players[person_now].ResetAll();
                // 再设置这个属性
                players[person_now].half = true;
            }
            else if (words[1].equals("skip")) {
                // 清除所有效果
                players[person_now].ResetAll();
                // 再设置这个属性
                players[person_now].skip = true;
            }
            else if (words[1].equals("noAward")) {
                // 清除所有效果
                players[person_now].ResetAll();
                // 再设置这个属性
                players[person_now].noAward = true;
            }
        }
        else if (words[0].equals("1")) {
            // 对下一位的影响
            int person_next = GetNextPerson(); // 用这个函数来计算下一个人
            // 清除原先所有增益
            players[person_next].ResetAll();
            if (words[1].equals("forward")) {
                int step = Integer.parseInt(words[2]);
                players[person_next].pos += step; // 加上增量
                // 如果结束了
                if (isFinish(person_next)) {
                    // 打印出完成挑战信息
                    Toast toast = Toast.makeText(getApplicationContext(), "恭喜你完成挑战", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_next].completed = true;
                    players[person_next].rank = complete_num + 1;
                    complete_num++;
                    // 消失ta的发光轮廓
                    // to do
                }
                else {
                    // 显示更新的发光轮廓
                    //  to do
                }
            }
            else if (words[1].equals("backward")) {
                int step = Integer.parseInt(words[2]);
                players[person_next].pos -= step;
                // 显示
                //to  do
            }
            else if (words[1].equals("forbidden")) {
                players[person_next].forbidden = true;
            }
            else if (words[1].equals("twofold")) {
                players[person_next].twofold = true;
            }
            else if (words[1].equals("half")) {
                players[person_next].half = true;
            }
            else if (words[1].equals("skip")) {
                players[person_next].skip = true;
            }
        }
        else if (words[0].equals("2")) {
            // 共同影响
            if (words[1].equals("swap")) {
                int person_next = GetNextPerson();
                // 自己&下一个玩家的增益清空
                players[person_now].ResetAll();
                players[person_next].ResetAll();
                int pos1 = players[person_now].pos;
                players[person_now].pos = players[person_next].pos;
                players[person_next].pos = pos1;
            }
            else if (words[1].equals("isYourForward")) {
                int person_next = GetNextPerson();
                // 清空下一个玩家的增益
                players[person_next].ResetAll();
                players[person_next].diceBook = players[person_now].forward;
            }
            else if (words[1].equals("isYourBackward")) {
                int person_next = GetNextPerson();
                // 清空下一个玩家的增益
                players[person_next].ResetAll();
                players[person_next].diceBook = players[person_now].backward;
            }
        }
    }

    // 处理答题返回信息


}