package com.example.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameOverActivity extends AppCompatActivity {

    Button b_restart, b_exit;
    ImageView image;
    TextView log;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        b_restart = findViewById(R.id.button_restart);
        b_exit = findViewById(R.id.button_exit);
        image = findViewById(R.id.image_win);
        log = findViewById(R.id.textView_log);
        log.setText("");

        //승리, 패배값 가져오기
        int gameover = Integer.valueOf(intent.getStringExtra("gameover"));
        ArrayList<String> battle_log = intent.getStringArrayListExtra("battlelog");
        for(int i=0; i< battle_log.size();i++){
            Log.d("로그",battle_log.get(i));
            log.append(battle_log.get(i));
        }

        //승리, 패배 문구 설정, 사진으로 변경 예정
        if(gameover==0){
            image.setImageResource(R.drawable.win);
        }
        else{
            image.setImageResource(R.drawable.lose);
        }

        //재시작 버튼
        b_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                Toast.makeText(GameOverActivity.this, "게임을 재시작합니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //종료 버튼
        b_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
    }
}