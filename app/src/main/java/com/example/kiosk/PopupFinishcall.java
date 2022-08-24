package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class PopupFinishcall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_finishcall);

        TextView tv = (TextView)findViewById(R.id.tv_second);
        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                int num = (int) (l / 1000);
                tv.setText(Integer.toString(num + 1) + "초 후에 초기화면으로 넘어갑니다.");
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), Start.class);
        startActivity(intent);
    }
}