package com.example.hengsoonmobile;

import android.app.FragmentTransaction;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.view.filter.ChildChangeAccumulator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InputFragment extends Fragment {

    private LinearLayout inPanel;
    private EditText editTextBrand;
    private EditText editTextModel;
    private EditText editTextSize;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextManufactureDate;
    private EditText editTextSellingPoint;

    private Button buttonSave;
    private Button buttonDisplay;

    private ProgressBar spinnerDisplay;
    private ProgressBar spinnerSave;

    private int position;

//    private final ArrayList<Tyre> tyreList = new ArrayList<Tyre>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_input_fragment, parent, false);
        Firebase.setAndroidContext(getActivity());
        inPanel = (LinearLayout)v.findViewById(R.id.inputPanel);
        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonDisplay = (Button) v.findViewById(R.id.buttonDisplay);
        editTextBrand = (EditText) v.findViewById(R.id.editTextBrand);
        editTextModel = (EditText) v.findViewById(R.id.editTextModel);
        editTextSize = (EditText) v.findViewById(R.id.editTextSize);
        editTextPrice = (EditText) v.findViewById(R.id.editTextPrice);
        editTextQuantity = (EditText) v.findViewById(R.id.editTextQuantity);
        editTextManufactureDate = (EditText) v.findViewById(R.id.editTextManufactureDate);
        editTextSellingPoint = (EditText) v.findViewById(R.id.editTextSellingPoint);
        spinnerDisplay=(ProgressBar) v.findViewById(R.id.progressDisplayBar);
        spinnerSave=(ProgressBar) v.findViewById(R.id.progressSaveBar);
        spinnerDisplay.setVisibility(View.GONE);
        spinnerSave.setVisibility(View.GONE);
        /*radioGroup = (RadioGroup) v.findViewById(R.id.radio_tyre_size);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                position = group.indexOfChild(radioButton);
                checkedradioButton = (RadioButton) group.getChildAt(position);
            }
        });*/

        /*editTextSize.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    String fullSize = editTextSize.getText().toString();
                    if(fullSize != "") {
                        String[] sizeDetails = fullSize.split("-");
                        if(sizeDetails.length >= 2){
                            //MUST CHECK IN RANGE OF RADIO GROUP VALUES!!
                            String inches = sizeDetails[sizeDetails.length-1];
                            int inch = 0;
                            try {
                                inch = Integer.parseInt(inches);
                            } catch (NumberFormatException e) {
                                System.out.println("Wrong number");
                                inch = -1;
                            }
                            if(inch >= 12 && inch <= 18){
                                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                                    RadioButton rbtn = (RadioButton) radioGroup.getChildAt(i);
                                    if(rbtn.getText().toString().equals(inch))
                                        rbtn.setChecked(true);
                                }
                            }
                            else {
                                Toast.makeText(getActivity(), "Invalid value.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });*/

        //Click Listener for button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextBrand.getText().toString().equals("") && !editTextModel.getText().toString().equals("") &&
                        !editTextSize.getText().toString().equals("") && !editTextPrice.getText().toString().equals(""))
                {
                    buttonSave.setEnabled(false);
                    buttonDisplay.setEnabled(false);
                    spinnerSave.setVisibility(View.VISIBLE);

                    inPanel.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//                    View current = getActivity().getCurrentFocus();
//                    if (current != null) current.clearFocus();

                    String brand = editTextBrand.getText().toString().trim();
                    String model = editTextModel.getText().toString().trim();
                    String size = editTextSize.getText().toString().trim();
                    String[] sizeDetails = size.split("-");
                    String inch = sizeDetails.length >= 2 ? sizeDetails[sizeDetails.length-1] : "0";
                    double price = Double.parseDouble(editTextPrice.getText().toString().trim());
                    int quantity = 0;
                    if(!editTextQuantity.getText().toString().equals("")){
                        quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                    }
                    String date = "-";
                    if(!editTextManufactureDate.getText().toString().equals("")){
                        date = editTextManufactureDate.getText().toString().trim();
                    }
                    double retailPrice = Double.parseDouble(editTextSellingPoint.getText().toString().trim());

                    Tyre newTyre = new Tyre(brand, model, size, price, quantity, date, retailPrice);

                    Firebase tyreRef = new Firebase("https://heng-soon-mobile.firebaseio.com/web/data/tyres/" + inch);
                    Firebase specificRef = tyreRef.child(size).push();

                    specificRef.setValue(newTyre, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
                                System.out.println("Data could not be saved " + firebaseError.getMessage());
                                Toast.makeText(getActivity(), "Data could not be saved " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                editTextBrand.setText("");
                                editTextModel.setText("");
                                editTextSize.setText("");
                                editTextPrice.setText("");
                                editTextQuantity.setText("");
                                editTextManufactureDate.setText("");
                                editTextSellingPoint.setText("");
                                Toast.makeText(getActivity(), "Data created successfully.", Toast.LENGTH_SHORT).show();
                            }
                            spinnerSave.setVisibility(View.GONE);
                            buttonSave.setEnabled(true);
                            buttonDisplay.setEnabled(true);
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Please fill in all the data...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Click Listener for button
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setEnabled(false);
                buttonDisplay.setEnabled(false);
                spinnerDisplay.setVisibility(View.VISIBLE);
                CheckFragment cf = new CheckFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_holder, cf).addToBackStack("tag");
                spinnerDisplay.setVisibility(View.GONE);
                ft.commit();


                /*final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("web/data/tyres/" + checkedradioButton.getText().toString());
                final String passedref = "web/data/tyres/" + checkedradioButton.getText().toString();
                final ArrayList<String> sizeList = new ArrayList<String>();
                // Attach a listener to read the data at our posts reference
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("Number of child: ", "" + dataSnapshot.getChildrenCount());
                        String x = dataSnapshot.getValue().toString();
                        sizeList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String result = child.getKey();
                            sizeList.add(result);
                        }
                        //swap fragment add here
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("SIZES", sizeList);
                        bundle.putString("PARENTREF", passedref);
                        //bundle.putString("DATABASEREF", "web/data/tyres/" + checkedradioButton.getText().toString());
                        CheckFragment cf = new CheckFragment();
                        cf.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_holder, cf).addToBackStack("tag");
                        spinnerDisplay.setVisibility(View.GONE);
                        ft.commit();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                        Toast.makeText(getActivity(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                };
                ref.addListenerForSingleValueEvent(postListener);*/

                /*if(radioGroup.getCheckedRadioButtonId() != -1){
                    buttonSave.setEnabled(false);
                    buttonDisplay.setEnabled(false);
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        radioGroup.getChildAt(i).setEnabled(false);
                    }
                    String x = checkedradioButton.getText().toString();
                    spinnerDisplay.setVisibility(View.VISIBLE);
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference("web/data/tyres/" + checkedradioButton.getText().toString());
                    final ArrayList<Tyre> tyreList = new ArrayList<Tyre>();
                    // Attach a listener to read the data at our posts reference
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("Number of child: ", "" + dataSnapshot.getChildrenCount());
                            String x = dataSnapshot.getValue().toString();
                            tyreList.clear();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Tyre result = child.getValue(Tyre.class);
                                String resultID = child.getKey();
                                String resultBrand = result.getTyreBrand();
                                String resultModel = result.getTyreModel();
                                String resultSize = result.getTyreSize();
                                double resultPrice = result.getPrice();
                                int resultQuantity = result.getQuantity();
                                String resultDate = result.getManufactureDate();
                                Tyre newTyre = new Tyre(resultID, resultBrand, resultModel, resultSize, resultPrice, resultQuantity, resultDate);
                                tyreList.add(newTyre);
                            }
                            //swap fragment add here
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("TYRES", tyreList);
                            bundle.putString("DATABASEREF", "web/data/tyres/" + checkedradioButton.getText().toString());
                            TyreListFragment tlf = new TyreListFragment();
                            tlf.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_holder, tlf).addToBackStack("tag");
                            spinnerDisplay.setVisibility(View.GONE);
                            radioGroup.clearCheck();
                            ft.commit();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                            Toast.makeText(getActivity(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    ref.addListenerForSingleValueEvent(postListener);
                }
                else{
                    Toast.makeText(getActivity(), "Please choose which size...", Toast.LENGTH_SHORT).show();
                }*/
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
