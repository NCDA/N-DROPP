package com.project.vanswa.ndropp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);



        //create a view for the create game screen
        //create a view for the join game screen
        //link the buttons to intents
        //link intents to start views
    }

    public void createGame(View view){
        Intent intent = new Intent(getApplicationContext(),CreateGame.class);
        startActivity(intent);
    }
}

