package edu.usf.cse.labrador.familycare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Winter on 6/4/2017.
 */

public class SetUpDisplayAlarms extends AsyncTask<Void, Void, Void> implements AdapterView.OnItemSelectedListener{

    Activity activity;
    ProgressDialog progress;
    DatabaseHelper myAlarmDb;
    ArrayList<OneTimeAlarm> oneTimeAlarms;
    ArrayList<FrequentAlarm> frequentAlarms;
    ListView alarmList;
    SetDeleteAlarm setAlarm;
    ArrayAdapter<FrequentAlarm> frequentAdapter;
    ArrayAdapter<OneTimeAlarm> oneTimeAdapter;


    public SetUpDisplayAlarms(Activity activity){
        this.activity = activity;
        myAlarmDb = new DatabaseHelper(activity);
        setAlarm = new SetDeleteAlarm(activity);
    }



    public void setUpSpinner(){

        Spinner spinner = (Spinner) activity.findViewById(R.id.menu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.alarmViewOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
    }

    public void setOneTimeAlarmList(){
        alarmList.setAdapter(oneTimeAdapter);
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Confirm Delete?");
                alertDialog.setMessage("Are you sure you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        setAlarm.cancelOneTimeAlarm(oneTimeAlarms.get(position));
                        int alarmId = oneTimeAlarms.get(position).getId();
                        myAlarmDb.deleteAlarm(alarmId);
                        oneTimeAlarms.remove(position);
                        oneTimeAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
                return true;
            }

        });
    }

    public void setFrequentAlarmsList(){
        alarmList.setAdapter(frequentAdapter);
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Confirm Delete?");
                alertDialog.setMessage("Are you sure you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        setAlarm.cancelFrequentAlarm(frequentAlarms.get(position));
                        ArrayList<DayOfFrequentAlarm> daysOfAlarm = frequentAlarms.get(position).getAlarms();
                        String titleDel = frequentAlarms.get(position).getTitle();
                        myAlarmDb.deleteAlarm(titleDel);
                        frequentAlarms.remove(position);
                        frequentAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.show();
                return true;
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        if(pos == 0)
            setOneTimeAlarmList();
        else
            setFrequentAlarmsList();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        setOneTimeAlarmList();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage("Loading...");
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        oneTimeAlarms = myAlarmDb.getAllOneTimeAlarms();
        frequentAlarms = myAlarmDb.getAllFrequentAlarms();
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        setUpSpinner();

        //Sets up Lists views
        alarmList = (ListView) activity.findViewById(R.id.alarm_list);
        frequentAdapter = new ArrayAdapter<FrequentAlarm>(activity, android.R.layout.simple_list_item_1,frequentAlarms);
        oneTimeAdapter = new ArrayAdapter<OneTimeAlarm>(activity, android.R.layout.simple_list_item_1, oneTimeAlarms);

        // do UI work here
        if(progress != null && progress.isShowing()){
            progress.dismiss();
        }
    }

}
