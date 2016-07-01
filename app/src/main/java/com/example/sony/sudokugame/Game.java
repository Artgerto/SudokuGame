package com.example.sony.sudokugame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {

    public static final String KEY_DIFFICULTY = "com.example.sudoku.difficulty";
    public static final int DIFFICULTY_CONTINUE = -1;
    public static final int LV_1 = 0;
    public static final int LV_2 = 1;
    public static final int LV_3 = 2;
    public static final int LV_4 = 3;
    public static final int LV_5 = 4;
    public static final int LV_6 = 5;
    public static final int LV_7 = 6;
    public static final int LV_8 = 7;
    public static final int LV_9 = 8;
    private static final String key = "puzzle";

    private int puzzle[] = new int[9 * 9];

    private PuzzleView puzzleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY,LV_1);
        puzzle = getPuzzle(diff);
        calculateUsedTiles();
        calculateUsedTilesIndex(question);
        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
    }

    //Метод вызывания Keypad или ошибки при отсутвии подходящего значения для вставки
    protected void showKeypadOrError(int x, int y) {
        int tiles[] = getUsedTiles(x, y);
        if (tiles.length == 9) {
            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Dialog v = new Keypad(this, tiles, puzzleView);
            v.show();
        }
    }

    //Проверка на повторения значения ячейки в той же строке, столбце, квадрате 3х3 + метод установления значения ячейки
    protected boolean setTileIfValid(int x, int y, int value) {
        int tiles[] = getUsedTiles(x, y);
        if (value != 0) {
            for (int tile : tiles) {
                if (tile == value)
                    return false;
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        return true;
    }

    private final int used[][][] = new int[9][9][];

    //Получение массива со значениями количества заполненных клеток (в той же строке, столбце, квадрате 3х3) для каждой ячейки
    protected int[] getUsedTiles(int x, int y) {
        return used[x][y];
    }

    //Получаем в used[x][y] значения количества заполненных клеток (в той же строке, столбце, квадрате 3х3) для каждой ячейки
    private void calculateUsedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = calculateUsedTiles(x, y);
            }
        }
    }

    //Количесво заполненных клеток в той же строке, столбце, квадрате 3х3
    private int[] calculateUsedTiles(int x, int y) {
        int c[] = new int[9];
        // Линия по горизонтали(строка)
        for (int i = 0; i < 9; i++) {
            if (i == y)
                continue;
            int t = getTile(x, i);
            if (t != 0)
                c[t - 1] = t;
        }
        // Линия по вертикали(столбец)
        for (int i = 0; i < 9; i++) {
            if (i == x)
                continue;
            int t = getTile(i, y);
            if (t != 0)
                c[t - 1] = t;
        }
        // Значения в квадрате 3х3, где находится цифра с указанными координатами x,y
        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;
        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }
        // nused - кол-во чисел в том квадрате 3х3
        int nused = 0;
        for (int t : c) {
            if (t != 0)
                nused++;
        }
        //Количесво заполненных клеток
        int c1[] = new int[nused];
        nused = 0;
        for (int t : c) {
            if (t != 0)
                c1[nused++] = t;
        }
        return c1;
    }

    //Условие по уровням сложности
    private final String lev1 = "018000490700000006090107020" + "000050000570246039000080000" + "060409080300000001089000760";
    private final String lev2 = "300605009020809010000040000" + "740000068002000900560000043" + "000010000070304080600207005";
    private final String lev3 = "009050200007000600506308409" + "600801004900000006700902005" + "405607308008000900003080500";
    private final String lev4 = "730020059000507000001000800" + "070050090200000006080030040" + "007000100000905000340080075";
    private final String lev5 = "300409007890000021006080300" + "000917000002060700000542000" + "003090800780000043400308009";
    private final String lev6 = "000030000073020940000704000" + "630000074100306009490000061" + "000509000056070810000010000";
    private final String lev7 = "006305200102000307000070000" + "600020005000109000200060008" + "000010000809000506004602100";
    private final String lev8 = "240000061000000000070508040" + "406050308050000010803040602" + "020104090000000000930000086";
    private final String lev9 = "800503009090107050000000000" + "002000900607934201001000700" + "000000000070306040200805003";

    //Получение массива по уровню сложности
    private int[] getPuzzle(int diff) {
        String puz;
        switch(diff) {
            // Продолжить последнюю игру
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(key, lev1);
                break;
            case LV_1:
            case LV_2:
                puz = lev2;
                break;
            case LV_3:
                puz = lev3;
                break;
            case LV_4:
                puz = lev4;
                break;
            case LV_5:
                puz = lev5;
                break;
            case LV_6:
                puz = lev6;
                break;
            case LV_7:
                puz = lev7;
                break;
            case LV_8:
                puz = lev8;
                break;
            case LV_9:
                puz = lev9;
                break;
            default:
                puz = lev1;
                break;
        }
        return fromPuzzleString(puz);
    }
    //Перевод массива в строку
    static private String toPuzzleString(int[] puz) {
        StringBuilder buf = new StringBuilder();
        for (int element : puz) {
            buf.append(element);
        }
        return buf.toString();
    }

    //Преобразуем строку с условием в массив
    static protected int[] fromPuzzleString(String string) {
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }
    //Получаем значение из клетки
    private int getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }
    //Устанавливаем значения в клетку
    private void setTile(int x, int y, int value) {
        puzzle[y * 9 + x] = value;
    }
    //Устанавливаем клеткам со значением "0" - пустое место
    protected String getTileString(int x, int y) {
        int v = getTile(x, y);
        if (v == 0)
            return "";
        else
            return String.valueOf(v);
    }

    protected final int question[][] = new int [81][2];
    protected int usedNum = 0;
    //Метод изменяет значение преременной usedNum для повторного использования в методе isFinish()
    private void calculateUsedTilesIndex(int[][] c) {
        int k = 0;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                final int t = getTile(i, j);
                if (t != 0)
                {
                    c[k][0] = i;
                    c[k][1] = j;
                    k++;
                }
            }
        }
        usedNum = k;
    }

    //Получаем массив всех чисел
    protected int[][] getUsedIndex()
    {
        return question;
    }

    //Проверка на конец игры
    protected boolean isFinish()
    {
        int[][] c = new int[81][2];
        calculateUsedTilesIndex(c);
        if (usedNum == 81)
        {
            return true;
        }
        return false;
    }

    //Для вызова после завершения игры в случае победы
    protected void callCongratScreen()
    {
        Intent intent = new Intent(Game.this, CongratScreen.class);
        startActivity(intent);
        finish();
    }

    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.game);

    }

    protected void onPause() {
        super.onPause();
        Music.stop(this);
        getPreferences(MODE_PRIVATE).edit().putString(key, toPuzzleString(puzzle)).commit();
    }

    //Coxpaнение Activity
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(key, toPuzzleString(puzzle));
        super.onSaveInstanceState(outState);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
