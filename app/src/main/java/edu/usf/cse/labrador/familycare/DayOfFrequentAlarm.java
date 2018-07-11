package edu.usf.cse.labrador.familycare;

/**
 * Created by Winter on 6/3/2017.
 */

public class DayOfFrequentAlarm {
    private int id;
    private long timeInMillis;
    private String dayOfWeek;


    public void setId(int id){
        this.id = id;
    }
    public void setTimeInMillis(long timeInMillis){
        this.timeInMillis = timeInMillis;
    }
    public void setDayOfWeek(String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }


    public int getId() {
        return id;
    }
    public long getTimeInMillis(){
        return timeInMillis;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
