package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    //private Intent intent;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView textView2=(TextView)findViewById(R.id.text2);
        TextView textView3 = (TextView)findViewById(R.id.text3);

        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//关闭之前得
                startActivity(intent);
               // finish();
                // 启动服务播放背景音乐
                Intent intent2 = new Intent(Main2Activity.this, MyIntentService.class);
                String action = MyIntentService.ACTION_MUSIC;
                // 设置action
                intent2.setAction(action);
                startService(intent2);


            }
        });


    }
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            // 对于intentService，这一步可能是多余的
            stopService(intent);
        }
    }*/
}
