package com.example.ourwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// 挑战模式——总地图
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.os.Handler;
import java.util.HashMap; // 用于hash
import java.util.HashSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.content.pm.ActivityInfo;
import java.util.Random;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ViewFlipper;
import android.util.Log;
// 线程通信
import java.util.concurrent.CountDownLatch;
import me.jessyan.autosize.internal.CustomAdapt;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.util.Timer;
import java.util.TimerTask;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import android.view.ViewGroup;
import java.util.Arrays;

public class ChanllegeModeMap extends AppCompatActivity implements View.OnClickListener{

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
        public int diceBook; // 骰子的预定点数(如果不是-1, 表示已经被"下一位角色投中的点数为你前进的点数","下一位角色投中的点数为你后退的点数"而修改)
        public int pos; // 现在的位置
        public int rank; // 排名
        public boolean completed; // 是否已经结束比赛

        // 类似于构造函数
        public void FirstInit() {
            forward = backward = 0;
            forbidden = twofold = half = skip = noAward = false;
            diceBook = -1;
            pos = 0;
            rank = 0;
            completed = false;
        }

        // 清除所有buf
        public void ResetAll() {
            forbidden = twofold = half = skip = noAward = false;
            diceBook = -1;
        }

        // 设置前进
        public void SetForward (int f) {
            forward = f;
        }

        // 设置后退
        public void SetBackward (int b) {
            backward = b;
        }

    }
    // 定义player对象
    public Player[] players = new Player[5]; // 注意后面需要对象数组需要初始化

    // imageview数组类型
    ImageView[] iv_grid = new ImageView[40];
    // 按钮
    Button diceClick; // 按钮
    boolean diceIsRotating; // 骰子是否正在旋转
    ImageView iv_map_dice; // 旋转骰子
    ViewFlipper mFilper;

    TextView layer1name;
    TextView layer2name;
    TextView layer3name;
    TextView layer4name;
    TextView layer5name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 父类构造 & layout展示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanllege_mode_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 显示横屏

        // 从"AdventureModePersonNumber"活动中获取人数信息
        Intent InitIntent = getIntent();
        if (InitIntent != null) {
            Bundle InitBundle = InitIntent.getExtras();
            if (InitBundle != null) {
                person_num = InitBundle.getInt("person_num");
            }
        }
        // 调试使用
        // person_num = 3; //note:调试

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
        for (int i = 0; i <= 499; i++) {
            ProblemUsed[i] = false; // 还没有使用这些题目
        }
        diceIsRotating = false; // 最开始骰子没有转

        // 格子控件
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
        iv_grid[30] = findViewById(R.id.r30);
        iv_grid[31] = findViewById(R.id.r31);
        iv_grid[32] = findViewById(R.id.r32);
        iv_grid[33] = findViewById(R.id.r33);
        iv_grid[34] = findViewById(R.id.r34);
        iv_grid[35] = findViewById(R.id.r35);
        iv_grid[36] = findViewById(R.id.r36);
        iv_grid[37] = findViewById(R.id.r37);
        iv_grid[38] = findViewById(R.id.r38);
        iv_grid[39] = findViewById(R.id.r39);

        // 显示玩家所有位置
        ShowAllPlayer();

        // diceClick控件
        diceClick = findViewById(R.id.btn_map_dice);
        diceClick.setOnClickListener(this);

        // 轮播控件
        mFilper = findViewById(R.id.flipper);

        // 用于根据用户人数显示用户textview
        layer1name = findViewById(R.id.layer1name);
        layer2name = findViewById(R.id.layer2name);
        layer3name = findViewById(R.id.layer3name);
        layer4name = findViewById(R.id.layer4name);
        layer5name = findViewById(R.id.layer5name);

        // 屏蔽多余的用户textview
        hideusers();
    }
    public void hideusers() {
        for (int i = 1; i <= 5; i++) {
            if (i > person_num) {
                if (i == 3) {
                    layer3name.setVisibility(View.INVISIBLE);
                }
                else if (i ==4 ) {
                    layer4name.setVisibility(View.INVISIBLE);
                }
                else if (i == 5) {
                    layer5name.setVisibility(View.INVISIBLE);
                }
            }

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
        return person_next;
    }

    // 判断第id个人是否结束
    public boolean isFinish(int id) {
        if (players[id].pos >= 40)
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_map_dice) {
            if (players[person_now].forbidden) {
                // 该玩家被禁用
                // 给出提示信息
                String prompt = "玩家" + String.valueOf(person_now + 1) + "本回合被跳过,请由下一位玩家投掷骰子";
                Toast toast = Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
                // 刷新本人信息
                players[person_now].ResetAll();
                // 调整person_now
                person_now = GetNextPerson(); // 通过此函数获得下一个人
            }
            else {
                // 没被禁用
                int step = 0; // 本次骰子的数
                if (players[person_now].diceBook != -1) {
                    // 已经被上一位玩家设置了点数,此时不投筛子
                    // 给出提示信息
                    String prompt = "玩家" + String.valueOf(person_now + 1) + "本回合点数已被确定为" + String.valueOf(players[person_now].diceBook);
                    Toast toast = Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改step
                    step = players[person_now].diceBook;
                    // 清空
                    players[person_now].ResetAll();
                }
                else {
                    int rollResult = 0;
                    if (!diceIsRotating) {
                        diceIsRotating = true;
                        mFilper.startFlipping(); // 开始旋转
                        diceClick.setText("停止"); // 设置停止
                        Log.d("fas", "dafffff");
                        return; // 结束 等待第二次按
                    }
                    else {
                        diceIsRotating = false;
                        mFilper.stopFlipping(); // 停止旋转
                        diceClick.setText("开始投掷"); // 设置停止
                        rollResult = mFilper.getDisplayedChild() + 1; // 获取点数
                        Log.d("num", String.valueOf(rollResult));
                    }

                    if (players[person_now].twofold) { // 翻倍
                        rollResult *= 2;
                    }
                    else if (players[person_now].half) { // 减半
                        rollResult /= 2;
                    }

                    // 显示走的数目
                    Toast toast = Toast.makeText(getApplicationContext(), "您前进点数为" + String.valueOf(rollResult), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 赋值
                    step = rollResult;
                }

                // 得出骰子数后 开始移动
                players[person_now].pos += step;
                players[person_now].SetForward(step); // 设置前进格数
                // 展示位置
                ShowAllPlayer();

                // 判断是否结束
                if (isFinish(person_now)) {
                    // 完成了将不会再答题or抽卡,直接结束
                    // 打印出完成挑战信息
                    String show = "恭喜" + "第" + String.valueOf(person_now + 1) + "位" + "玩家" + "完成挑战";
                    Toast toast = Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_now].ResetAll(); // 先所有buf清空
                    players[person_now].completed = true;
                    players[person_now].rank = complete_num + 1;
                    complete_num++;
                    // 获得下一个人
                    person_now = GetNextPerson(); // 通过此函数获得下一个人
                    // 消失ta的发光轮廓
                    ShowAllPlayer();

                    // 判断游戏是否结束
                    if (complete_num == person_num - 1) {
                        Toast toast_over = Toast.makeText(getApplicationContext(), "游戏结束", Toast.LENGTH_SHORT);
                        toast_over.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                        toast_over.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent in = new Intent(ChanllegeModeMap.this, ChanllengeModeGameResult.class); // 转到游戏结算界面
                                Bundle dat = new Bundle();
                                dat.putInt("number", person_num); // 人数信息
                                for (int i = 1; i <= person_num - 1; i++) {
                                    for (int j = 0; j < person_num; j++) {
                                        if (players[j].rank == i) {
                                            dat.putInt(String.valueOf(i), j + 1); // 获得第i名的编号j
                                            break;
                                        }
                                    }
                                }
                                for (int i = 0; i < person_num; i++) {
                                    if (players[i].rank == 0) { // rank为0 说明没有完成游戏
                                        dat.putInt(String.valueOf(person_num), i + 1); // 注意此处需要+1
                                        break;
                                    }
                                }
                                in.putExtras(dat);
                                startActivity(in);
                                finish(); // 结束本活动
                            }
                        }, 3000); // 1000毫秒等于1秒

                    }
                }
                else { // 没有结束

                    // 显示所有人位置
                    ShowAllPlayer();
                    // 注意下负数

                    int pos = players[person_now].pos;
                    while (pos < 0) {
                        pos += 40;
                    }
                    Integer gridTypeInteger = grid.get(pos);
                    if (gridTypeInteger != null) {
                        // 只有在不是null的情况下才能够正常进行
                        int gridType = gridTypeInteger;
                        if (gridType == -2) {
                            // 起点(说明是后退回到了起点)
                            Toast toast = Toast.makeText(getApplicationContext(), "您已回到起点", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                            toast.show();

                            // 清除所有属性
                            players[person_now].ResetAll();
                            // 计算下一个人
                            person_now = GetNextPerson(); // 通过此函数获得下一个人
                        }
                        else if (gridType == -1) {
                            // 陷阱 直接禁用骰子
                            // 打印信息
                            Toast toast = Toast.makeText(getApplicationContext(), "触发陷阱，下一回合禁用骰子", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                            toast.show();
                            // 刷新属性
                            players[person_now].ResetAll();
                            players[person_now].forbidden = true;
                            // 得出下一个人
                            person_now = GetNextPerson(); // 通过本函数获得下一个人
                        }

                        else if (gridType == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), "进入抽卡界面", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                            toast.show();
                            // 展示下位置
                            ShowAllPlayer();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // 抽卡界面
                                    Intent intent = new Intent(ChanllegeModeMap.this, ChanllengeModeLottery.class);
                                    startActivity(intent);
                                }
                            }, 3000); // 1000毫秒等于1秒

                        }
                        else {
                            if (players[person_now].skip) {
                                // 跳过答题界面
                                // 打印信息
                                Toast toast = Toast.makeText(getApplicationContext(), "本次答题您无须作答", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                toast.show();
                                // 刷新属性
                                players[person_now].ResetAll();
                                // 得出下一个人
                                person_now = GetNextPerson(); // 通过本函数获得下一个人
                            }
                            else {
                                Toast toast = Toast.makeText(getApplicationContext(), "进入答题界面，您需回答" + String.valueOf(gridType) + "道题", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                toast.show();
                                // 也展示下位置
                                ShowAllPlayer();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 在这里执行启动下一个活动的代码
                                        // 答题界面
                                        Intent intent = new Intent(ChanllegeModeMap.this, ChanllengeModeQuestion.class);
                                        Bundle dataBundle = new Bundle();
                                        dataBundle.putInt("number", gridType); // 题目数量
                                        long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
                                        Random random = new Random(seed);
                                        Arrays.fill(ProblemUsed, false); // 保证本5次的题目不重复即可
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
                                }, 3000); // 1000毫秒等于1秒

                            }
                        }
                    }
                    else {
                        // 按理不会出现这种hashmap找不到key的情况
                        // 日志写异常
                        Log.d("position", "exception");
                    }

                }
            }
        }
    }

    // 一旦进入了答题  or  抽卡  将在此处进行修正
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 获得Bundle对象
        Bundle dataBundle = intent.getExtras();
        if (dataBundle != null) {
            String LotteryOrProblem = dataBundle.getString("LotteryOrProblem");
            Log.d("lotteryOproblem", "or");
            if (LotteryOrProblem.equals("Lottery")) {
                // 从抽卡界面返回
                String LotteryResult = dataBundle.getString("LotteryResult");
                LotteryResultDeal(LotteryResult);  // 调用函数进行修改即可
            }
            else if (LotteryOrProblem.equals("Problem")) {
                // 从答题界面返回
                Boolean isAllRight = dataBundle.getBoolean("isAllRight");
                IsAllRightDeal(isAllRight);
            }
        }
        // 这两种情况在此处刷新person_now
        person_now = GetNextPerson(); // 通过此函数获得下一个人 // important

        // 判定游戏是否结束
        if (complete_num == person_num - 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(ChanllegeModeMap.this, ChanllengeModeGameResult.class);
                    Bundle dat = new Bundle();
                    dat.putInt("number", person_num); // 人数信息
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
                            dat.putInt(String.valueOf(person_num), i + 1); // 注意需要+1
                            break;
                        }
                    }
                    in.putExtras(dat);
                    startActivity(in);
                    finish(); // 结束本活动
                }
            }, 3000); // 1000毫秒等于1秒
        }
    }

    // 处理抽卡返回信息
    public void LotteryResultDeal(String s) {
        String[] words = s.split(" "); // 根据空格进行分开
        if (words[0].equals("0")) {
            // 对自身的影响
            if (words[1].equals("forward")) {
                int step = Integer.parseInt(words[2]);
                players[person_now].pos += step; // 加上增量
                players[person_now].SetForward(step); // 修改此属性
                Toast toast_go = Toast.makeText(getApplicationContext(), "前进" + String.valueOf(step) +  "步", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();

                // 清除所有效果
                players[person_now].ResetAll();
                // 如果结束了
                if (isFinish(person_now)) {
                    // 打印出完成挑战信息
                    String show = "恭喜" + "第" + String.valueOf(person_now + 1) + "位" + "玩家" + "完成挑战";
                    Toast toast = Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_now].ResetAll();
                    players[person_now].completed = true;
                    players[person_now].rank = complete_num + 1;
                    complete_num++;
                    // 消失ta的发光轮廓
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里执行启动下一个活动的代码
                            ShowAllPlayer();
                        }
                    }, 2000); // 1000毫秒等于1秒

                }
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 显示更新的发光轮廓
                            ShowAllPlayer();
                            // 清除所有属性
                            players[person_now].ResetAll();
                        }
                    }, 2000); // 1000毫秒等于1秒
                }
            }
            else if (words[1].equals("backward")) {
                int step = Integer.parseInt(words[2]);
                players[person_now].pos -= step;
                players[person_now].SetBackward(step); // 修改此属性
                Toast toast_go = Toast.makeText(getApplicationContext(), "后退" + String.valueOf(step) +  "步", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 显示自己
                        ShowAllPlayer();
                        // 清除所有效果
                        players[person_now].ResetAll();
                    }
                }, 2000); // 1000毫秒等于1秒

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
            // 清除原先所有增益 important
            players[person_next].ResetAll(); //                                    此处刷新了
            if (words[1].equals("forward")) {
                int step = Integer.parseInt(words[2]);
                players[person_next].pos += step; // 加上增量
                players[person_next].SetForward(step); // 修改此属性
                Toast toast_go = Toast.makeText(getApplicationContext(), "前进" + String.valueOf(step) +  "步", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();
                try {
                    // 在主线程中等待 1 秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 如果结束了
                if (isFinish(person_next)) {
                    // 打印出完成挑战信息
                    String show = "恭喜" + "第" + String.valueOf(person_next + 1) + "位" + "玩家" + "完成挑战";
                    Toast toast = Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_next].completed = true;
                    players[person_next].rank = complete_num + 1;
                    complete_num++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 显示更新的发光轮廓
                            ShowAllPlayer();
                        }
                    }, 2000); // 1000毫秒等于1秒
                }
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 显示更新的发光轮廓
                            ShowAllPlayer();
                        }
                    }, 2000); // 1000毫秒等于1秒
                }
            }
            else if (words[1].equals("backward")) {
                int step = Integer.parseInt(words[2]);
                players[person_next].pos -= step;
                players[person_next].SetBackward(step); // 修改此属性
                Toast toast_go = Toast.makeText(getApplicationContext(), "后退" + String.valueOf(step) + "步", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 显示更新的发光轮廓
                        ShowAllPlayer();
                    }
                }, 2000); // 1000毫秒等于1秒
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
                Toast toast_go = Toast.makeText(getApplicationContext(), "玩家交换位置", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();
                // 交换位置
                int pos1 = players[person_now].pos;
                players[person_now].pos = players[person_next].pos;
                players[person_next].pos = pos1;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 显示更新的发光轮廓
                        ShowAllPlayer();
                    }
                }, 2000); // 1000毫秒等于1秒
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
    public void IsAllRightDeal(boolean isAllRight){
        if (isAllRight) {
            if (players[person_now].noAward) {
                // 打印信息
                Toast toast = Toast.makeText(getApplicationContext(), "本次答题您全部作答对但无效", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
                // 刷新属性
                players[person_now].ResetAll();
            }
            else {
                // 只有在全对时，才能够前进两格
                players[person_now].pos += 2;
                Toast toast_go = Toast.makeText(getApplicationContext(), "全部答对，前进两步", Toast.LENGTH_SHORT);
                toast_go.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast_go.show();
                try {
                    // 在主线程中等待 1 秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 如果结束了
                if (isFinish(person_now)) {
                    // 打印出完成挑战信息
                    String show = "恭喜" + "第" + String.valueOf(person_now + 1) + "位" + "玩家" + "完成挑战";
                    Toast toast = Toast.makeText(getApplicationContext(), show, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                    // 修改此人信息
                    players[person_now].ResetAll(); // 清除所有buf
                    players[person_now].completed = true;
                    players[person_now].rank = complete_num + 1;
                    complete_num++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 显示更新的发光轮廓
                            ShowAllPlayer();
                        }
                    }, 2000); // 1000毫秒等于1秒
                }
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 显示更新的发光轮廓
                            ShowAllPlayer();
                            // 清除所有属性
                            players[person_now].ResetAll();
                        }
                    }, 2000); // 1000毫秒等于1秒


                }
            }
        }
    }

    public void ShowAllPlayer() {
        // 首先刷新原来的资源
        RefreshAllGrid();
        // 显示所有玩家位置
        for (int i = 1; i <= person_num; i++) {
            ShowSingle(i - 1);
            Log.d("show",String.valueOf(i));
        }
    }

    // 刷新所有网格
    public void RefreshAllGrid() {
        for (int i = 0; i < 40; i++) {
            if (i == 38) {
                Drawable drawable = getResources().getDrawable(R.drawable.rectangle); // 替换成你的 Drawable 资源名称
                // 将 Drawable 设置到 ImageView
                iv_grid[i].setImageDrawable(drawable);
            }
            else if (i == 39) {
                Drawable drawable = getResources().getDrawable(R.drawable.rectangle2); // 替换成你的 Drawable 资源名称
                // 将 Drawable 设置到 ImageView
                iv_grid[i].setImageDrawable(drawable);
            }
            else {
                int mod = i % 3;
                if (mod == 0) {
                    Drawable drawable = getResources().getDrawable(R.drawable.rectangle); // 替换成你的 Drawable 资源名称
                    // 将 Drawable 设置到 ImageView
                    iv_grid[i].setImageDrawable(drawable);
                }
                else if (mod == 1) {
                    Drawable drawable = getResources().getDrawable(R.drawable.rectangle1); // 替换成你的 Drawable 资源名称
                    // 将 Drawable 设置到 ImageView
                    iv_grid[i].setImageDrawable(drawable);
                }
                else if (mod == 2) {
                    Drawable drawable = getResources().getDrawable(R.drawable.rectangle2); // 替换成你的 Drawable 资源名称
                    // 将 Drawable 设置到 ImageView
                    iv_grid[i].setImageDrawable(drawable);
                }
            }
        }
    }

    public void ShowSingle(int id) {
        if (players[id].completed) // 不显示它的
            return;
        int pos = players[id].pos;
        while (pos < 0) {
            pos += 40;
        }
        while (pos >= 40) {
            pos -= 40;
        }
        Drawable drawable = iv_grid[pos].getDrawable();
        Bitmap originalBitmap = drawableToBitamp(drawable);
        // 添加边框
        int customColor = 0;
        if (id == 0) {
            customColor = Color.YELLOW; // 黄色
        }
        else if (id == 1) {
            customColor = Color.RED; // 红色
        }
        else if (id == 2) {
            customColor = Color.BLACK; // 黑色
        }
        else if (id == 3) {
            customColor = Color.BLUE; // 蓝色
        }
        else if (id == 4) {
            customColor = Color.GRAY; // 灰色
        }
        Bitmap bitmapWithBorder = addBorderToBitmap(originalBitmap, 25, customColor);
        // 将带有边框的图片显示在ImageView中
        iv_grid[pos].setImageBitmap(bitmapWithBorder);

        // 创建一个 LayoutParams 对象来设置宽度和高度
        ViewGroup.LayoutParams params = iv_grid[pos].getLayoutParams();

    }

    public Bitmap drawableToBitamp(Drawable drawable)
    {
        int w=250;
        int h=250;
        // 取 drawable 的颜色格式
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
    public Bitmap addBorderToBitmap(Bitmap bitmap, int borderWidth, int color) {
        int width = bitmap.getWidth() + 2 * borderWidth;
        int height = bitmap.getHeight() + 2 * borderWidth;
        Bitmap borderedBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());

        Canvas canvas = new Canvas(borderedBitmap);
        canvas.drawColor(color); // 设置边框颜色

        // 将原始图片绘制在边框内部
        canvas.drawBitmap(bitmap, borderWidth, borderWidth, null);

        return borderedBitmap;
    }

    // 骰子进行旋转
    // use
    public int rollDice() {
        Log.d("roll", "come here");
        // 首先强制从第0张图片开始
        mFilper = findViewById(R.id.flipper);
        mFilper.setDisplayedChild(0);

        long seed = System.currentTimeMillis(); // 使用当前时间的毫秒数作为种子
        Random random = new Random(seed);
        int rollRandom = random.nextInt(6 - 1 + 1) + 1; // 生成1-6的随机数
        int TimeToRoll = 12000 + rollRandom * 100; // 设置总时长

        mFilper.startFlipping();
        Handler handler = new Handler();
        // 在3秒后停止ViewFlipper的自动播放
        handler.postDelayed(new Runnable() { // 子线程跑
            @Override
            public void run() {
                mFilper.stopFlipping(); // 停止自动切换
            }
        }, TimeToRoll); //

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

}