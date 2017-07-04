package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/** Screen that allows editing or deletion of an already
 * created contact item
 */
public class DetailViewActivity extends Activity {

    private EditText businessNumField, nameField, addressField;
    private Spinner pBusinessSpinner, provinceSpinner;

    private Contact receivedPersonInfo;
    private MyApplicationData appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        receivedPersonInfo = (Contact)getIntent().getSerializableExtra("Contact");

        appState = ((MyApplicationData) getApplicationContext());

        businessNumField = (EditText) findViewById(R.id.t_editBusinessNumber);
        nameField = (EditText) findViewById(R.id.t_editName);
        pBusinessSpinner = (Spinner) findViewById(R.id.s_editPrimaryBusiness);
        addressField = (EditText) findViewById(R.id.t_editAddress);
        provinceSpinner = (Spinner) findViewById(R.id.s_editProvince);

        if(receivedPersonInfo != null){
            businessNumField.setText(String.valueOf(receivedPersonInfo.businessNum));
            nameField.setText(receivedPersonInfo.name);
            pBusinessSpinner.setSelection(getIndex(pBusinessSpinner, receivedPersonInfo.primaryBusiness));
            addressField.setText(receivedPersonInfo.address);
            provinceSpinner.setSelection(getIndex(provinceSpinner, receivedPersonInfo.province));
        }
    }

    /** Gets index of a spinner where a certain string is
     *
     * @param spinner spinner list
     * @param myString string to find in spinner
     * @return location of string in spinner list
     */
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    /** Gets information from various fields and updates firebase item
     * with information
     * @param v current view
     */
    public void updateContact(View v){
        String personID = receivedPersonInfo.uid;
        try{
            int businessNum = Integer.parseInt(businessNumField.getText().toString());
            String name = nameField.getText().toString();
            String primaryBusiness = pBusinessSpinner.getSelectedItem().toString();
            String address = addressField.getText().toString();
            String province = provinceSpinner.getSelectedItem().toString();

            Contact person = new Contact(personID, businessNum, name, primaryBusiness, address, province);

            appState.firebaseReference.child(personID).setValue(person);
        }
        catch(NumberFormatException e){
        }

        finish();
    }

    /** Deletes the currently opened contact
     *
     * @param v current view
     */
    public void eraseContact(View v)
    {
        String personID = receivedPersonInfo.uid;
        appState.firebaseReference.child(personID).removeValue();

        finish();
    }
}
