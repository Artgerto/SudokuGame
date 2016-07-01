package com.example.sony.sudokugame;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class CongratScreen extends Activity implements OnClickListener
{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratscreen);
        Toast toast = Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        View menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);
        View replayButton = findViewById(R.id.replay_button);
        replayButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                finish();
                break;
            case R.id.replay_button:
                openNewGameDialog();
                break;
        }
    }

    private void openNewGameDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                startGame(i);
                            }
                        })
                .show();
    }

    //Начало игры
    private void startGame(int i) {
        Intent intent = new Intent(this,Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
        finish();
    }
    protected void onResume() {
        super.onResume();
        //Воспроизведение музыки для поздравления
        Music.play(this, R.raw.congr);
    }
    protected void onPause() {
        super.onPause();
        //Приостановка музыки
        Music.stop(this);
    }
}
