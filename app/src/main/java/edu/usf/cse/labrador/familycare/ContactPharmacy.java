package edu.usf.cse.labrador.familycare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class ContactPharmacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_pharmacy);
    }

    public void sendMessage(View view){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        String aEmailList[] = {"jwomble@fdprx.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        startActivity(emailIntent);
    }

    public void callPharmacy(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9419149991"));
        startActivity(intent);
    }

    public void pharmacyRoute(View view){
        startActivity(new Intent(ContactPharmacy.this,MapsActivity.class));
    }
}
