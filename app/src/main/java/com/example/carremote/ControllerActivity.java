package com.example.carremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ControllerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        String savedExtra = getIntent().getStringExtra("nume");
        TextView myText = (TextView) findViewById(R.id.textView);
        myText.setText(savedExtra);
    }
}
