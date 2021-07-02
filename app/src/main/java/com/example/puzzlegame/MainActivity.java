package com.example.puzzlegame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private GamePintuLayout gamePintuLayout;
    private TextView textView;
    private Button button;//添加图片
    private Button button3;//退出
    private Button button4;//最高纪录
    static Bitmap bitmap;
    static Bitmap bitmap1;
    static boolean flag2 = false;
    int time = 0;
    private TextView timeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

                Bundle bundle = new Bundle();
               // String str = "第" + GamePintuLayout.numguan + "关，用了 " + GamePintuLayout.number + " 步";
                //char[] strchar = str.toCharArray();
                bundle.putString("name", GamePintuLayout.str);


                // char  strchar[] = str.toCharArray();
                // bundle.putCharArray("name",strchar);


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gameview);
        textView = (TextView) findViewById(R.id.text);
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出整个游戏
                //android.os.Process.killProcess(android.os.Process.myPid());
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
            }
        });
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gamePintuLayout.nextLevel();
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */

                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });

        //每100毫秒更新一次步数
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 100);


    }


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            //ImageView image = (ImageView) findViewById(R.id.image);
            /* 将Bitmap设定到ImageView */
            //image.setImageBitmap(bitmap);
            textView.setText("第" + GamePintuLayout.numguan + "关，您用了 " + GamePintuLayout.number + " 步");
            handler.postDelayed(this, 1000);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                // bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                gamePintuLayout.bitmap = bitmap;
                gamePintuLayout.initBitmap();
                gamePintuLayout.initItem();
                /* 将Bitmap设定到ImageView */
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void restart(View view) {
        //
        //restore();

        //

        //将拼图重新打乱
        gamePintuLayout.next();
        /*handler.removeMessages(1);
        //将时间重新归0，并且重新开始计时
        time =0 ;
        timeTV.setText("时间  ："+time+" 秒");
        handler.sendEmptyMessageDelayed(1,1000);
    }

    private void restore() {
        ib0.setClickable(true);
        ib1.setClickable(true);
        ib2.setClickable(true);
        ib3.setClickable(true);
        ib4.setClickable(true);
        ib5.setClickable(true);
        ib6.setClickable(true);
        ib7.setClickable(true);
        ib8.setClickable(true);
        //
        ImageButton clickBtn=findViewById(blankImgid);
        clickBtn.setVisibility(View.VISIBLE);
        //
        ImageButton blankBtn=findViewById(R.id.pt_bb3xx);
        blankBtn.setVisibility(View.INVISIBLE);
        //
        blankImgid=R.id.pt_bb3xx;
        blankSwap=imagecount - 1;
    }*/


    }
}