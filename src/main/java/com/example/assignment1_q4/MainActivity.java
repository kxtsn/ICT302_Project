package com.example.assignment1_q4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickComputerBtn(View view) {
        mode = "Computer";
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
    public void onClickMultiplayerBtn(View view) {
        mode = "Multiplayer";
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

}