package edu.usf.cse.labrador.familycare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Winter on 6/3/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "YAY");
        setNotification(context, intent);
    }

    public void setNotification(Context context, Intent intent){
        String title = intent.getStringExtra(SetDeleteAlarm.EXTRA_TITLE);
        String time = intent.getStringExtra(SetDeleteAlarm.EXTRA_TIME);
        String date = intent.getStringExtra(SetDeleteAlarm.EXTRA_DATE);

        Intent intentP = new Intent(context, DisplayAlarmsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentP, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Notification notification = builder
                .setContentTitle(title)
                .setContentText("PILL INSTRUCTIONS")
                .setContentInfo(date + "\n" + time)
                .setTicker("Pill Reminder")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSound(sound)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000}).build();
        notificationManager.notify(0, notification);

        int id = intent.getIntExtra(SetDeleteAlarm.EXTRA_ID, -1);
        if(id != -1){
            DatabaseHelper myAlarmDb = new DatabaseHelper(context);
            myAlarmDb.deleteAlarm(id);
        }
    }
}