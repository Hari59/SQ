package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toMenu(View V)
    {
        //loads the game play screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toLevelSelect(View V)
    {
        //loads the game play screen
        Intent intent = new Intent(this, LevelSelect.class);
        startActivity(intent);
    }
}
