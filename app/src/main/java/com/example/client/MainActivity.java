package com.example.client;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import datapackage.Data;
import datapackage.Info;

public class MainActivity extends AppCompatActivity {

    private final static int DEATH = 0;
    private final static int ATTACK = 1;
    private final static int ATTACK_COUNTER = 2;
    private final static int DEFEND = 3;
    private final static int DEFEND_COUNTER = 4;
    private final static int DAMAGE_BUFF = 5;
    private final static int BUFF_COUNTER = 6;
    private final static int FINISH = 7;

    Button btn_int;
    TextView txt_info,txt_active;
    ImageView img_card;
    Button btn_select;
    Info info;
    Data DPlayer, DEnemy;
    int iDamage;
    String strlog; // "상대가 O만큼 공격했습니다. 내가 O만큼 공격했습니다." 이런 로그를 나타내는 문자열
    String strlog2; // 버프 체크를 해주는 문자열
    ArrayList<String> battle_log = new ArrayList<>();
    static int turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        btn_int = findViewById(R.id.btn_int);

        btn_int.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DPlayer = new Data();
        DEnemy = new Data();
        info = new Info();
        info.Init();
        DPlayer.SetInfo(info);

        txt_info = (TextView) findViewById(R.id.txt_info);
        txt_active = (TextView) findViewById(R.id.enemy_active);
        img_card = (ImageView) findViewById(R.id.img_card);
        btn_select = (Button) findViewById(R.id.btn_select);

        txt_info.setText(info.GetStat()); //string 을 인자로 넣어줘야함

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), SelectCard.class);

                trans.launch(i);
            }
        });
    }

    ActivityResultLauncher<Intent> trans = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //내가 선택한 행동에 따라서 데이터설정하고 보내기
                Game ga = new Game();
                strlog="";
                strlog2="";
                turn++;

                int iMyChoice = result.getResultCode(); // 혹시 몰라서 변수에 저장해둠


                // 일단 상대방에게 데이터를 먼저 받고 난 후에 분기처리를 진행함.
                // 겸사겸사 내가 무엇을 선택했는지 보냄.
                switch (iMyChoice){
                    case ATTACK:
                        DPlayer.SetChoice(ATTACK);   //Game 클래스안에서 Data클래스의 멤버변수인 iChoice를 Set함
                        img_card.setImageResource(R.drawable.attack);
                        break;
                    case ATTACK_COUNTER:
                        DPlayer.SetChoice(ATTACK_COUNTER);
                        img_card.setImageResource(R.drawable.attack_counter);
                        break;
                    case DEFEND:
                        DPlayer.SetChoice(DEFEND);
                        img_card.setImageResource(R.drawable.armor);
                        break;
                    case DEFEND_COUNTER:
                        DPlayer.SetChoice(DEFEND_COUNTER);
                        img_card.setImageResource(R.drawable.armor_counter);
                        break;
                    case DAMAGE_BUFF:
                        DPlayer.SetChoice(DAMAGE_BUFF);
                        img_card.setImageResource(R.drawable.buff);
                        break;
                    case BUFF_COUNTER:
                        DPlayer.SetChoice(BUFF_COUNTER);
                        img_card.setImageResource(R.drawable.buff_counter);
                        break;
                    case FINISH:
                        DPlayer.SetChoice(FINISH);
                        break;
                }
                if(info.GetHP() <= 0) {// 만약 플레이어의 죽은 상태라면 죽었다고 표시하고 데이터 보냄
                    DPlayer.SetChoice(DEATH);
                }

                // 상대방에게 내가 선택한 정보를 보냄과 동시에 상대방이 선택한 정보를 받아옴
                try {
                    Thread thr = new Thread(ga);
                    thr.start(); // 데이터를 보내고
                    thr.join(); //데이터를 받는다
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 내가 보낸 데이터에 따른 분기처리
                switch (iMyChoice){
                    case ATTACK:
                        iDamage = DPlayer.GetInfo().GetDamage();
                        if(DEnemy.GetChoice() == DEFEND)
                            iDamage -= DEnemy.GetInfo().GetArmor();
                        DEnemy.GetInfo().SetHP(iDamage);
                        strlog=iDamage + "만큼 공격합니다.\n";
                        break;
                    case ATTACK_COUNTER:// ATTACK_COUNTER todo
                        if(DEnemy.GetChoice() == ATTACK) {
                            DEnemy.GetInfo().SetHP(DEnemy.GetInfo().GetDamage());
                            DPlayer.GetInfo().SetHP(-DEnemy.GetInfo().GetDamage());
                            strlog="상대방의 공격을 카운터 했습니다!\n";
                        }
                        else{
                            strlog="상대방의 공격을 카운터 하지 못했습니다.\n";
                        }
                        break;
                    case DEFEND:// DEFEND
                        DPlayer.GetInfo().SetArmor(3);
                        strlog = DPlayer.GetInfo().GetArmor() + "만큼 방어했습니다.\n";
                        break;
                    case DEFEND_COUNTER:// DEFEND_COUNTER todo
                        if(DEnemy.GetChoice() == DEFEND){
                            DEnemy.GetInfo().SetHP(DEnemy.GetInfo().GetArmor());
                            strlog = "상대방의 방어를 카운터 했습니다!\n";
                        }
                        else{
                            strlog = "상대방의 방어를 카운터 하지 못했습니다.\n";
                        }
                        break;
                    case DAMAGE_BUFF:// DAMAGE_BUFF todo
                        DPlayer.GetInfo().SetDamageDouble();
                        DPlayer.GetInfo().SetBuffCount(3);
                        strlog = "자신의 공격력이 2턴 동안 2배가 됩니다.\n";
                        break;
                    case BUFF_COUNTER:// BUFF_COUNTER todo
                        DEnemy.GetInfo().SetBuffCount(-DEnemy.GetInfo().GetBuffCount());
                        strlog = "상대방의 버프를 해제 했습니다!\n";
                        break;
                    case FINISH:// FINISH todo
                        DPlayer.SetChoice(FINISH);
                        break;
                }

                // 상대방이 보낸 데이터에 따른 분기처리
                switch (DEnemy.GetChoice()){
                    case ATTACK:
                        iDamage = DEnemy.GetInfo().GetDamage();
                        if(DPlayer.GetChoice() == DEFEND)
                            iDamage -= DPlayer.GetInfo().GetArmor();
                        DPlayer.GetInfo().SetHP(iDamage);
                        strlog = strlog + "상대방이 "+ iDamage + "만큼 공격합니다. \n";
                        battle_log.add(turn + "번째 턴\n" + strlog);
                        break;
                    case ATTACK_COUNTER:// ATTACK_COUNTER todo
                        if(DPlayer.GetChoice() == ATTACK) {
                            DPlayer.GetInfo().SetHP(DPlayer.GetInfo().GetDamage());
                            DEnemy.GetInfo().SetHP(-DPlayer.GetInfo().GetDamage());
                            strlog = strlog + "상대방이 나의 공격을 카운터 했습니다! \n";
                            battle_log.add(turn + "번째 턴\n" + strlog);
                        }
                        else{
                            strlog = strlog + "상대방이 공격을 카운터 하지 못했습니다. \n";
                            battle_log.add(turn + "번째 턴\n" + strlog);
                        }
                        break;
                    case DEFEND:// DEFEND todo
                        DEnemy.GetInfo().SetArmor(3);
                        strlog = strlog + "상대방이 " + DEnemy.GetInfo().GetArmor() + "만큼 방어했습니다.\n";
                        battle_log.add(turn + "번째 턴\n" + strlog);
                        break;
                    case DEFEND_COUNTER:// DEFEND_COUNTER todo
                        if(DPlayer.GetChoice() == DEFEND){
                            DPlayer.GetInfo().SetHP(DPlayer.GetInfo().GetArmor());
                            strlog = strlog + "상대방이 나의 방어를 카운터 했습니다!\n";
                            battle_log.add(turn + "번째 턴\n" + strlog);
                        }
                        else{
                            strlog = strlog + "상대방이 나의 방어를 카운터 하지 못했습니다.\n";
                            battle_log.add(turn + "번째 턴\n" + strlog);
                        }
                        break;
                    case DAMAGE_BUFF:// DAMAGE_BUFF todo
                        DEnemy.GetInfo().SetDamageDouble();
                        DEnemy.GetInfo().SetBuffCount(3);
                        strlog = strlog + "상대방의 공격력이 2턴 동안 2배가 됩니다.\n";
                        battle_log.add(turn + "번째 턴\n" + strlog);
                        break;
                    case BUFF_COUNTER:// BUFF_COUNTER todo
                        DPlayer.GetInfo().SetBuffCount(-DPlayer.GetInfo().GetBuffCount());
                        strlog = strlog + "상대방이 나의 버프를 해제 했습니다!\n";
                        battle_log.add(turn + "번째 턴\n" + strlog);
                        break;
                    case FINISH:// FINISH todo
                        break;
                }
                if(info.GetHP() <= 0) {
                    txt_info.setText(info.GetStat() + "\n패배했습니다...\n"); // 로그와 플레이어 정보 출력
                    battle_log.add("상대방이 승리했습니다.\n");
                    Toast.makeText(this, "패배했습니다...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                    String gameover = "1";
                    intent.putExtra("gameover", gameover);
                    intent.putStringArrayListExtra("battlelog",battle_log);
                    startActivity(intent);
                    return;
                }

                if (DEnemy.GetInfo().GetHP() <= 0){
                    // 상대방이 죽었을 때의 동작 실행
                    txt_info.setText(info.GetStat() + "\n승리했습니다!\n");
                    battle_log.add("플레이어가 승리했습니다.\n");
                    Toast.makeText(this, "승리했습니다!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                    String gameover = "0";
                    intent.putExtra("gameover", gameover);
                    intent.putStringArrayListExtra("battlelog",battle_log);
                    startActivity(intent);
                    return;
                }

                // 버프 체크
                if(DPlayer.GetInfo().GetBuffCount()>0) {
                    DPlayer.GetInfo().SetBuffCount(-1);
                    if(DPlayer.GetInfo().GetBuffCount() == 0) {
                        DPlayer.GetInfo().SetDamage(5);
                        strlog2 += "버프가 해제되었습니다.\n";
                    }
                }

                if(DEnemy.GetInfo().GetBuffCount()>0) {
                    DEnemy.GetInfo().SetBuffCount(-1);
                    if(DEnemy.GetInfo().GetBuffCount() == 0) {
                        DEnemy.GetInfo().SetDamage(5);
                        strlog2 += "상대방의 버프가 해제되었습니다 \n";
                    }
                }
                txt_active.setText(strlog + "상대방 정보 \n" + DEnemy.GetInfo().GetStat());
//                txt_active.setText(strlog);
                txt_info.setText(info.GetStat() + strlog2); // 로그와 플레이어 정보 출력
            }
    );

    class Game implements Runnable {

        @Override
        public void run() {
            try {
                Socket socket = new Socket("192.168.0.17", 5000);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                out.writeObject((Object) DPlayer); //예외 발생.

                DEnemy = (Data) in.readObject();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
