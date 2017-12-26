package com.kemp.testfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by wangkp on 2017/12/26.
 */

public class TestActivity extends AppCompatActivity {

    static int num = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv = findViewById(R.id.textView2);
        Button btn = findViewById(R.id.button2);

        if(savedInstanceState != null){
            num = savedInstanceState.getInt("num");
        }
        tv.setText("activity:" + num);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                Intent intent = new Intent(TestActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        if (num == 2) {
            throw new NullPointerException();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("num", num);
    }
}
