package com.example.hengsoonmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckFragment extends Fragment {

    private Spinner spinner;
    private ProgressBar progressbarDisplay;
    private RadioGroup radioGroup;
    private ListView tyrelv;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_check_fragment, parent, false);
        spinner = (Spinner) v.findViewById(R.id.sizes_spinner);
        tyrelv = (ListView) v.findViewById(R.id.tyrelist_view);
        progressbarDisplay = (ProgressBar) v.findViewById(R.id.progressDisplayBar);
        radioGroup = (RadioGroup) v.findViewById(R.id.radio_tyre_size);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                spinner.setOnClickListener(null);
                progressbarDisplay.setVisibility(View.VISIBLE);
                spinner.setAdapter(null);
                tyrelv.setAdapter(null);
                String value = ((RadioButton) group.getChildAt(group.indexOfChild(group.findViewById(checkedId)))).getText().toString();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("web/data/tyres/" + value);
                final String parentref = "web/data/tyres/" + value;
                final ArrayList<String> sizeList = new ArrayList<String>();
                // Attach a listener to read the data at our posts reference
                ValueEventListener postSizeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sizeList.clear();
                        if(dataSnapshot.getValue() != null) {
                            if(dataSnapshot.getChildrenCount() > 0){
                                sizeList.add("<Select tyre size>");
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String result = child.getKey();
                                    sizeList.add(result);
                                }
                                // Create an ArrayAdapter using the string array and a default spinner layout
                                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizeList);
                                CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizeList, 0);
                                // Apply the adapter to the spinner
                                spinner.setAdapter(adapter);
                                spinner.setSelection(0);
                                progressbarDisplay.setVisibility(View.GONE);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int position, long id) {
                                        if(position > 0){
                                            tyrelv.setAdapter(null);
                                            progressbarDisplay.setVisibility(View.VISIBLE);
                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            final DatabaseReference ref = database.getReference(parentref + "/" + parent.getItemAtPosition(position).toString());
                                            final String updateRef = parentref + "/" + parent.getItemAtPosition(position).toString();
                                            final ArrayList<Tyre> tyreList = new ArrayList<Tyre>();
                                            // Attach a listener to read the data at our posts reference
                                            ValueEventListener postListener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    tyreList.clear();
                                                    if(dataSnapshot.getValue() != null){
                                                        if(dataSnapshot.getChildrenCount() > 0){
                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                Tyre result = child.getValue(Tyre.class);
                                                                String resultID = child.getKey();
                                                                String resultBrand = result.getTyreBrand();
                                                                String resultModel = result.getTyreModel();
                                                                String resultSize = result.getTyreSize();
                                                                double resultPrice = result.getPrice();
                                                                int resultQuantity = result.getQuantity();
                                                                String resultDate = result.getManufactureDate();
                                                                double retailPrice = result.getSellingPoint();
                                                                Tyre newTyre = new Tyre(resultID, resultBrand, resultModel, resultSize, resultPrice, resultQuantity, resultDate, retailPrice, "", "");
                                                                tyreList.add(newTyre);
                                                            }
                                                            if(tyreList != null && tyreList.size() > 0) {
                                                                TyreAdapter adapter = new TyreAdapter(getActivity(), tyreList);
                                                                tyrelv.setAdapter(adapter);
                                                                tyrelv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                        Tyre clickedTyre = (Tyre) parent.getItemAtPosition(position);
                                                                        //swap fragment add here
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putParcelable("TYRE", clickedTyre);
                                                                        bundle.putString("PARENTREF", updateRef);
                                                                        EditFragment ef = new EditFragment();
                                                                        ef.setArguments(bundle);
                                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                        ft.replace(R.id.frame_holder, ef).addToBackStack("tag");
                                                                        ft.commit();
                                                                        //Toast.makeText(getActivity(), clickedTyre.getTyreBrand(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(getActivity(), "No Data...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressbarDisplay.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    System.out.println("The read failed: " + databaseError.getCode());
                                                    Toast.makeText(getActivity(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
                                                    progressbarDisplay.setVisibility(View.GONE);
                                                }
                                            };
                                            ref.addListenerForSingleValueEvent(postListener);
                                        }
                                        else{
                                            tyrelv.setAdapter(null);
                                            Toast.makeText(getActivity(), "Select tyre size...", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "No Data...", Toast.LENGTH_SHORT).show();
                        }
                        progressbarDisplay.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                        Toast.makeText(getActivity(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
                        progressbarDisplay.setVisibility(View.GONE);
                    }
                };
                ref.addListenerForSingleValueEvent(postSizeListener);
            }
        });

        return  v;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }
}
