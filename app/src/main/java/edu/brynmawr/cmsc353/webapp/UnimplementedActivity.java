package edu.brynmawr.cmsc353.webapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UnimplementedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.defaultlayout);

    }

}
