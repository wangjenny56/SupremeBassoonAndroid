package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DonationFeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this Activity has a different layout
        setContentView(R.layout.activity_donation_feed);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        List<Map<String, String>> listings = (List<Map<String, String>>) args.getSerializable("list");
        ArrayList<String> allListingsArrayList = new ArrayList<>();

        for (Map<String, String> map : listings) {
            String name = map.get("organization");
            String item = map.get("food_description");
            String quantity = "Quantity: " + map.get("quantity");
            String foodType = "Food Type: " + map.get("food_type");
            String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

            if (map.containsKey("cuisine")) {
                String cuisine = "\n" + "Cuisine " + map.get("cuisine");
                text = text + cuisine;
            }
            allListingsArrayList.add(text);
        }

        ListView donationFeed = (ListView) findViewById(R.id.donationLayout); // Root ViewGroup in which you want to add textviews
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, allListingsArrayList);
        donationFeed.setAdapter(adapter);

        //TextView tv = new TextView(this); // Prepare textview object programmatically
        //tv.setText(text);
        //tv.setBackgroundResource(R.drawable.text_view_style);
        //donationFeed.addView(tv);

        donationFeed.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int donationId = (int) id;
                Intent intent = new Intent(DonationFeedActivity.this, ViewDonationActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", donationId);
                b.putStringArrayList("listings", allListingsArrayList);
                intent.putExtras(b);
                startActivity(intent);
                finish();

            }
        });


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
                    allListingsArrayList.clear();

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
                            String name = map.get("organization");
                            String item = map.get("food_description");
                            String quantity = "Quantity: " + map.get("quantity");
                            String foodType = "Food Type: " + map.get("food_type");
                            String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                            if (map.containsKey("cuisine")) {
                                String cuisine = "\n" + "Cuisine " + map.get("cuisine");
                                text = text + cuisine;
                            }
                            allListingsArrayList.add(text);
                        }

                        adapter.notifyDataSetChanged();

                        //for (Map<String, String> map : listings) {
                        //    String name = map.get("organization");
                        //    String item = map.get("food_description");
                        //    String quantity = "Quanity: " + map.get("quantity");
                        //    String foodType = "Food Type: " + map.get("food_type");
                        //    String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                        //    if(map.containsKey("cuisine")){
                        //        String cuisine = "\n" + "Cuisine " + map.get("cuisine");
                        //        text = text + cuisine;
                        //    }

                        //    TextView tv = new TextView(DonationFeedActivity.this); // Prepare textview object programmatically
                        //    tv.setText(text);
                        //    tv.setBackgroundResource(R.drawable.text_view_style);
                        //    donationFeed.addView(tv);
                        //}
                    }
                    catch (Exception e) {
                        // uh oh
                        e.printStackTrace();
                    }
                }
                else if (s.length() == 0){
                    System.out.println("EMPTY");
                    allListingsArrayList.clear();

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
                            String name = map.get("organization");
                            String item = map.get("food_description");
                            String quantity = "Quantity: " + map.get("quantity");
                            String foodType = "Food Type: " + map.get("food_type");
                            String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                            if (map.containsKey("cuisine")) {
                                String cuisine = "\n" + "Cuisine " + map.get("cuisine");
                                text = text + cuisine;
                            }
                            allListingsArrayList.add(text);
                        }

                        adapter.notifyDataSetChanged();

                        //for (Map<String, String> map : listings) {
                        //    String name = map.get("organization");
                        //    String item = map.get("food_description");
                        //    String quantity = "Quanity: " + map.get("quantity");
                        //    String foodType = "Food Type: " + map.get("food_type");
                        //    String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                        //    if(map.containsKey("cuisine")){
                        //        String cuisine = "\n" + "Cuisine " + map.get("cuisine");
                        //        text = text + cuisine;
                        //    }

                        //    TextView tv = new TextView(DonationFeedActivity.this); // Prepare textview object programmatically
                        //    tv.setText(text);
                        //    tv.setBackgroundResource(R.drawable.text_view_style);
                        //    donationFeed.addView(tv);
                        //}
                    }
                    catch (Exception e) {
                        // uh oh
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

}
