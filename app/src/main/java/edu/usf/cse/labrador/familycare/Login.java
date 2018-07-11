package edu.usf.cse.labrador.familycare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private String emailInput = "";

    private static final String TAG = "Login";
    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
   /*     mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                    startActivity(new Intent(Login.this, Dashboard.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };*/
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void forgotPassword(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Password Reset");
        alertDialog.setMessage("Please enter your email address");
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                emailInput = input.getText().toString();
                if(isValidEmail(emailInput)){
                    mAuth.sendPasswordResetEmail(emailInput)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Password reset sent successfully", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        Toast.makeText(Login.this, "Unable to send password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(Login.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialogMain = alertDialog.create();
        alertDialogMain.show();

    }


    public void submitLogin(View view){
        EditText editTextEmail = (EditText) findViewById(R.id.text_box_email);
        EditText editTextPass = (EditText) findViewById(R.id.text_box_password);

        String email = editTextEmail.getText().toString();
        String password = editTextPass.getText().toString();


        if(!isValidEmail(email)) {
            editTextEmail.getText().clear();
            editTextEmail.setHint("Invalid Email");
            editTextEmail.setHintTextColor(Color.RED);
            return;
        }

        if(TextUtils.isEmpty(password)){
            editTextPass.getText().clear();
            editTextPass.setHint("Invalid Password");
            editTextPass.setHintTextColor(Color.RED);
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                        }

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Succeeded", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(Login.this, Dashboard.class));
                        }

                        // ...
                    }
                });
    }
}
