package edu.brynmawr.cmsc353.webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    public void onConnectButtonClick(View v) {

        TextView tv = findViewById(R.id.statusField);

        try {
            // assumes that there is a server running on the AVD's host on port 3000
            // and that it has a /test endpoint that returns a JSON object with
            // a field called "status"
            URL url = new URL("http://10.0.2.2:3000/test");

            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            String status = task.get();

            tv.setText(status);

        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
            tv.setText(e.toString());
        }


    }
}