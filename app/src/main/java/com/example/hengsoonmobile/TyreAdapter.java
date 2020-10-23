package com.example.hengsoonmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Jex on 25/11/2016.
 */

public class TyreAdapter extends ArrayAdapter<Tyre> {

    ArrayList<Tyre> lstTyer = new ArrayList<Tyre>();

    public TyreAdapter(Context context, ArrayList<Tyre> tyres) {
        super(context, 0, tyres);
        lstTyer = tyres;
        // NEED TO FIND OUT HOW TO PASS FRAGMENT TRANSACTION
        //ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Tyre tyre = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tyre_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvBrand = (TextView) convertView.findViewById(R.id.tvBrand);
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvModel);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvSize = (TextView) convertView.findViewById(R.id.tvSize);

        // Populate the data into the template view using the data object
        tvBrand.setText(tyre.getTyreBrand());
        tvModel.setText(tyre.getTyreModel());
        DecimalFormat df = new DecimalFormat("#.00");
        tvPrice.setText("" +  df.format(tyre.getSellingPoint()));
        tvQuantity.setText("Qty. " + tyre.getQuantity());
        tvSize.setText(tyre.getTyreSize());
        tvDate.setText(tyre.getManufactureDate());
        // Return the completed view to render on screen

        /*// Lookup view for data population
        Button btButton = (Button) convertView.findViewById(R.id.btButton);
        // Cache row position inside the button using `setTag`
        btButton.setTag(position);
        // Attach the click event handler
        btButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                User user = getItem(position);
                // Do what you want here...
                Bundle bundle = new Bundle();
                bundle.putParcelable("USER", user);
                UserEditFragment userfrag = new UserEditFragment();
                userfrag.setArguments(bundle);

                FragmentManager fm = ((MainActivity)getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                // Replace the contents of the container with the new fragment
                ft.replace(R.id.your_placeholder, userfrag).addToBackStack("tag");
                // Complete the changes added above
                ft.commit();
            }
        });*/


        return convertView;
    }
}
