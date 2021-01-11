package com.websarva.wings.android.droidsoftsecond;

import android.app.Service;

public abstract class s001_BaseTaskService extends Service {
        //Serviceを使っているので、非同期ではないみたい。でも非同期にする必要もないので、このまま進める。
/*
    private static final String CHANNEL_ID_DEFAULT = "default";
    static final int PROGRESS_NOTIFICATION_ID = 0;
    static final int FINISHED_NOTIFICATION_ID = 1;

    private static final String TAG = "MyBaseTaskService";
    private int mNumTasks = 0;

    public void taskStarted(){changeNumberOfTasks(1);}
    public void taskCompleted(){changeNumberOfTasks(-1);}

    private synchronized void changeNumberOfTasks(int delta){
        Log.d(TAG, "changeNumberOfTasks:" + mNumTasks + ":" + delta);
        mNumTasks += delta;

        if(mNumTasks <= 0){
            Log.d(TAG,"stopping");
            stopSelf();
        }
    };

//deltaって何？そもそもmNumTasksとはなんなのか。

    private void createDefaultChannel(){
        //
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);//getSystemServiceとは？
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT, "Default", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }
    }
    //デフォルトのチャンネルということはデフォルトではないチャンネルも作るということ？


    protected void showProgressNotification(String caption, long completedUnits, long totalUnits){
        int percentComplete = 0;
        if (totalUnits>0){
            percentComplete = (int)(100* completedUnits/totalUnits);
        }

        createDefaultChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.drawable.ic_file_upload_white_24dp)
                .setContentTitle("保存")
                .setContentText(caption)
                .setProgress(100, percentComplete, false)
                .setOngoing(true)
                .setAutoCancel(false);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(PROGRESS_NOTIFICATION_ID, builder.build());
    }*/



}
