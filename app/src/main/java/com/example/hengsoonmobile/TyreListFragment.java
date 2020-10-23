package com.example.hengsoonmobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TyreListFragment extends Fragment {

    private ListView listView;
    ArrayList<Tyre> lstTyre = new ArrayList<Tyre>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tyre_list_fragment, parent, false);
        listView = (ListView) v.findViewById(R.id.tyrelist_holder);
        Bundle bundle = this.getArguments();
        DatabaseReference ref = null;
        if(bundle != null)
        {
            final String firebaseRef = bundle.getString("DATABASEREF");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference(firebaseRef);
            lstTyre = bundle.getParcelableArrayList("TYRES");
            for(final Tyre tyre : lstTyre){
                String a = tyre.getUniqueID();
                String b = tyre.getTyreBrand();
                String c = tyre.getTyreModel();
                String d = tyre.getTyreSize();
                Double e = tyre.getPrice();
                int f = tyre.getQuantity();
            }
        }

        // Attach a listener to read the data at our posts reference
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Number of child: ", "" + dataSnapshot.getChildrenCount());
                String x = dataSnapshot.getValue().toString();
                lstTyre.clear();
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
                    lstTyre.add(newTyre);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(getActivity(), "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        };
        ref.addValueEventListener(postListener);

        TyreAdapter adapter = new TyreAdapter(getActivity(), lstTyre);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tyre clickedTyre = (Tyre) parent.getItemAtPosition(position);
                //swap fragment add here
                Bundle bundle = new Bundle();
                bundle.putParcelable("TYRE", clickedTyre);
                EditFragment ef = new EditFragment();
                ef.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_holder, ef).addToBackStack("tag");
                ft.commit();
                //Toast.makeText(getActivity(), clickedTyre.getTyreBrand(), Toast.LENGTH_SHORT).show();
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
