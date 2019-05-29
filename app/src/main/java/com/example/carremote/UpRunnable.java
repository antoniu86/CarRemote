package com.example.carremote;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class UpRunnable extends AppCompatActivity implements Runnable {
    private Handler mHandler = new Handler();
    private String message;
    private TextView Tx;

    public UpRunnable(String message, TextView Tx) {
        this.message = message;
        this.Tx = Tx;
    }

    @Override
    public void run() {
        Tx.setText("");
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        mHandler.postAtTime(this, SystemClock.uptimeMillis() + 100);

    }
}
