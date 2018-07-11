package edu.usf.cse.labrador.familycare;

/**
 * Created by Winter on 6/4/2017.
 */

public class OneTimeAlarm {

    private int id;
    private String title, time, date;
    private long timeInMillis;

    public OneTimeAlarm(){}

    public void setTimeInMillis(long timeInMillis){
        this.timeInMillis = timeInMillis;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setDate(String date){
        this.date = date;
    }


    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public long getTimeInMillis(){
        return timeInMillis;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }

    @Override
    public String toString() {
        return  "Title: " + title + "\n" +
                getTime() + "\n" +
                getDate() + "\n";

    }

}
