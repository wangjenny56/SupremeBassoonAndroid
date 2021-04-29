package edu.brynmawr.cmsc353.webapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, List<Map<String, String>>> {

    @Override
    protected List<Map<String, String>> doInBackground(URL... urls) {

        try {
            List<Map<String, String>> listings = new ArrayList<>();
            URL url = urls[0];

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String response = in.nextLine();

            JSONArray arrayResult = new JSONArray(response);

            for(int i = 0; i < arrayResult.length(); i++){
                JSONObject document = arrayResult.getJSONObject(i); //document object
                JSONArray listingArray = document.getJSONArray("listings"); //listings array
                System.out.println("WORKS" + document.getString("username"));
                for(int j = 0; j < listingArray.length(); j++){
                    JSONObject foodItem = listingArray.getJSONObject(j); //one item in listings
                    if(foodItem.has("food_description") && foodItem.has("food_type") && foodItem.has("quantity")
                        && foodItem.has("perishability") && foodItem.has("pick_up_time") && foodItem.has("availability_status")
                        && foodItem.has("picked_up_by")){

                        HashMap<String,String> item = new HashMap<>();
                        item.put("restaurant", document.getString("restaurant_name"));
                        item.put("food_description", foodItem.getString("food_description"));
                        item.put("food_type", foodItem.getString("food_type"));
                        item.put("quantity", foodItem.getString("quantity"));
                        item.put("perishability", foodItem.getString("perishability"));
                        item.put("pick_up_time", foodItem.getString("pick_up_time"));
                        item.put("availability_status", foodItem.getString("availability_status"));
                        item.put("picked_up_by", foodItem.getString("picked_up_by"));

                        listings.add(item);
                    }

                }
            }


            /*JSONObject listing = arrayResult.getJSONObject(0);

            for(int i = 0; i < arrayResult.length(); i++){
                HashMap<String,String> item = new HashMap<>();
                item.put("food_description", listing.getString("food_description"));
                item.put("food_type", listing.getString("food_type"));
                item.put("quantity", listing.getString("quantity"));
                item.put("perishability", listing.getString("perishability"));
                item.put("pick_up_time", listing.getString("pick_up_time"));
                item.put("availability_status", listing.getString("availability_status"));
                item.put("picked_up_by", listing.getString("picked_up_by"));

                listings.add(item);
            }*/

            //return listing.getString("food_description");
            return listings;

        }
        catch (Exception e) {
            return Collections.emptyList();
        }

    }

    @Override
    protected void onPostExecute(List<Map<String, String>> list) {
        // this method would be called in the UI thread after doInBackground finishes
        // it can access the Views and update them asynchronously
    }

}
