package edu.usf.cse.labrador.familycare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Winter on 6/6/2017.
 */

public class AsyncDashboardOffline extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private ProgressDialog progress;
    private DatabaseHelper myPresDb;
    private PrescriptionAdapter adapter;

    public AsyncDashboardOffline(Activity activity){
        this.activity = activity;
        myPresDb = new DatabaseHelper(activity);
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
        Cursor c = myPresDb.getRxItems();
        adapter = new PrescriptionAdapter(activity, c, 0);
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        ListView rxList = (ListView) activity.findViewById(R.id.rxList);
        rxList.setAdapter(adapter);
        // do UI work here
        if(progress != null && progress.isShowing()){
            progress.dismiss();
        }
    }
}
