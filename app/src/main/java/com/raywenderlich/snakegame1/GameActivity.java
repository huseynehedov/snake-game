package com.raywenderlich.snakegame1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private ImageView[][] images = new ImageView[5][11];
    private List<List<Integer>> snake = new ArrayList<>();
    private int[] apple = new int[2];
    private int[][] array = new int[5][11];
    private Random random = new Random();
    public static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
    public static final int SNAKE = 1, BLANK = 0;
    private GameLoop gameLoop;
    private int direction = DOWN;
    private RelativeLayout relativeLayout;
    private TextView textViewEatenScore, textViewBestScore;
    private AlertDialog.Builder alertDialog;
    int countScore = 0;
    int maxScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);

        relativeLayout = findViewById(R.id.relativeLayout);
        textViewEatenScore = findViewById(R.id.textViewEatenScore);
        textViewBestScore = findViewById(R.id.textViewBestScore);
        alertDialog = new AlertDialog.Builder(this);

        for(int i = 0; i < images.length; i++){
            for(int j = 0; j < images[0].length; j++){
                String imageId = "imageView"+i+j ;
                int resId = getResources().getIdentifier(imageId, "id", getPackageName());
                images[i][j] = findViewById(resId);
            }
        }

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(this){

            public void onSwipeRight() {
                if (direction != LEFT){
                    direction = RIGHT;
                }
            }

            public void onSwipeLeft() {
                if (direction != RIGHT){
                    direction = LEFT;
                }
            }

            public void onSwipeTop() {
                if (direction != DOWN){
                    direction = UP;
                }
            }

            public void onSwipeBottom() {
                if (direction != UP){
                    direction = DOWN;
                }
            }

        });

        MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();

        mutableLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                moveToNext();
            }
        });
        gameLoop = new GameLoop(mutableLiveData);

        startGame();

    }

    public void moveToNext(){
        List<Integer> snakeHead = snake.get(snake.size()-1);
        List<Integer> snakeTail = snake.get(0);
        if(direction == RIGHT) {
            if (snakeHead.get(1) == 10 || array[snakeHead.get(0)][snakeHead.get(1) + 1] == SNAKE) {
                loseGame();
                return;
            }
            images[snakeHead.get(0)][snakeHead.get(1) + 1].setImageResource(R.drawable.head_right);
            if(snake.size() > 1){
                images[snakeHead.get(0)][snakeHead.get(1)].setImageResource(R.drawable.snakebody);
            }
            array[snakeHead.get(0)][snakeHead.get(1) + 1] = SNAKE;
            if (apple[0] == snakeHead.get(0) && apple[1] == snakeHead.get(1) +1){
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.snakebody);
                checkScore();
                reNewApple();
            }else {
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.grass);
                array[snakeTail.get(0)][snakeTail.get(1)] = BLANK;
                snake.remove(0);
            }
            snake.add(Arrays.asList(snakeHead.get(0), snakeHead.get(1) + 1));
        }else if(direction == LEFT) {
            if (snakeHead.get(1) == 0 || array[snakeHead.get(0)][snakeHead.get(1) - 1] == SNAKE) {
                loseGame();
                return;
            }
            images[snakeHead.get(0)][snakeHead.get(1) - 1].setImageResource(R.drawable.head_left);
            if(snake.size() > 1){
                images[snakeHead.get(0)][snakeHead.get(1)].setImageResource(R.drawable.snakebody);
            }
            array[snakeHead.get(0)][snakeHead.get(1) - 1] = SNAKE;
            if (apple[0] == snakeHead.get(0) && apple[1] == snakeHead.get(1) - 1){
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.snakebody);
                checkScore();
                reNewApple();
            }else {
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.grass);
                array[snakeTail.get(0)][snakeTail.get(1)] = BLANK;
                snake.remove(0);
            }
            snake.add(Arrays.asList(snakeHead.get(0), snakeHead.get(1) - 1));

        } else if(direction == UP) {
            if (snakeHead.get(0) == 0 || array[snakeHead.get(0) - 1][snakeHead.get(1)] == SNAKE) {
                loseGame();
                return;
            }
            images[snakeHead.get(0) - 1][snakeHead.get(1)].setImageResource(R.drawable.head_up);
            if(snake.size() > 1){
                images[snakeHead.get(0)][snakeHead.get(1)].setImageResource(R.drawable.snakebody);
            }
            array[snakeHead.get(0) - 1][snakeHead.get(1)] = SNAKE;
            if (apple[0] == snakeHead.get(0) - 1 && apple[1] == snakeHead.get(1)){
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.snakebody);
                checkScore();
                reNewApple();
            }else {
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.grass);
                array[snakeTail.get(0)][snakeTail.get(1)] = BLANK;
                snake.remove(0);
            }
            snake.add(Arrays.asList(snakeHead.get(0) - 1, snakeHead.get(1)));
        }
        else {
            if (snakeHead.get(0) == 4 || array[snakeHead.get(0) + 1][snakeHead.get(1)] == SNAKE) {
                loseGame();
                return;
            }
            images[snakeHead.get(0) + 1][snakeHead.get(1)].setImageResource(R.drawable.head_down);
            if(snake.size() > 1){
                images[snakeHead.get(0)][snakeHead.get(1)].setImageResource(R.drawable.snakebody);
            }
            array[snakeHead.get(0) + 1][snakeHead.get(1)] = SNAKE;
            if (apple[0] == snakeHead.get(0) + 1 && apple[1] == snakeHead.get(1)){
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.snakebody);
                checkScore();
                reNewApple();
            }else {
                images[snakeTail.get(0)][snakeTail.get(1)].setImageResource(R.drawable.grass);
                array[snakeTail.get(0)][snakeTail.get(1)] = BLANK;
                snake.remove(0);
            }
            snake.add(Arrays.asList(snakeHead.get(0) + 1, snakeHead.get(1)));
        }
    }

    private void reNewApple() {
        //Yeni alma Yeri versilmesi ucun
        while (true){
            int i = random.nextInt(5);
            int j = random.nextInt(11);

            if (array[i][j] == BLANK) {
                apple[0] = i;
                apple[1] = j;
                images[i][j].setImageResource(R.drawable.img_1);
                return;
            }
        }
    }

    public void loseGame(){
        gameLoop.stop();
        loseAlertDialog();
    }

    private void loseAlertDialog() {
        View view = View.inflate(this, R.layout.alertview, null);
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button buttonAgain = view.findViewById(R.id.buttonAgain);
        Button buttonExit = view.findViewById(R.id.buttonExit);
        ImageView imageViewClose = view.findViewById(R.id.imageViewClose);

        dialog.setCanceledOnTouchOutside(false);

        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countScore = 0;
                textViewEatenScore.setText(" x"+countScore);
                dialog.dismiss();
                startGame();
            }
        });

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countScore = 0;
                textViewEatenScore.setText(" x"+countScore);
                dialog.dismiss();
                startGame();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
               dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void startGame(){
        for(int[] arr: array){
            Arrays.fill(arr, BLANK);
        }
        snake.clear();
        snake.add(Arrays.asList(0, 0));
        array[0][0] = 1;
        direction = RIGHT;

        for(int i = 0; i < images.length; i++){
            for(int j = 0; j < images[0].length; j++){
                if(array[i][j] == SNAKE){
                    images[i][j].setImageResource(R.drawable.bg_dialog);
                }else{
                    images[i][j].setImageResource(R.drawable.grass);
                }
            }
        }

        reNewApple();
        gameLoop.start();
    }

    public void checkScore(){
        countScore++;
        textViewEatenScore.setText(" x"+countScore);
        if(countScore > maxScore){
            maxScore = countScore;
            textViewBestScore.setText(" x"+maxScore);
        }
    }

}