package com.example.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryActivity extends AppCompatActivity {
    private Button save, read, back;
    private TextView content;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        save = (Button) findViewById(R.id.save);
        read = (Button) findViewById(R.id.read);
        back = (Button) findViewById(R.id.back);
        content = (TextView) findViewById(R.id.content);

        Bundle bundle=getIntent().getExtras();
      String name=  bundle.getString("name");
        content.setText(name);
        show = (TextView) findViewById(R.id.show);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用SAveFile方法  文件保存到date/date目录下
                saveFile();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.setText(readFile());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
 /*
    //删除文件
    public void removeFile() {
        String[] files = fileList();
        for (String str : files) {
            if (str.equals("text.text")) {
                deleteFile(str);
                break;
            }
        }
    }*/

    //从内存储中读取文件
    public String readFile() {
        // BufferedReader包装流（字符流 字节流）  带缓冲区的
        BufferedReader reader = null;
        FileInputStream fis = null;
        StringBuilder sbd = new StringBuilder();
        try {
            fis = openFileInput("text.text");
            reader = new BufferedReader(new InputStreamReader(fis));
            //sbd.append(getFilesDir().getCanonicalPath());

            String row = "";
            while ((row = reader.readLine()) != null) {
                sbd.append(row);

            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getBaseContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sbd.toString();
    }

    //保存文件到内存储
    public void saveFile() {
        FileOutputStream fos = null;
        try {
            /*
            openFileOutput返回一个输出字节流
            指向的路径为data/data/包名/files
            参数1：文件名称（如果不存在则自动创建）
            参数2:模式MODE_APPEND文件内容可樶加
                  模式MODE_PRIVATE文件内容被覆盖
             */
            fos = openFileOutput("text.text", MODE_PRIVATE);
            String str = content.getText().toString();
            fos.write(str.getBytes());
            Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
