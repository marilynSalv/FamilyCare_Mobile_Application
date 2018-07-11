package edu.usf.cse.labrador.familycare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Winter on 6/4/2017.
 */

public class SetDeleteAlarm {

    public final static String EXTRA_ID = "ID";
    public final static String EXTRA_TITLE = "TITLE";
    public final static String EXTRA_TIME = "TIME";
    public final static String EXTRA_DATE = "DATE";

    private Context context;
    private AlarmManager alarmManager;
    private final static int ALARM_INTERVAL = 24 * 7 * 60 * 60 * 1000;

    public SetDeleteAlarm(Context context) {
        this.context = context;
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setOneTimeAlarm(OneTimeAlarm alarm) {

        Intent my_intent = new Intent(context, AlarmReceiver.class);
        my_intent.putExtra(EXTRA_ID, alarm.getId());
        my_intent.putExtra(EXTRA_TITLE, alarm.getTitle());
        my_intent.putExtra(EXTRA_TIME, alarm.getTime());
        my_intent.putExtra(EXTRA_DATE, alarm.getDate());

        PendingIntent pending_Intent = PendingIntent.getBroadcast(context, alarm.getId(), my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pending_Intent);

    }
    public void setFrequentAlarm(FrequentAlarm alarm, DayOfFrequentAlarm day) {
        if(day.getTimeInMillis() < System.currentTimeMillis()){
            Calendar targetCal = Calendar.getInstance();
            targetCal.setTimeInMillis(day.getTimeInMillis());
            int i = targetCal.get(Calendar.WEEK_OF_YEAR);
            targetCal.set(Calendar.WEEK_OF_YEAR, ++i);
            day.setTimeInMillis(targetCal.getTimeInMillis());
            Log.e("was less " + day.getDayOfWeek() +": ", targetCal.getTime().toString());
        }
        else{
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(day.getTimeInMillis());
            Log.e("was right " + day.getDayOfWeek() +": ", cal.getTime().toString());
        }

        Intent my_intent = new Intent(context, AlarmReceiver.class);
        my_intent.putExtra(EXTRA_TITLE, alarm.getTitle());
        my_intent.putExtra(EXTRA_TIME, alarm.getTime());
        my_intent.putExtra(EXTRA_DATE, day.getDayOfWeek());

        PendingIntent pending_Intent = PendingIntent.getBroadcast(context, day.getId(), my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, day.getTimeInMillis(), ALARM_INTERVAL, pending_Intent);
    }

    public void cancelOneTimeAlarm(OneTimeAlarm alarm){
        Intent my_intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pending_Intent = PendingIntent.getBroadcast(context, alarm.getId(), my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pending_Intent);
    }

    public void cancelFrequentAlarm(FrequentAlarm alarm){
        Intent my_intent = new Intent(context, AlarmReceiver.class);
        ArrayList<DayOfFrequentAlarm> daysOfAlarm = alarm.getAlarms();
        for(int j=0;j<daysOfAlarm.size();j++){
            PendingIntent pending_Intent = PendingIntent.getBroadcast(context,daysOfAlarm.get(j).getId(), my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pending_Intent);
        }
    }

}
