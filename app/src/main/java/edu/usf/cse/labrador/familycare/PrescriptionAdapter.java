package edu.usf.cse.labrador.familycare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Winter on 6/6/2017.
 */

public class PrescriptionAdapter extends CursorAdapter{
    private LayoutInflater cursorInflater;

    public PrescriptionAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.prescription_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView rxItem = (TextView) view.findViewById(R.id.rx_item);
        Button details = (Button) view.findViewById(R.id.btnDetails);

        final String rx = cursor.getString( cursor.getColumnIndex("_id"));
        final String drugName = cursor.getString(cursor.getColumnIndex("COL_DRUG_NAME"));
        final String description = cursor.getString(cursor.getColumnIndex("COL_DESCRIPTION"));
        final String lastFilledDate = cursor.getString(cursor.getColumnIndex("COL_LAST_FILLED_DATE"));
        final String qty = cursor.getString(cursor.getColumnIndex("COL_QUANTITY"));
        final String refillsRemaining = cursor.getString(cursor.getColumnIndex("COL_REFILLS_REMAINING"));
        final String refillUntilDate = cursor.getString(cursor.getColumnIndex("COL_REFILL_UNTIL_DATE"));
        final String doctorName = cursor.getString(cursor.getColumnIndex("COL_DOCTOR_NAME"));
        final String instructions = cursor.getString(cursor.getColumnIndex("COL_INSTRUCTIONS"));
        final String symptoms = cursor.getString(cursor.getColumnIndex("COL_SYMPTOMS"));

        rxItem.setText("Rx: " + rx + "\n" +
                drugName + " " + description + "\n" +
                "Last Filled: " + lastFilledDate + "  " + "Qty: " + qty + "\n" +
                refillsRemaining + " refills until " + refillUntilDate + "\n"
                + doctorName);

        // DETAILS BUTTON
        details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicationDetails.class);
                intent.putExtra(Dashboard.MED_NAME, drugName);
                intent.putExtra(Dashboard.INSTRUCTIONS, instructions);
                intent.putExtra(Dashboard.SYMPTOMS, symptoms);
                context.startActivity(intent);
            }
        });
        // REFILL BUTTON

    }
}
