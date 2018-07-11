package edu.usf.cse.labrador.familycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicationDetails extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);

        TextView medName = (TextView) findViewById(R.id.med_name);
        TextView medInst = (TextView) findViewById(R.id.instructions);
        TextView medSyms = (TextView) findViewById(R.id.symptoms);


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(MedicationDetails.this, Login.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

        Intent intent = getIntent();
        String name = intent.getStringExtra(Dashboard.MED_NAME);
        medName.setText(name);

        String instructions = intent.getStringExtra(Dashboard.INSTRUCTIONS);
        medInst.setText(instructions);

        String symptoms = intent.getStringExtra(Dashboard.SYMPTOMS);
        medSyms.setText(symptoms);

    }


}
