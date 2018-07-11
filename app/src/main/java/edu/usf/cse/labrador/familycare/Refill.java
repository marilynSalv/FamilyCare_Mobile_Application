package edu.usf.cse.labrador.familycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Refill extends AppCompatActivity implements View.OnClickListener{
    String toEmail = "receiverefill@gmail.com";
    String messageBody = "The information contained in this email is legally privileged and confidential information intended only for the use of the individual or entity to which it is addressed. " +
            "If the reader of this message is not the intended recipient, you are hereby notified that any viewing, dissemination, distribution, or copy of this e-mail message is strictly prohibited. " +
            "If you have received and/or are viewing this e-mail in error, please immediately notify the sender by reply e-mail, and delete this e-mail from your system. " +
            "" +
            "Thank you.";
    String Subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_refill);

        // get Rx number
        Intent intent = getIntent();
        String rxNum = intent.getStringExtra(Dashboard.RX_NUM);
        Subject = "Refill Request for Rx " + rxNum;
        messageBody = Subject + "\n\n" + messageBody;

        Button noButton = (Button) this.findViewById(R.id.noRefill);
        Button yesButton = (Button) this.findViewById(R.id.yesRefill);

        noButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(Refill.this, Dashboard.class));
            }
        });
        yesButton.setOnClickListener(this);
    }

    private void sendEmail()
    {
        SendMail sm = new SendMail(this, toEmail, Subject, messageBody);
        sm.execute();
    }

    @Override
    public void onClick(View v)
    {
        sendEmail();
        startActivity(new Intent(Refill.this, Dashboard.class));


    }
}
