package edu.brynmawr.cmsc353.webapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SelectDonationSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_select_donation_success);
    }

    public void onBackButtonPressed(View view) {
        startActivity(new Intent(this, MainActivity.class));
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