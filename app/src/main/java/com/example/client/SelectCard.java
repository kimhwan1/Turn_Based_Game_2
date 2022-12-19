package com.example.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class SelectCard extends Activity {

    private final static int ATTACK = 1;
    private final static int ATTACK_COUNTER = 2;
    private final static int DEFEND = 3;
    private final static int DEFEND_COUNTER = 4;
    private final static int DAMAGE_BUFF = 5;
    private final static int BUFF_COUNTER = 6;
    private final static int FINISH = 7;

    ImageButton ATTACK_BUTTON, ATTACK_COUNTER_BUTTON, DEFEND_BUTTON, DEFEND_COUNTER_BUTTON, DAMAGE_BUFF_BUTTON
            , BUFF_COUNTER_BUTTON;
    Button FINISH_BUTTON;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);

        ATTACK_BUTTON = (ImageButton)findViewById(R.id.ATTACK_BUTTON);
        ATTACK_COUNTER_BUTTON = (ImageButton)findViewById(R.id.ATTACK_COUNTER_BUTTON);
        DEFEND_BUTTON = (ImageButton)findViewById(R.id.DEFEND_BUTTON);
        DEFEND_COUNTER_BUTTON = (ImageButton)findViewById(R.id.DEFEND_COUNTER_BUTTON);
        DAMAGE_BUFF_BUTTON = (ImageButton)findViewById(R.id.DAMAGE_BUFF_BUTTON);
        BUFF_COUNTER_BUTTON = (ImageButton)findViewById(R.id.BUFF_COUNTER_BUTTON);
        FINISH_BUTTON = (Button)findViewById(R.id.FINISH_BUTTON);

        ATTACK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                setResult(ATTACK);

                finish();
            }
        });

        ATTACK_COUNTER_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(ATTACK_COUNTER);

                finish();
            }
        });

        DEFEND_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(DEFEND);

            }
        });

        DEFEND_COUNTER_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(DEFEND_COUNTER);

                finish();
            }
        });

        DAMAGE_BUFF_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(DAMAGE_BUFF);

                finish();
            }
        });

        BUFF_COUNTER_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(BUFF_COUNTER);

                finish();
            }
        });

        FINISH_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(FINISH);

                finish();
            }
        });


    }
}