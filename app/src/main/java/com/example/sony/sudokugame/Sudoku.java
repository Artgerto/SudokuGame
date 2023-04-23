package com.example.sony.sudokugame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends AppCompatActivity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, Prefs.class));
            return true;
        }

        return false;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.about_button) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
            return;
        }

        if (v.getId() == R.id.new_button) {
            openNewGameDialog();
            return;
        }

        if (v.getId() == R.id.exit_button) {
            finish();
            return;
        }

        if (v.getId() == R.id.continue_button) {
            startGame(Game.DIFFICULTY_CONTINUE);
        }
    }

    //Открытие диалогового окна с выбором уровня сложности
    private void openNewGameDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                startGame(i);
                            }
                        }).show();
    }

    //Открытие Activity со стартом игры
    public void startGame(int i) {
        Intent intent = new Intent(Sudoku.this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
    }
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.menu);
    }

    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }
}
