package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewDonationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donation);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int id = -1;
        ArrayList<String> listings = new ArrayList<>();
        if(b != null) {
            id = b.getInt("id");
            listings = b.getStringArrayList("listings");
        }
        TextView textViewRestaurant = (TextView) findViewById(R.id.donationListingRestaurant);
        TextView textViewDescription = (TextView) findViewById(R.id.donationListingDescription);
        String text = listings.get(id);
        String restaurantName = text.substring(0, text.indexOf("\n"));
        String listingDetails = text.substring(text.indexOf("\n"));
        textViewRestaurant.setText(restaurantName);
        textViewDescription.setText(listingDetails);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        try {
            //URL url = new URL("http://10.0.2.2:3000/test");
            URL url = new URL("http://10.0.2.2:3000/viewListingsForSocialService?zipcode=");
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

            Intent intent = new Intent(this, DonationFeedActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("list",(Serializable)listings);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);

        }
        catch (Exception e) {
            startActivity(new Intent(this, MainActivity.class));
            e.printStackTrace();
        }
    }

}
