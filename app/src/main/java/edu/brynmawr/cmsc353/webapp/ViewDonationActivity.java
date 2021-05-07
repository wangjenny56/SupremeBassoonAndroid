package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewDonationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donation);

        Bundle b = getIntent().getExtras();
        int id = -1;
        ArrayList<String> listings = new ArrayList<>();
        if(b != null) {
            id = b.getInt("id");
            listings = b.getStringArrayList("listings");
        }
        TextView textView = (TextView) findViewById(R.id.donationId);
        textView.setText(listings.get(id));
    }

    //public void onBackClick (View v){
    //    Intent intent = new Intent(ViewDonationActivity.this, DonationFeedActivity.class);
    //    startActivity(intent);
    //    finish();
    //}
}
