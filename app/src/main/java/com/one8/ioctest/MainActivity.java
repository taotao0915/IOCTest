package com.one8.ioctest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv)
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        tv.setText("IOCTest");
    }


    @OnClick({R.id.tv,R.id.img})
    @CheckNet
    private void onClick(View view){
        switch (view.getId()){
            case R.id.tv:
                Toast.makeText(this,"点击了文本",0).show();
                break;
            case R.id.img:
                Toast.makeText(this,"点击了图片",0).show();
                break;
        }

    }

}
