package edu.usf.cse.labrador.familycare;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OneTimeReminderActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Calendar targetCal;
    DatabaseHelper myAlarmDb;
    OneTimeAlarm alarm;
    boolean timeSet, dateSet;
    EditText alarmTitleEdit;
    SetDeleteAlarm setAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_reminder);

        alarmTitleEdit = (EditText) findViewById(R.id.editAlarmTitle);
        setAlarm = new SetDeleteAlarm(OneTimeReminderActivity.this);
        targetCal = Calendar.getInstance();
        alarm = new OneTimeAlarm();
        myAlarmDb = new DatabaseHelper(this);


        timeSet = false;
        dateSet = false;

    }

    public String timeToString(int hour, int min){
        String strHr, strMin, amOrPm;
        //hour
        if (hour > 12 ) {
            strHr = String.valueOf(hour - 12);
            amOrPm = "PM";
        }
        else {
            if(hour == 0)
                strHr = "12";
            else
                strHr = String.valueOf(hour);
            amOrPm = "AM";
        }
        //minute
        strMin = String.valueOf(min);
        if(min < 10)
            strMin = "0" + strMin;

        return strHr + ":" + strMin + " " + amOrPm;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //sets time for alarm object
        targetCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        targetCal.set(Calendar.MINUTE, minute);

        //set time to object and textView
        String time = timeToString(hourOfDay, minute);
        //sets time for alarm object
        alarm.setTime(time);

        //update textView of time
        TextView displayTime = (TextView) findViewById(R.id.displayTime);
        displayTime.setText(time);
        timeSet = true;

    }

    public void setTime(View view){
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog time = new TimePickerDialog(this, OneTimeReminderActivity.this, hour, minute, false);
        time.setTitle("Select a time");
        time.show();
    }

    public void setDate(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OneTimeReminderActivity.this);
        final DatePicker datePicker = new DatePicker(this);
        alertDialog.setView(datePicker);
        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                targetCal.set(Calendar.DAY_OF_MONTH, day);
                targetCal.set(Calendar.MONTH, month);
                targetCal.set(Calendar.YEAR, year);

                String date = month + "/" + day + "/" + year;
                alarm.setDate(date);

                TextView displayDate = (TextView) findViewById(R.id.displayDate);
                displayDate.setText(date);
                dateSet = true;
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialogMain = alertDialog.create();
        alertDialogMain.show();
    }

    public boolean isTitleValid(){
        String alarmTitle = alarmTitleEdit.getText().toString();
        if(TextUtils.isEmpty(alarmTitle)){
            Toast.makeText(OneTimeReminderActivity.this, "No title entered" , Toast.LENGTH_SHORT).show();
            alarmTitleEdit.getText().clear();
            alarmTitleEdit.setHint("ENTER TITLE");
            alarmTitleEdit.setHintTextColor(Color.RED);
            return false;
        }
        else if(myAlarmDb.isTitleUsed(alarmTitle)){
            Toast.makeText(OneTimeReminderActivity.this, "Title is already used" , Toast.LENGTH_SHORT).show();
            alarmTitleEdit.getText().clear();
            alarmTitleEdit.setHint("ENTER ANOTHER TITLE");
            alarmTitleEdit.setHintTextColor(Color.RED);
            return false;
        }
        else {
            //sets alarm's title
            alarm.setTitle(alarmTitle);
            return true;
        }
    }

    public boolean validInput(){
        if(!isTitleValid()){
            return false;
        }
        else if(!timeSet) {
            Toast.makeText(OneTimeReminderActivity.this, "No time set" , Toast.LENGTH_SHORT).show();
            TextView displayTime = (TextView) findViewById(R.id.displayTime);
            displayTime.setText("SELECT A TIME");
            displayTime.setTextColor(Color.RED);
            return false;
        }
        else if(!dateSet) {
            Toast.makeText(OneTimeReminderActivity.this, "No date set" , Toast.LENGTH_SHORT).show();
            TextView displayDate = (TextView) findViewById(R.id.displayDate);
            displayDate.setText("SELECT A DATE");
            displayDate.setTextColor(Color.RED);
            return false;
        }
        else
            return true;
    }

    public void setAlarm(View view){
        if(!validInput()) {
            return;
        }
        // save time in millis to object
        alarm.setTimeInMillis(targetCal.getTimeInMillis());
        //store Alarm in Database and set it up with alarm manager
        long insertedID = myAlarmDb.createOneTimeAlarm(alarm);
        if(insertedID != -1) {
            int id = (int)insertedID;
            alarm.setId(id);
            setAlarm.setOneTimeAlarm(alarm);
            Toast.makeText(OneTimeReminderActivity.this, "Alarm set and saved with ID: " + id, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(OneTimeReminderActivity.this, "Alarm could not be saved" , Toast.LENGTH_SHORT).show();
    }
}
