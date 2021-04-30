package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DonationFeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this Activity has a different layout
        setContentView(R.layout.activity_donation_feed);

        // get the argument passed to this Activity via the Intent
        //String item = getIntent().getStringExtra("food_description");
        //List<Map<String, String>> listings = (List<Map<String, String>>) getIntent().getSerializableExtra("food_list");

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        List<Map<String, String>> listings = (List<Map<String, String>>) args.getSerializable("list");

        /*TextView donationView = findViewById(R.id.donationFeedField);
        String foodDesc = listings.get(0).get("food_description");
        String foodType = listings.get(0).get("food_type");
        String name = listings.get(0).get("restaurant");
        donationView.setText(foodDesc + " " + foodType + " " + name);*/

        LinearLayout donationFeed = (LinearLayout) findViewById(R.id.donationLayout); // Root ViewGroup in which you want to add textviews

        for (Map<String, String> map : listings) {
            String name = map.get("restaurant");
            String item = map.get("food_description");
            String quantity = "Quanity: " + map.get("quantity");
            String cuisine = "Cuisine " + map.get("cuisine");

            TextView tv = new TextView(this); // Prepare textview object programmatically
            tv.setText(name + "\n" + item + "\n" + cuisine + "\n" + quantity);
            tv.setBackgroundResource(R.drawable.text_view_style);
            donationFeed.addView(tv);
        }

        /*for (int i = 0; i < 5; i++) {

            TextView tv = new TextView(this); // Prepare textview object programmatically
            tv.setText("Dynamic TextView" + i);
            tv.setId(i + 5);
            tv.setBackgroundResource(R.drawable.text_view_style);
            donationFeed.addView(tv); // Add to your ViewGroup using this method
        }*/
        //donationView.setText(foodDesc);
        //donationView.setText(item);
    }
}
