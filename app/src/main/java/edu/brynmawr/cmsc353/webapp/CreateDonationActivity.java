package edu.brynmawr.cmsc353.webapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreateDonationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_donation);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    public void onSubmit(View v) {
        boolean checkInputs = validate(new EditText[] {(EditText)findViewById(R.id.socialServiceName),
                (EditText)findViewById(R.id.foodDescription), (EditText)findViewById(R.id.foodType) ,
                (EditText)findViewById(R.id.quantity), (EditText)findViewById(R.id.perishability),
                (EditText)findViewById(R.id.pickUpTime) });

        if(!checkInputs){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            EditText socialService  = (EditText)findViewById(R.id.socialServiceName);
            String socialServiceString = "\"" + socialService.getText() + "\",";
            EditText foodDescription  = (EditText)findViewById(R.id.foodDescription);
            String foodDescriptionString = "\"" + foodDescription.getText()+ "\",";
            EditText foodType  = (EditText)findViewById(R.id.foodType);
            String foodTypeString = "\"" + foodType.getText()+ "\",";
            EditText quantity  = (EditText)findViewById(R.id.quantity);
            String quantityString = "\"" + quantity.getText()+ "\",";
            EditText perishability  = (EditText)findViewById(R.id.perishability);
            String perishabilityString = "\"" + perishability.getText()+ "\",";
            EditText pickUpTime  = (EditText)findViewById(R.id.pickUpTime);
            String pickUpTimeString = "\"" + pickUpTime.getText()+ "\"";

            String json = "{" +
                    "\"socialService\":" +
                    socialServiceString +
                    "\"foodDescription\":" +
                    foodDescriptionString +
                    "\"foodType\":" +
                    foodTypeString +
                    "\"quantity\":" +
                    quantityString +
                    "\"perishability\":" +
                    perishabilityString +
                    "\"pickUpTime\":" +
                    pickUpTimeString +
                    "}";

            /*String [] input = {"http://10.0.2.2:3000/addDonationForSocialService",
                    "{\"username\":\"root user\",\"password\":\"password\"}"};*/
            String [] input = {"http://10.0.2.2:3000/addDonationForSocialService", json};

            CreateDonWebTask task = new CreateDonWebTask();
            task.execute(input);
            String donation = task.get();
            System.out.println(donation);

            Intent i = new Intent(this, NewDonationMadeActivity.class);
            //if task.get is good then send this
            if(donation.equals("done")){
                i.putExtra("userDonation", json);
            }
            else{
                i.putExtra("userDonation", "fail");
            }
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
