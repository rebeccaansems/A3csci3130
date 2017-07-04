package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateContactAcitivity extends Activity {

    private Button submitButton;
    private EditText nameField, businessNumberField, addressField;
    private Spinner primaryBusinessSpinner, provinceSpinner;
    private MyApplicationData appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact_acitivity);

        //Get the app wide shared variables
        appState = ((MyApplicationData) getApplicationContext());

        submitButton = (Button) findViewById(R.id.submitInfoButton);
        businessNumberField = (EditText) findViewById(R.id.businessNumber);
        nameField = (EditText) findViewById(R.id.name);
        primaryBusinessSpinner = (Spinner) findViewById(R.id.primaryBusiness);
        addressField = (EditText) findViewById(R.id.address);
        provinceSpinner = (Spinner) findViewById(R.id.provinceTerritory);
    }

    public void submitInfoButton(View v) {
        //each entry needs a unique ID
        String personID = appState.firebaseReference.push().getKey();
        try{
            int businessNum = Integer.parseInt(businessNumberField.getText().toString());
            String name = nameField.getText().toString();
            String primaryBusiness = primaryBusinessSpinner.getSelectedItem().toString();
            String address = addressField.getText().toString();
            String province = provinceSpinner.getSelectedItem().toString();

            Contact person = new Contact(personID, businessNum, name, primaryBusiness, address, province);

            appState.firebaseReference.child(personID).setValue(person);
        }
        catch(NumberFormatException e){
        }

        finish();

    }
}
