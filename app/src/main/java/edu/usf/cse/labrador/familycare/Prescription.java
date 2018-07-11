package edu.usf.cse.labrador.familycare;

/**
 * Created by Winter on 3/30/2017.
 */

public class Prescription {
    private String description, doctorName, drugName, expirationDate, instructions, lastFilledDate, qty, refillUntilDate, refillsRemaining, rx, symptoms;

    ///int refillsRemaining, refillUntilDate, qty, lastFilledDate, expirationDate;

    private Prescription(){}

    public Prescription(String description, String doctorName, String drugName, String expirationDate, String instructions, String lastFilledDate, String qty, String refillUntilDate, String refillsRemaining, String rx, String symptoms)
    {
        this.description = description;
        this.doctorName = doctorName;
        this.drugName = drugName;
        this.expirationDate = expirationDate;
        this.instructions = instructions;
        this.lastFilledDate = lastFilledDate;
        this.qty = qty;
        this.refillUntilDate = refillUntilDate;
        this.refillsRemaining = refillsRemaining;
        this.rx = rx;
        this.symptoms = symptoms;

    }

    @Override
    public String toString() {
        return  "Rx: " + rx + "\n" +
                drugName + " " + description + "\n" +
                "Last Filled: " + lastFilledDate + "  " + "Qty: " + qty + "\n" +
                refillsRemaining + " refills until " + refillUntilDate + "\n"
                + doctorName;
    }



    public String getDescription() {
        return description;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public String getDrugName() {
        return drugName;
    }
    public String getExpirationDate() {return expirationDate;}
    public String getInstructions() {return instructions;}
    public String getLastFilledDate() {return lastFilledDate;}
    public String getQty() {return qty;}
    public String getRefillUntilDate() {return refillUntilDate;}
    public String getRefillsRemaining() {return refillsRemaining;}
    public String getRx() {
        return rx;
    }
    public String getSymptoms() {
        return symptoms;
    }





}
