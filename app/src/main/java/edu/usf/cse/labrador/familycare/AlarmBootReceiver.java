package edu.usf.cse.labrador.familycare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Winter on 6/3/2017.
 */

public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // use database to get alarms
            DatabaseHelper myAlarmDb = new DatabaseHelper(context);
            //used to setAlarm types
            SetDeleteAlarm setAlarm = new SetDeleteAlarm(context);

            //Get all OneTime alarms
            ArrayList<OneTimeAlarm> oneTimeAlarms = myAlarmDb.getAllOneTimeAlarms();

            for(int j=0;j<oneTimeAlarms.size();j++){

                setAlarm.setOneTimeAlarm(oneTimeAlarms.get(j));

            }

            //Today's date
            Calendar cal = Calendar.getInstance();
            final int this_week = cal.get(Calendar.WEEK_OF_YEAR); // this week

            //Get all Frequent alarms and sets them up
            ArrayList<FrequentAlarm> frequentAlarms = myAlarmDb.getAllFrequentAlarms();
            for(int j=0;j<frequentAlarms.size();j++){
                ArrayList<DayOfFrequentAlarm> days = frequentAlarms.get(j).getAlarms();
                for(int i=0;i<days.size();i++){
                    cal.setTimeInMillis(days.get(i).getTimeInMillis());
                    cal.set(Calendar.WEEK_OF_YEAR, this_week);
                    setAlarm.setFrequentAlarm(frequentAlarms.get(j), days.get(i));
                }
            }


        }

    }
}
