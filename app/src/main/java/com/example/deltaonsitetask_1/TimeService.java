package com.example.deltaonsitetask_1;

import static com.example.deltaonsitetask_1.MainActivity.PAUSE;
import static com.example.deltaonsitetask_1.MainActivity.STOP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service{

    public static final String BROADCAST_ACTION ="com.example.deltaonsitetask_1.MainActivity";

    private long timeRemaining;
    public static final String NOTIFICATION_CHANNEL="channel";
    public static final int NOTIFICATION_ID=1;
    public static final String NOTIFICATION_NAME="channel1";
//    private NotificationManagerCompat notificationCompat;
//    private Notification notification;
    private Communicator communicator;
    private Timer timer;
    private int key;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timeRemaining=intent.getLongExtra("Time",0);
        key=intent.getIntExtra("key",STOP);
        communicator=(Communicator)this;
//        createNotification();
//        notificationCompat=NotificationManagerCompat.from(this);
        timer=new Timer();
//        countDownTimer=new CountDownTimer(timeRemaining,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Intent newIntent=new Intent();
//                newIntent.setAction(BROADCAST_ACTION);
//                newIntent.putExtra("Remaining",millisUntilFinished);
//                createNotification(backConvertor(millisUntilFinished));
//                sendBroadcast(newIntent);
//            }
//
//            @Override
//            public void onFinish() {
//
//
//            }
//        };
//        countDownTimer.start();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Intent intent1local = new Intent();
                intent1local.setAction(BROADCAST_ACTION);

                timeRemaining--;

                createNotification(backConvertor(timeRemaining));

//                timeRemainingPercent[0] = (timeRemaining[0]/startTime)*100;
                if (timeRemaining <= 0||key==PAUSE){
                    timer.cancel();
                }


                intent1local.putExtra("Remaining", timeRemaining);
//                intent1local.putExtra("remainingPercent", timeRemainingPercent[0]);
                sendBroadcast(intent1local);

            }
        }, 0, 1000);
        return super.onStartCommand(intent, flags, startId);
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
    public void createNotification(long[] arr){
        Intent notification = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notification, 0);
        final Notification[] notifications = {new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setContentTitle("Timer")
                .setContentText("Time Remaining: " + arr[0]+":"+arr[1]+":"+arr[2])
                .setSmallIcon(R.drawable.ic_baseline_stop_24)
                .setContentIntent(pendingIntent)
                .build()};
        startForeground(1, notifications[0]);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
//              NotificationChannel channel=new NotificationChannel(NOTIFICATION_CHANNEL,NOTIFICATION_NAME,NotificationManager.IMPORTANCE_LOW);
//              channel.setDescription("This ia notification");
//              NotificationManager manager=getSystemService(NotificationManager.class);
//              manager.createNotificationChannel(channel);
        }
    }


//    public void Notify(long[] arr){
//        notification=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL).setSmallIcon(R.drawable.ic_baseline_pause_24).setContentTitle("Timer").setContentText("TimeRemaining: " + arr[0]+":"+arr[1]+":"+arr[2]).setPriority(NotificationCompat.PRIORITY_LOW).build();
//        notificationCompat.notify(NOTIFICATION_ID,notification);
//    }


}
