package edu.usf.cse.labrador.familycare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class setUpDashboard extends AsyncTask<Void, Void, Void> {

    Activity activity;
    ProgressDialog progress;
    private ArrayList<Prescription> temp;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user;
    private DatabaseHelper myPresDb;


    public setUpDashboard(Activity activity){
        this.activity = activity;
        temp = new ArrayList<Prescription>();
        myPresDb = new DatabaseHelper(activity);
    }
/*
    public void dynamicDashboard() {
        final LinearLayout lm = (LinearLayout) activity.findViewById(R.id.row_2);
        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        //params for p1 textview
        RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        param4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        for(int j=0;j<temp.size();j++){
            // Create LinearLayout
            RelativeLayout ll = new RelativeLayout(activity);
            ll.setPadding(0,20,0,0);


            // Create TextView
            TextView name = new TextView(activity);
            name.setText(temp.get(j).toString());
            ll.addView(name);

            // Create LinearLayout
            LinearLayout p1 = new LinearLayout(activity);
            p1.setOrientation(LinearLayout.VERTICAL);
            p1.setLayoutParams(param4);

            // Create Button
            final Button btn = new Button(activity);
            btn.setText("Details");
            // set the layoutParams on the button
            btn.setLayoutParams(params);

            final int index = j;
            // Set click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // DETAILS
                    Intent intent = new Intent(activity, MedicationDetails.class);
                    intent.putExtra(Dashboard.MED_NAME, temp.get(index).getDrugName());
                    intent.putExtra(Dashboard.INSTRUCTIONS, temp.get(index).getInstructions());
                    intent.putExtra(Dashboard.SYMPTOMS,temp.get(index).getSymptoms());
                    activity.startActivity(intent);
                }
            });

            p1.addView(btn);

            // Create Button
            final Button btn2 = new Button(activity);
            btn2.setText("Refill");
            // set the layoutParams on the button
            btn2.setLayoutParams(params);

            // Set click listener for button
            btn2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO REFILLS
                    Intent intent = new Intent(activity, Refill.class);
                    intent.putExtra(Dashboard.RX_NUM, temp.get(index).getRx());
                    activity.startActivity(intent);
                }
            });

            p1.addView(btn2);

            ll.addView(p1);
            //Add button to LinearLayout defined in XML
            lm.addView(ll);
        }
    }
*/


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage("Loading...");
        progress.setProgress(10);
        progress.show();
    }

    protected Void doInBackground(Void... Void) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference rxRef = mRootRef.child("prescriptions").child(user.getUid());

        rxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Prescription rx = postSnapshot.getValue(Prescription.class);
                    temp.add(rx);
                    long insertedID = myPresDb.createPrescription(rx);
                    if(insertedID != -1)
                        Log.e("Prescription " + rx.getRx() + ": ", "Saved");
                    else
                        Log.e("Prescription " + rx.getRx() + ": ", "Not Saved");
                }
                //dynamicDashboard();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: ", databaseError.getMessage());
            }
        });
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // do UI work here
        if(progress != null && progress.isShowing()){
            progress.dismiss();
        }
    }

}
