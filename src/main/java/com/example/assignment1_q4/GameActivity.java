package com.example.assignment1_q4;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Matrix grid;
    private TextView message;
    private int size;
    private int round;
    private int player;
    private boolean gameOver;
    private String mode = MainActivity.mode;

    //store value of cell when orientation change
    private Queue<String> storeCellValue = new LinkedList<>();

    //referring to the center 3 by 3 cells
    private int[] landBtnPlacement = {
            R.id.button7, R.id.button8, R.id.button9,
            R.id.button12, R.id.button13, R.id.button14,
            R.id.button17, R.id.button18, R.id.button19 };

    //all buttons for easy calling in loops
    private int[] cellBtn = {
            R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10,
            R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15,
            R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20,
            R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        round = 0;
        player = 1;
        gameOver = false;
        message = findViewById(R.id.textView);
        message.setText("Player 1's turn [X]");

        switch (getResources().getConfiguration().orientation) {
            case (Configuration
                    .ORIENTATION_PORTRAIT):
                size = 3;
                grid = new Matrix(size);
                break;
            case (Configuration.ORIENTATION_LANDSCAPE):
                if (savedInstanceState != null){
                    grid = (Matrix) savedInstanceState.getSerializable("GRID");
                    player = savedInstanceState.getInt("PLAYER_TURN");
                    round = savedInstanceState.getInt("ROUND");
                    gameOver = savedInstanceState.getBoolean("GAME_OVER");
                    message.setText(savedInstanceState.getString("MESSAGE"));
                    restoreCellOccupancy();
                    size = 5;
                    grid = new Matrix(size);
                    restoreMatrix();
                } else {
                    size = 5;
                    grid = new Matrix(size);
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("GRID",grid);
        savedInstanceState.putInt("PLAYER_TURN",player);
        savedInstanceState.putInt("ROUND",round);
        savedInstanceState.putBoolean("GAME_OVER",gameOver);
        savedInstanceState.putString("MESSAGE",(String)message.getText());
    }

    //restoring of cell button occupancy
    private void restoreCellOccupancy(){
        //cell no = button id no
        int i = 0;
        //row
        for (int j = 0; j < 3; j++) {
            //column
            for (int k = 0; k < 3; k++) {
                //get button in view
                Button b = findViewById(landBtnPlacement[i]);
                // get cell value from matrix
                String cellValue = grid.get(j, k);
                if (cellValue != "-") {
                    //set button text
                    b.setText(cellValue);
                    storeCellValue.add(cellValue);
                } else {
                    storeCellValue.add("-");
                }
                i++;
            }
        }
    }

    //restore matrix with cell occupancy
    private void restoreMatrix(){
            // coordinate for row
        for(int j = 1; j < 4; j++){
                //coordinate for column
            for (int k =1; k < 4; k++){
                //set value in matrix
                grid.set(j,k,storeCellValue.poll());
            }
        }
    }

    public void onClickResetBtn(View view) {
        grid = new Matrix(size);
        message = findViewById(R.id.textView);
        message.setText("Player 1's turn [X]");
        round = 0;
        player = 1;
        gameOver = false;

        for (int i = 0; i < size*size; i++) {
            Button b = findViewById(cellBtn[i]);
            b.setText("");
        }
    }

    public void onClickBtn(View view){
        if (!gameOver){
            Button b = (Button)view;
            String text = b.getText().toString();
            if(text.equals("X") || text.equals("O")){
                message.setText("Cell is occupied");
            } else {
                if (mode.equals("Multiplayer") && player == 2 || player == 1){
                    playerTurn(b);
                }
            }
        }
    }

    private void setCell(int tag){
        round ++;
        int row = (tag-1)/size;
        int col = (tag-1)%size;
        grid.set(row,col,getOption(player));
        checkWinner();
        nextPlayer();

        if(mode.equals("Computer") && player == 2){
            computerTurn();
        }
    }

    private void playerTurn(Button b){
        String option = getOption(player);
        b.setText(option);
        int tag = Integer.parseInt(b.getTag().toString());
        setCell(tag);
    }

    private void computerTurn(){
        if(!gameOver) {
            int max = (size * size) - 1;
            int min = 0;
            Button btn = findViewById(cellBtn[0]);
            boolean empty = false;
            while (!empty) {
                int random = new Random().nextInt(max - min + 1) + min;
                btn = findViewById(cellBtn[random]);
                if(btn.getText().toString().equals("")){
                    empty = true;
                }
            }
            int tag = Integer.parseInt(btn.getTag().toString());
            btn.setText("O");
            setCell(tag);
        }
    }

    private void checkWinner(){
        int winner = 0;
        if (checkIfWin(1)){
            winner = 1;
        } else if (checkIfWin(2)){
            winner = 2;
        }

        if(winner != 0) {
            gameOver = true;
            printWinner(winner);
        } else {
            checkDraw();
        }
    }

    private String getOption(int player){
        String option = "";
        if(player == 1){
            option = "X";
        } else if (player == 2){
            option = "O";
        }
        return option;
    }

    private boolean checkIfWin(int player){
        String option = getOption(player);
        boolean winner = false;
        int row = 0, column = 0, diagonal1 = 0, diagonal2 = 0;

        //check through diagonals
        for(int i = 0; i < size; i++){
            if(option.equalsIgnoreCase(grid.get(i,i))){
                diagonal1++;
            }
            if(option.equalsIgnoreCase(grid.get(i,(size-1-i)))){
                diagonal2++;
            }
        }

        //check through rows and columns
        for(int i = 0; i < size; i ++){
            row = column = 0;
            for(int j = 0; j < size; j++) {
                //(0,0),(1,0),(2,0)
                if(option.equalsIgnoreCase(grid.get(j,i))){
                    row++;
                    if(row == size){
                        break;
                    }
                }
                if (option.equalsIgnoreCase(grid.get(i,j))){
                    column++;
                    if(column == size){
                        break;
                    }
                }
            }

            if(row == size || column == size){
                break;
            }
        }
        if(row == size || column == size || diagonal1 == size || diagonal2 == size){
            winner = true;
        }

        return winner;
    }

    private void printWinner(int player){
        if(player == 1){
            message.setText("Player 1 wins!!");
        } else if (player == 2 && mode.equals("Multiplayer")){
            message.setText("Player 2 wins!!");
        } else if (player == 2 && mode.equals("Computer")){
            message.setText("Computer Wins!!");
        }
        gameOver = true;
    }

    private void checkDraw(){
        if (round == size*size){
            message.setText("Draw!!");
            gameOver = true;
        }
    }

    private void nextPlayer() {
        if (!gameOver) {
            if (player == 1) {
                player = 2;
                message.setText("Player 2's turn [O]");
            } else {
                player = 1;
                message.setText("Player 1's turn [X]");
            }
        }
    }
}