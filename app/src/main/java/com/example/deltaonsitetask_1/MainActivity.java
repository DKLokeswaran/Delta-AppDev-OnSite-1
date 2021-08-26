package com.example.deltaonsitetask_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity implements Communicator {
    private Button setTime;
    private ImageButton Start,Stop,Pause;
    private TextView[] timeDisp;
    private long[] Time;
    private long timeInMilli;
    private boolean isTimerSet,isTimerRunning;
    private Intent intent;
    public static final int START=0,PAUSE=1,STOP=2,CLOSED=3;
    private int key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Time=new long[]{0L,0L,0L};
        setTime=findViewById(R.id.button);
        Start=findViewById(R.id.start);
        Stop=findViewById(R.id.stop);
        Pause=findViewById(R.id.pause);
        isTimerRunning=isTimerSet=false;
        timeDisp=new TextView[]{findViewById(R.id.dispHour),findViewById(R.id.dispMin),findViewById(R.id.dispSec)};
        updateTextView();
        key=CLOSED;
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSetter timeSetter=new TimeSetter();
                timeSetter.show(getSupportFragmentManager(),"TIME_SETTER");
            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerSet){
                    key=START;
                    isTimerRunning=true;
                    updateButtons();
                    intent=new Intent(MainActivity.this,TimeService.class);
                    intent.putExtra("Time",convertor());
                    intent.putExtra("key",key);
                    startService(intent);
                    registerReceiver(broadcastReceiver, new IntentFilter(TimeService.BROADCAST_ACTION));

               }
            }
        });
        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key=PAUSE;
                isTimerRunning=false;
                updateButtons();
                getTimer();
                intent.putExtra("key",key);
                unregisterReceiver(broadcastReceiver);
                stopService(intent);
            }
        });

    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Time=backConvertor(intent.getLongExtra("Remaining",0));
            updateButtons();
            updateTextView();
        }
    };


    @Override
    protected void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){

        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(TimeService.BROADCAST_ACTION));

    }
    //
    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this,TimeService.class));
        super.onDestroy();
    }

    @Override
    public void timeGetter(long[] arr) {
        Time=arr;
        isTimerSet=true;
        updateTextView();
    }

    @Override
    public void getTimer(Timer timer) {

    }

    public void updateTextView() {
        for(int i=0;i<3;i++){
            timeDisp[i].setText(String.format("%02d", Time[i]));
        }
    }

    public long convertor(){
        timeInMilli=0;
        timeInMilli+=(Time[2]+(Time[1]*60)+(Time[0]*3600));
        return timeInMilli;
    }
    public void updateButtons(){
        if(!isTimerSet){
            Start.setVisibility(View.VISIBLE);
            Pause.setVisibility(View.INVISIBLE);
        }
        else{
            if(isTimerRunning){
                Start.setVisibility(View.INVISIBLE);
                Pause.setVisibility(View.VISIBLE);
            }
            else {
                Start.setVisibility(View.VISIBLE);
                Pause.setVisibility(View.INVISIBLE);
            }
        }
    }
    public long[] backConvertor(long a){
        long[] arr={0L,0L,0L};
//        a/=1000;
        arr[0]=a/3600;
        a-=arr[0]*3600;
        arr[1]=a/60;
        a-=arr[1]*60;
        arr[2]=a;
        return arr;
    }

}