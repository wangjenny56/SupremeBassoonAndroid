package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DonationFeedActivity extends AppCompatActivity {
    // list of items for the cuisine filter spinner
    ArrayList<String> allCuisines = new ArrayList<>();
    // list of items for the perishability filter spinner
    ArrayList<String> allPerishabilities = new ArrayList<>();
    // all of the donation listings received from main activity for user's hardcoded zipcode: 19010
    List<Map<String, String>> listings;
    // arraylist of all listings to be shown on feed
    ArrayList<String> allListingsArrayList = new ArrayList<>();
    // adapts allCuisines arraylist into cuisine spinner
    ArrayAdapter<String> cuisineAdapterSpinner;
    // adapts allPerishabilities arraylist into perishability spinner
    ArrayAdapter<String> perishAdapterSpinner;
    ArrayAdapter adapter;

    // current filters on donation listing feed
    String zipcodeInput = "19010"; // hardcoded for this user
    String currCuisineFiltering = "No Cuisine Filter";
    String currPerishabilityFilter = "No Time Filter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this Activity has a different layout
        setContentView(R.layout.activity_donation_feed);
        // showing the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // extras received from main activity (all of the donation listings for user's hardcoded zipcode: 19010)
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        listings = (List<Map<String, String>>) args.getSerializable("list");


        allCuisines.add("No Cuisine Filter");
        allPerishabilities.add("No Time Filter");

        // creating the arraylist of all listings (unfiltered) to be shown on feed
        for (Map<String, String> map : listings) {
            String name = map.get("organization");
            String item = "Food Description: " + map.get("food_description");
            String quantity = "Quantity: " + map.get("quantity");
            String foodType = "Food Type: " + map.get("food_type");
            String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

            if (map.containsKey("cuisine")) {
                if (! map.get("cuisine").equals("")) {
                    String cuisine = "\n" + "Cuisine: " + map.get("cuisine").toLowerCase();
                    text = text + cuisine;

                    // creating list of items for cuisine filter spinner
                    if (!allCuisines.contains(map.get("cuisine").toLowerCase())) {
                        allCuisines.add(map.get("cuisine").toLowerCase());
                    }
                }
            }

            if (map.containsKey("perishability")) {
                if (! map.get("perishability").equals("")) {
                    String perish = "\n" + "Perishability: " + map.get("perishability").toLowerCase();
                    text = text + perish;

                    // creating list of items for perishability spinner
                    if (!allPerishabilities.contains(map.get("perishability").toLowerCase())) {
                        allPerishabilities.add(map.get("perishability").toLowerCase());
                    }
                }
            }
            // add donation listing string to arraylist of all listing for feed
            allListingsArrayList.add(text);
        }

        // spinner for cuisine filter
        Spinner cuisineDropdown = findViewById(R.id.spinner1);
        cuisineAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allCuisines);
        cuisineDropdown.setAdapter(cuisineAdapterSpinner);

        // spinner for perishability filter
        Spinner perishabilityDropdown = findViewById(R.id.spinner2);
        perishAdapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allPerishabilities);
        perishabilityDropdown.setAdapter(perishAdapterSpinner);

        // feed of all donation listings in the arraylist
        ListView donationFeed = (ListView) findViewById(R.id.donationLayout); // Root ViewGroup in which you want to add textviews
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, allListingsArrayList);
        donationFeed.setAdapter(adapter);


        // -------------- ACTION: SELECTING INDIVIDUAL DONATION LISTINGS -----------------
        // upon clicking an individual listing in the feed, user is shown an view screen for that listing
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


        // ---------------- ACTION: FILTERING DONATION LISTINGS BY CUISINE ------------------
        // upon selecting a cuisine filter, a new list of donation listings is generated and displayed
        cuisineDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                currCuisineFiltering = selectedItem;
                updateFilteringResults();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        // ---------------- ACTION: FILTERING DONATION LISTINGS BY PERISHABILITY ----------------
        // upon selecting a perishability filter, a new list of donation listings is generated and displayed
        perishabilityDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                currPerishabilityFilter = selectedItem;
                updateFilteringResults();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        // ---------------- ACTION: FILTERING DONATION LISTINGS BY ZIPCODE ------------------
        // upon entering a zipcode, a new list of donation listings is generated and displayed
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
                // update donation listing feed when full zipcode is input
                if(s.length() == 5){
                    zipcodeInput = s.toString();
                    currCuisineFiltering = "No Cuisine Filter";
                    currPerishabilityFilter = "No Time Filter";
                    cuisineDropdown.setSelection(0);
                    perishabilityDropdown.setSelection(0);
                    updateFilteringResults();
                }
                // update donation listing feed when zipcode input is cleared
                else if (s.length() == 0){
                    zipcodeInput = "";
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

                        allCuisines.clear();
                        allCuisines.add("No Cuisine Filter");
                        allPerishabilities.clear();
                        allPerishabilities.add("No Time Filter");

                        for (Map<String, String> map : listings) {
                            String name = map.get("organization");
                            String item = "Food Description: " + map.get("food_description");
                            String quantity = "Quantity: " + map.get("quantity");
                            String foodType = "Food Type: " + map.get("food_type");
                            String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                            if (map.containsKey("cuisine")) {
                                if (! map.get("cuisine").equals("")) {
                                    String cuisine = "\n" + "Cuisine: " + map.get("cuisine").toLowerCase();
                                    text = text + cuisine;

                                    if (!allCuisines.contains(map.get("cuisine").toLowerCase())) {
                                        allCuisines.add(map.get("cuisine").toLowerCase());
                                    }
                                }
                            }

                            if (map.containsKey("perishability")) {
                                if (! map.get("perishability").equals("")) {
                                    String perish = "\n" + "Perishability: " + map.get("perishability").toLowerCase();
                                    text = text + perish;

                                    if (!allPerishabilities.contains(map.get("perishability").toLowerCase())) {
                                        allPerishabilities.add(map.get("perishability").toLowerCase());
                                    }
                                }
                            }
                            allListingsArrayList.add(text);
                        }
                        adapter.notifyDataSetChanged();
                        cuisineAdapterSpinner.notifyDataSetChanged();
                        perishAdapterSpinner.notifyDataSetChanged();

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


    // Method updates the donation listing feed based on zipcode, cuisine, and perishability filters
    private void updateFilteringResults() {
        System.out.println(zipcodeInput);
        allListingsArrayList.clear();

        try {
            // retrieving the donation listing data for zipcode
            String zipUrl = "http://10.0.2.2:3000/viewListingsForSocialService?zipcode=" + zipcodeInput;
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

            // available filter options may change according to the new filter changes
            allCuisines.clear();
            allCuisines.add("No Cuisine Filter");
            allPerishabilities.clear();
            allPerishabilities.add("No Time Filter");

            // no cuisine filter
            if (currCuisineFiltering.equals("No Cuisine Filter")) {
                // no cuisine filter and no perishability filter
                if (currPerishabilityFilter.equals("No Time Filter")) {
                    for (Map<String, String> map : listings) {
                        String name = map.get("organization");
                        String item = "Food Description: " + map.get("food_description");
                        String quantity = "Quantity: " + map.get("quantity");
                        String foodType = "Food Type: " + map.get("food_type");
                        String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                        if (map.containsKey("cuisine")) {
                            if (!map.get("cuisine").equals("")) {
                                String cuisine = "\n" + "Cuisine: " + map.get("cuisine").toLowerCase();
                                text = text + cuisine;

                                // creating list of items for cuisine filter spinner
                                if (!allCuisines.contains(map.get("cuisine").toLowerCase())) {
                                    allCuisines.add(map.get("cuisine").toLowerCase());
                                }
                            }
                        }

                        if (map.containsKey("perishability")) {
                            if (!map.get("perishability").equals("")) {
                                String perish = "\n" + "Perishability: " + map.get("perishability").toLowerCase();
                                text = text + perish;

                                // creating list of items for perishability filter spinner
                                if (!allPerishabilities.contains(map.get("perishability").toLowerCase())) {
                                    allPerishabilities.add(map.get("perishability").toLowerCase());
                                }
                            }
                        }
                        allListingsArrayList.add(text);
                    }
                }

                // no cuisine filter and yes perishability filter
                else {
                    allPerishabilities.add(currPerishabilityFilter);
                    for (Map<String, String> map : listings) {
                        if (map.containsKey("perishability")) {
                            String currPerishability = map.get("perishability").toLowerCase();
                            // only want to display the listings that have the correct cuisine
                            if (currPerishability.equals(currPerishabilityFilter)) {
                                String name = map.get("organization");
                                String item = "Food Description: " + map.get("food_description");
                                String quantity = "Quantity: " + map.get("quantity");
                                String foodType = "Food Type: " + map.get("food_type");
                                String text = name + "\n" + item + "\n" + quantity + "\n" + foodType;

                                if (map.containsKey("cuisine")) {
                                    if (! map.get("cuisine").equals("")) {
                                        String cuisine = "\n" + "Cuisine: " + map.get("cuisine").toLowerCase();
                                        text = text + cuisine;

                                        // creating list of items for cuisine filter spinner
                                        if (!allCuisines.contains(map.get("cuisine").toLowerCase())) {
                                            allCuisines.add(map.get("cuisine").toLowerCase());
                                        }
                                    }
                                }
                                text = text + "\nPerishability: " + currPerishability.toLowerCase();
                                allListingsArrayList.add(text);
                            }
                        }
                    }
                }
            }
            // yes cuisine filter
            else {
                // yes cuisine filter and no perishability filter
                if (currPerishabilityFilter.equals("No Time Filter")) {
                    allCuisines.add(currCuisineFiltering);
                    for (Map<String, String> map : listings) {
                        if (map.containsKey("cuisine")) {
                            String currCuisine = map.get("cuisine").toLowerCase();
                            // only want to display the listings that have the correct cuisine
                            if (currCuisine.equals(currCuisineFiltering)) {
                                String name = map.get("organization");
                                String item = "Food Description: " + map.get("food_description");
                                String quantity = "Quantity: " + map.get("quantity");
                                String foodType = "Food Type: " + map.get("food_type");
                                String text = name + "\n" + item + "\n" + quantity + "\n" + foodType + "\nCuisine: " + currCuisine;

                                if (map.containsKey("perishability")) {
                                    if (!map.get("perishability").equals("")) {
                                        String perish = "\n" + "Perishability: " + map.get("perishability").toLowerCase();
                                        text = text + perish;

                                        // creating list of items for perishability filter spinner
                                        if (!allPerishabilities.contains(map.get("perishability").toLowerCase())) {
                                            allPerishabilities.add(map.get("perishability").toLowerCase());
                                        }
                                    }
                                }
                                allListingsArrayList.add(text);
                            }
                        }
                    }
                }
                // yes cuisine filter and yes perishability filter
                else {
                    allCuisines.add(currCuisineFiltering);
                    allPerishabilities.add(currPerishabilityFilter);
                    for (Map<String, String> map : listings) {
                        if (map.containsKey("cuisine") && map.containsKey("perishability")) {
                            String currCuisine = map.get("cuisine").toLowerCase();
                            String currPerishability = map.get("perishability").toLowerCase();
                            // only want to display the listings that have the correct cuisine & perishability
                            if (currCuisine.equals(currCuisineFiltering) && currPerishability.equals(currPerishabilityFilter)) {
                                String name = map.get("organization");
                                String item = "Food Description: " + map.get("food_description");
                                String quantity = "Quantity: " + map.get("quantity");
                                String foodType = "Food Type: " + map.get("food_type");
                                String text = name + "\n" + item + "\n" + quantity + "\n" + foodType + "\nCuisine: " + currCuisine + "\nPerishability: " + currPerishability;
                                allListingsArrayList.add(text);
                            }
                        }
                    }
                }
            }
            // update donation listing feed and filter spinners
            adapter.notifyDataSetChanged();
            cuisineAdapterSpinner.notifyDataSetChanged();
            perishAdapterSpinner.notifyDataSetChanged();
        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }

    }

}
