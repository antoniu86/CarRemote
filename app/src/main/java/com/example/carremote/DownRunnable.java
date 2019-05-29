package com.example.carremote;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class DownRunnable extends AppCompatActivity implements Runnable {
    private Handler mHandler = new Handler();
    private String data;
    private String message;
    private ConnectedThread btt;
    private TextView Tx;

    public DownRunnable(String data, String message, ConnectedThread btt, TextView Tx) {
        this.data = data;
        this.message = message;
        this.btt = btt;
        this.Tx = Tx;
    }

    @Override
    public void run() {
        btt.write(data.getBytes());
        Tx.setText(data);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        mHandler.postAtTime(this, SystemClock.uptimeMillis() + 100);

    }
}
