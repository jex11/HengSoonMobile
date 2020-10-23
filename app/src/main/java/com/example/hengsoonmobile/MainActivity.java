package com.example.hengsoonmobile;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin the transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.frame_holder, new InputFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_12:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_13:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_14:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_15:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_16:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_17:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

}
