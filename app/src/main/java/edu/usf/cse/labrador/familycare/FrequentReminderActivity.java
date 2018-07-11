package edu.usf.cse.labrador.familycare;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class FrequentReminderActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Calendar targetCal;
    FrequentAlarm alarm = null;
    boolean timeSet;
    DatabaseHelper myAlarmDb;
    EditText alarmTitleEdit;
    SetDeleteAlarm setAlarm ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequent_reminder);

        //Initialize variables
        alarmTitleEdit = (EditText) findViewById(R.id.editAlarmTitle2);
        myAlarmDb = new DatabaseHelper(this);
        setAlarm = new SetDeleteAlarm(FrequentReminderActivity.this);
        targetCal = Calendar.getInstance();
        alarm = new FrequentAlarm();
        timeSet = false;
    }

    public void setForDay(int intDayOfWeek, String strDayOfWeek){
        DayOfFrequentAlarm day = new DayOfFrequentAlarm();

        targetCal.set(Calendar.DAY_OF_WEEK, intDayOfWeek);
        day.setTimeInMillis(targetCal.getTimeInMillis());
        day.setDayOfWeek(strDayOfWeek);
        long insertedID = myAlarmDb.createFrequentAlarm(alarm, day);
        if(insertedID != -1) {
            int id = (int)insertedID;
            day.setId(id);
            setAlarm.setFrequentAlarm(alarm, day);
            Toast.makeText(FrequentReminderActivity.this, "Alarm set and saved with ID: " + id, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(FrequentReminderActivity.this, "Alarm could not be saved" , Toast.LENGTH_SHORT).show();
    }

    public void setAlarmForDaysOfWeek(){
        ToggleButton toggleSun = (ToggleButton) findViewById(R.id.Sun);
        ToggleButton toggleM = (ToggleButton) findViewById(R.id.M);
        ToggleButton toggleT = (ToggleButton) findViewById(R.id.T);
        ToggleButton toggleW = (ToggleButton) findViewById(R.id.W);
        ToggleButton toggleTh = (ToggleButton) findViewById(R.id.Th);
        ToggleButton toggleF = (ToggleButton) findViewById(R.id.F);
        ToggleButton toggleSat = (ToggleButton) findViewById(R.id.Sat);

        if(toggleSun.isChecked()){
            setForDay(Calendar.SUNDAY, "Sun");
        }
        if(toggleM.isChecked()){
            setForDay(Calendar.MONDAY, "M");
        }
        if(toggleT.isChecked()){
            setForDay(Calendar.TUESDAY, "T");
        }
        if(toggleW.isChecked()){
            setForDay(Calendar.WEDNESDAY, "W");
        }
        if(toggleTh.isChecked()){
            setForDay(Calendar.THURSDAY, "Th");
        }
        if(toggleF.isChecked()){
            setForDay(Calendar.FRIDAY, "F");
        }
        if(toggleSat.isChecked()){
            setForDay(Calendar.SATURDAY, "Sat");
        }
        else
            Toast.makeText(FrequentReminderActivity.this, "No day selected for alarm", Toast.LENGTH_SHORT).show();
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
        alarm.setTime(time);
        //update textView of time
        TextView displayTime = (TextView) findViewById(R.id.displayTime2);
        displayTime.setText(time);
        timeSet = true;
    }

    public void setTime(View view){
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog time = new TimePickerDialog(this, FrequentReminderActivity.this, hour, minute, false);
        time.setTitle("Select a time");
        time.show();
    }

    public boolean isTitleValid(){
        String alarmTitle = alarmTitleEdit.getText().toString();
        if(TextUtils.isEmpty(alarmTitle)){
            Toast.makeText(FrequentReminderActivity.this, "No title entered" , Toast.LENGTH_SHORT).show();
            alarmTitleEdit.getText().clear();
            alarmTitleEdit.setHint("ENTER TITLE");
            alarmTitleEdit.setHintTextColor(Color.RED);
            return false;
        }
        else if(myAlarmDb.isTitleUsed(alarmTitle)){
            Toast.makeText(FrequentReminderActivity.this, "Title is already used" , Toast.LENGTH_SHORT).show();
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
            Toast.makeText(FrequentReminderActivity.this, "No time set" , Toast.LENGTH_SHORT).show();
            TextView displayTime = (TextView) findViewById(R.id.displayTime2);
            displayTime.setText("SELECT A TIME");
            displayTime.setTextColor(Color.RED);
            return false;
        }
        else
            return true;
    }

    public void setAlarm(View view){
        if(!validInput()) {
            return;
        }
        setAlarmForDaysOfWeek();
    }
}
