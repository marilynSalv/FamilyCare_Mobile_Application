package edu.usf.cse.labrador.familycare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;


public class Dashboard extends AppCompatActivity{
    public final static String MED_NAME = "MED_NAME";
    public final static String INSTRUCTIONS = "INSTRUCTIONS";
    public final static String SYMPTOMS = "SYMPTOMS";
    public final static String RX_NUM = "RX_NUM";

    private FirebaseAuth mAuth;

    //private ArrayList<Prescription> rxNum;

    /**BARCODE*/
    // Request code to verify ActivityResult is from barcode capture
    private static final int RC_BARCODE_CAPTURE = 9001;
    // Activity tag for debugging
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(Dashboard.this, Login.class));
        }

        AsyncDashboardOffline setRxList = new AsyncDashboardOffline(this);
        setRxList.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_signOut:
                mAuth.signOut();
                finish();
                Toast.makeText(Dashboard.this, "Signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pillReminder(View view) {
        Intent intent = new Intent(Dashboard.this, DisplayAlarmsActivity.class);
        startActivity(intent);
    }

    public void barCodeScanner(View view) {
        Intent intent = new Intent(Dashboard.this, BarcodeActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    public void contactPharmacy(View view){
        startActivity(new Intent(Dashboard.this, ContactPharmacy.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeActivity.BarcodeObject);
                    // TODO verify valid rx number
                    barcodeDialog(barcode);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(Dashboard.this, "No barcode captured", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(Dashboard.this, "Error reading barcode", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Dialog box when barcode is read successfully
    public void barcodeDialog(Barcode barcode){
        final String rxNum = barcode.displayValue;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Dashboard.this);
        alertDialog.setTitle("Scan Successful");
        alertDialog.setMessage("Send refill request for Rx " + rxNum + "?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Email refill request
                String addr = "receiverefill@gmail.com";
                String subject = String.format("Refill Request for Rx %s", rxNum);
                String body = String.format("Refill Request for Rx %s\n\n%s", rxNum, getString(R.string.confidential));
                sendEmail(addr, subject, body);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialogMain = alertDialog.create();
        alertDialogMain.show();
    }

    private void sendEmail(String addr, String subject, String body){
        SendMail sm = new SendMail(this, addr, subject, body);
        sm.execute();
    }
}


