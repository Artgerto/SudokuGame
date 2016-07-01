package com.example.sony.sudokugame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//Класс, отвечающий за заставку при запуске приложения
public class OpenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openactivity);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {}
                Intent i = new Intent(OpenActivity.this,Sudoku.class);
                startActivity(i);
                finish();
            }
        });
        t.start();
    }
}
