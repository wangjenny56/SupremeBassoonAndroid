package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URL;
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


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        List<Map<String, String>> listings = (List<Map<String, String>>) args.getSerializable("list");

        LinearLayout donationFeed = (LinearLayout) findViewById(R.id.donationLayout); // Root ViewGroup in which you want to add textviews
        //TextView tv = new TextView(this); // Prepare textview object programmatically

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

        EditText userZipcode = findViewById(R.id.userZipcode);

        userZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 5){
                    System.out.println(s.toString());
                    donationFeed.removeAllViews();
                    donationFeed.invalidate();

                    try {
                        String zipUrl = "http://10.0.2.2:3000/viewListingsForSocialService?zipcode=" + s.toString();
                        URL url = new URL(zipUrl);
                        AccessWebTask task = new AccessWebTask();
                        task.execute(url);
                        List<Map<String, String>> listings = task.get();
                        System.out.println("The size is " + listings.size());
                        for (Map<String, String> map : listings) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                System.out.println(key + " " + value);
                            }
                        }

                        for (Map<String, String> map : listings) {
                            String name = map.get("restaurant");
                            String item = map.get("food_description");
                            String quantity = "Quanity: " + map.get("quantity");
                            String cuisine = "Cuisine " + map.get("cuisine");

                            TextView tv = new TextView(DonationFeedActivity.this); // Prepare textview object programmatically
                            tv.setText(name + "\n" + item + "\n" + cuisine + "\n" + quantity);
                            tv.setBackgroundResource(R.drawable.text_view_style);
                            donationFeed.addView(tv);
                        }
                    }
                    catch (Exception e) {
                        // uh oh
                        e.printStackTrace();
                    }
                }
                else if (s.length() == 0){
                    System.out.println("EMPTY");
                    donationFeed.removeAllViews();
                    donationFeed.invalidate();

                    try {
                        String zipUrl = "http://10.0.2.2:3000/viewListingsForSocialService?zipcode=";
                        URL url = new URL(zipUrl);
                        AccessWebTask task = new AccessWebTask();
                        task.execute(url);
                        List<Map<String, String>> listings = task.get();
                        System.out.println("The size is " + listings.size());
                        for (Map<String, String> map : listings) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                System.out.println(key + " " + value);
                            }
                        }

                        for (Map<String, String> map : listings) {
                            String name = map.get("restaurant");
                            String item = map.get("food_description");
                            String quantity = "Quanity: " + map.get("quantity");
                            String cuisine = "Cuisine " + map.get("cuisine");

                            TextView tv = new TextView(DonationFeedActivity.this); // Prepare textview object programmatically
                            tv.setText(name + "\n" + item + "\n" + cuisine + "\n" + quantity);
                            tv.setBackgroundResource(R.drawable.text_view_style);
                            donationFeed.addView(tv);
                        }
                    }
                    catch (Exception e) {
                        // uh oh
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
