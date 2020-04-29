package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gamePlayScreen(View V)
    {
        //loads the game play screen
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void settingScreen(View V)
    {
        //loads the setting screen (overlay
    }

}
