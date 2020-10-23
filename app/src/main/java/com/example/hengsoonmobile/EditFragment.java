package com.example.hengsoonmobile;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditFragment extends Fragment {

    private LinearLayout editPanel;
    private Switch swtEditor;

    private EditText editTextBrand;
    private EditText editTextModel;
    private EditText editTextSize;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextManufactureDate;
    private EditText editTextSellingPoint;

    private Button btnUpdate;
    private ProgressBar spinnerUpdate;

    private String entryKey;
    private int position;
    private String tempref;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_fragment, parent, false);
        Firebase.setAndroidContext(getActivity());
        editPanel = (LinearLayout) v.findViewById(R.id.editPanel);
        swtEditor = (Switch) v.findViewById(R.id.edit_enabler);
        btnUpdate = (Button) v.findViewById(R.id.buttonUpdate);
        spinnerUpdate = (ProgressBar) v.findViewById(R.id.progressUpdateBar);
        //spinnerUpdate.setVisibility(View.GONE);
        editTextBrand = (EditText) v.findViewById(R.id.editTextBrand);
        editTextModel = (EditText) v.findViewById(R.id.editTextModel);
        editTextSize = (EditText) v.findViewById(R.id.editTextSize);
        editTextPrice = (EditText) v.findViewById(R.id.editTextPrice);
        editTextQuantity = (EditText) v.findViewById(R.id.editTextQuantity);
        editTextManufactureDate = (EditText) v.findViewById(R.id.editTextManufactureDate);
        editTextSellingPoint = (EditText) v.findViewById(R.id.editTextSellingPoint);

        swtEditor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    btnUpdate.setEnabled(true);
                    editTextBrand.setEnabled(true);
                    editTextModel.setEnabled(true);
                    editTextPrice.setEnabled(true);
                    editTextQuantity.setEnabled(true);
                    editTextManufactureDate.setEnabled(true);
                    editTextSellingPoint.setEnabled(true);
                } else {
                    btnUpdate.setEnabled(false);
                    editTextBrand.setEnabled(false);
                    editTextModel.setEnabled(false);
                    editTextSize.setEnabled(false);
                    editTextPrice.setEnabled(false);
                    editTextQuantity.setEnabled(false);
                    editTextManufactureDate.setEnabled(false);
                    editTextSellingPoint.setEnabled(false);
                }
            }
        });

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            Tyre chosenTyre = bundle.getParcelable("TYRE");
            tempref = bundle.getString("PARENTREF");
            entryKey = chosenTyre.getUniqueID();
            editTextBrand.setText(chosenTyre.getTyreBrand());
            editTextModel.setText(chosenTyre.getTyreModel());
            editTextSize.setText(chosenTyre.getTyreSize());
            editTextPrice.setText("" + chosenTyre.getPrice());
            editTextQuantity.setText("" + chosenTyre.getQuantity());
            editTextManufactureDate.setText(chosenTyre.getManufactureDate());
            editTextSellingPoint.setText("" + chosenTyre.getSellingPoint());
            String fullSize = editTextSize.getText().toString();
            /*if(fullSize != "") {
                String[] sizeDetails = fullSize.split("-");
                String inch = sizeDetails.length >= 2 ? sizeDetails[sizeDetails.length-1] : "0";
            }*/
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextBrand.getText().toString().equals("") && !editTextModel.getText().toString().equals("") &&
                        !editTextPrice.getText().toString().equals("") && !editTextSellingPoint.getText().toString().equals("")){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    final String brand = editTextBrand.getText().toString();
                    final String model = editTextModel.getText().toString();
                    final String size = editTextSize.getText().toString();
                    final double price = Double.parseDouble(editTextPrice.getText().toString().trim());
                    final int quantity = !editTextQuantity.getText().toString().equals("") ? Integer.parseInt(editTextQuantity.getText().toString().trim()) : 0;
                    final String manufactureDate = !editTextManufactureDate.getText().toString().equals("") ? editTextManufactureDate.getText().toString().trim() : "-";
                    final double retailPrice = Double.parseDouble(editTextSellingPoint.getText().toString().trim());
                    final String parentref = tempref != null ? tempref : "";

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Update Confirmation")
                            .setMessage("Do you want to update " + entryKey + ": " + brand + " " + size + " ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    btnUpdate.setEnabled(false);
                                    spinnerUpdate.setVisibility(View.VISIBLE);
                                    editPanel.requestFocus();

                                    Firebase tyreRef = new Firebase("https://heng-soon-mobile.firebaseio.com/" + parentref);
                                    Tyre editedTyre = new Tyre(brand, model, size, price, quantity, manufactureDate, retailPrice);
                                    String x = entryKey;
                                    tyreRef.child(entryKey).setValue(editedTyre, new Firebase.CompletionListener(){
                                        @Override
                                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                            if (firebaseError != null) {
                                                System.out.println("Data could not be saved " + firebaseError.getMessage());
                                                Toast.makeText(getActivity(), "Data could not be saved " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Data updated successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                            spinnerUpdate.setVisibility(View.GONE);
                                            btnUpdate.setEnabled(true);
                                        }
                                    });
                                }})
                            .setNegativeButton("NO", null).show();
                }
                else{
                    Toast.makeText(getActivity(), "Please fill in all fields...", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return v;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }
}
