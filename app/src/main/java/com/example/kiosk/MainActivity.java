package com.example.kiosk;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment_Menu fragment_menu;
    private Fragment_Order fragment_order;
    private FragmentTransaction fragmentTransaction;
    private TextToSpeech tts;
    final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // STT 퍼미션
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        fragmentManager = getSupportFragmentManager();
        fragment_menu = new Fragment_Menu();
        fragment_order = new Fragment_Order();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_frame, fragment_menu).commitAllowingStateLoss();

        // 호출 버튼 클릭시 화면전환
        Button button_call = (Button) findViewById(R.id.btn_call);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupCall.class);
                startActivity(intent);
            }
        });

        // TTS 설정
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if(status != ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        //V_main 서비스 호출
        Intent intent = new Intent(getApplicationContext(), V_main.class);
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        Intent serviceIntent;
        if (intent != null) {
            String Service = intent.getStringExtra("Service");
            Log.d("main activity intent", String.valueOf(Service));
            if (Service.equals("MAIN")) {
                serviceIntent = new Intent(getApplicationContext(), V_main.class);
                startService(serviceIntent);
            }
            else if (Service.equals("MENU")) {
                serviceIntent  = new Intent(getApplicationContext(), V_menu.class);
                startService(serviceIntent);
            }
            else if (Service.equals("CALL")) {
                serviceIntent  = new Intent(getApplicationContext(), V_call.class);
                startService(serviceIntent);
            }
        }

    }

    public void clickHandler(View view)
    {
        fragmentTransaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.btn_go_order:
                fragmentTransaction.replace(R.id.layout_frame, fragment_order).commitAllowingStateLoss();
                break;

            case R.id.btn_back:
                fragmentTransaction.replace(R.id.layout_frame, fragment_menu).commitAllowingStateLoss();
                break;

            case R.id.btn_submit_order:
                Intent intent = new Intent(getApplicationContext(), PopupFinish.class);
                startActivity(intent);
                break;
        }
    }

}