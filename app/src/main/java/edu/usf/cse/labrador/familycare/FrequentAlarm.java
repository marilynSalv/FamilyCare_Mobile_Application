package edu.usf.cse.labrador.familycare;

import java.util.ArrayList;

/**
 * Created by Winter on 6/4/2017.
 */

public class FrequentAlarm {
    private String title, time;
    private ArrayList<DayOfFrequentAlarm> alarms = new ArrayList<DayOfFrequentAlarm>();


    public void setTitle(String title){
        this.title = title;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setAlarms(ArrayList<DayOfFrequentAlarm> alarms){
        this.alarms = alarms;
    }

    public ArrayList<DayOfFrequentAlarm> getAlarms(){
        return alarms;
    }
    public String getTitle() {
        return title;
    }
    public String getTime(){
        return time;
    }
    public String getDaysOfWeek(){
        String days = "";
        for(int j=0;j<alarms.size();j++){
            days = days + alarms.get(j).getDayOfWeek() + " ";
        }
        return days;
    }

    @Override
    public String toString() {
        return  "Title: " + title + "\n" +
                getTime() + "\n" +
                getDaysOfWeek() + "\n";
    }
}
