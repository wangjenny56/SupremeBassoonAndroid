package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NewDonationMadeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation_made);

        TextView textView = (TextView)findViewById(R.id.donation);

        String donationMade = getIntent().getStringExtra("userDonation");
        if(!donationMade.equals("fail")){
            try {
                JSONObject donationJSON = new JSONObject(donationMade);
                String food = donationJSON.getString("foodDescription");
                String message = "Donation for " + food + " successfully listed!";
                textView.setText(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            textView.setText("Donation was unsuccessful.");
        }

    }

    public void onCreateAnotherDonationClick (View v){
        startActivity(new Intent(this, CreateDonationActivity.class));
    }

    public void onViewMenuClick (View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}
