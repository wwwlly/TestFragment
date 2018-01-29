package com.kemp.testfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 *
 * 程序由于某种原因发生崩溃时，会结束问题activity及其上层activity。
 * 验证：本例中：MainActivity -> TestActivity 1 -> TestActivity 2(问题activity 抛出异常使程序崩溃，
 * 程序回到MAinActivity，如果MainActivity或TestActivity 1使程序崩溃会回到桌面，如果TestActivity 3使程序
 * 崩溃会回到TestActivity 1)
 * 这是android自带的机制吗？能力有限还没弄明白，记录下。
 *
 * 如果是验证中的情况崩溃后回到MainActivity，MainActity没有重新初始化(确切点讲没有被销毁就执行onCreate方法)
 * 如果没有做相应处理就会导致本例中的fragment重叠现象。
 *
 * Created by wangkp on 2017/12/25.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tvFirst, tvSecond;

    private int current = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        final FragmentManager fm = getSupportFragmentManager();

        if(savedInstanceState != null){
            Log.d(TAG,"savedInstanceState is not null");
            current = savedInstanceState.getInt("current");
        }else{
            fm.beginTransaction().add(R.id.fl_content, Fragment.instantiate(this, FirstFragment.class.getName())).commit();
        }

        tvFirst = findViewById(R.id.tv_first);
        tvSecond = findViewById(R.id.tv_second);
        tvFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 0;
                fm.beginTransaction().replace(R.id.fl_content, Fragment.instantiate(MainActivity.this, FirstFragment.class.getName())).commit();
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 1;
                fm.beginTransaction().replace(R.id.fl_content, Fragment.instantiate(MainActivity.this, SecondFragment.class.getName())).commit();
            }
        });

    }

    /**
     * 解决崩溃后fragment混乱
     * fragment不需要处理FragmentActivity已经处理过，但是在onCreate中init并添加fragment时一定要在
     * savedInstanceState为空的时候添加，不为空时FragmentActivity已经处理过;我们要做的是处理fragment之外
     * 的东西，比如：tvFirst和tvSecond的行为也就是current,onSaveInstanceState中记载onCreate中读取。
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current", current);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
