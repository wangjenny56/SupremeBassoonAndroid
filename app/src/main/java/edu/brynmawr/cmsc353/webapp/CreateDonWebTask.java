package edu.brynmawr.cmsc353.webapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CreateDonWebTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            // creates an object to represent the URL
            //URL url = new URL("http://10.0.2.2:3000/addDonationForSocialService");
            URL url = new URL(params[0]);
            // represents the connection to the URL
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");


            // this is the JSON that you want to send
            // note that you need double-quotes around strings
            // and need to escape those double-quotes with the backslash character
            //String json = "{\"username\":\"root\",\"password\":\"password\"}";
            String json = params[1];

            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

            try(OutputStream os = conn.getOutputStream()) {
                os.write(bytes, 0, bytes.length);
            }
            conn.connect();


            // now the response comes back
            int responsecode = conn.getResponseCode();

            // make sure the response has "200 OK" as the status
            if (responsecode != 200) {
                System.out.println("Oops: " + responsecode);
            }
            else {

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    //System.out.println(response.toString());
                    return response.toString();
                }
            }

            //System.out.println("DONE");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "GOT IT";
    }

    @Override
    protected void onPreExecute() {

    }

}
