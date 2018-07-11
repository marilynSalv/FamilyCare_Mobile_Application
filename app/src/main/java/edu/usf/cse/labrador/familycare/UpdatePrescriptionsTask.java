package edu.usf.cse.labrador.familycare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Winter on 7/3/2017.
 */

public class UpdatePrescriptionsTask extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private ProgressDialog progress;
    private DatabaseHelper myPresDb;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRootRef;

    public UpdatePrescriptionsTask(Activity activity){
        this.activity = activity;
        myPresDb = new DatabaseHelper(activity);
        mRootRef = FirebaseDatabase.getInstance().getReference();
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
        //Cursor c = myPresDb.getRxItems();
        //if(c != null && c.getCount()> 0)  //means rx list has stored prescriptions
          //  myPresDb.deleteAllPrescriptions(); // So delete prescription list to replace
        //updates prescription list
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference rxRef = mRootRef.child("prescriptions").child(user.getUid());

        rxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Prescription rx = postSnapshot.getValue(Prescription.class);
                    Log.e("Phi", "hi");
                    long insertedID = myPresDb.createPrescription(rx);
                    if(insertedID != -1)
                        Log.e("Prescription " + rx.getRx() + ": ", "Saved");
                    else
                        Log.e("Prescription " + rx.getRx() + ": ", "Not Saved");
                }
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
