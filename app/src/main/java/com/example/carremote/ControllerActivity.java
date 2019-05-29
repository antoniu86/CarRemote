package com.example.carremote;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ControllerActivity extends AppCompatActivity {
    public static String MODULE_MAC;
    public final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bta;                 //bluetooth stuff
    BluetoothSocket mmSocket;             //bluetooth stuff
    BluetoothDevice mmDevice;             //bluetooth stuff
    ConnectedThread btt = null;           //Our custom thread
    public Handler mHandler;
    private int counter = 0;

    private TextView Tx;
    private TextView Rx;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Tx = (TextView) findViewById(R.id.Tx);
        Rx = (TextView) findViewById(R.id.Rx);

        String numeSofer = getIntent().getStringExtra("numeSofer");
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        numeSoferText.setText(numeSofer);

        String numeDispozitiv = getIntent().getStringExtra("numeDispozitiv");
        TextView numeDispozitivText = (TextView) findViewById(R.id.numeDispozitiv);
        numeDispozitivText.setText(numeDispozitiv);

        MODULE_MAC = getIntent().getStringExtra("adresaDispozitiv");
        TextView adresaDispozitivText = (TextView) findViewById(R.id.adresaDispozitiv);
        adresaDispozitivText.setText(MODULE_MAC);

        Button up = (Button) findViewById(R.id.up);
        up.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();


            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    counter++;
                    String text = "u " + counter;
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis());
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "inainte", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown,SystemClock.uptimeMillis() + 50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    counter = 0;
                    Tx.setText("" + counter);
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    return true;
                }

                return false;
            }
        });

        Button down = (Button) findViewById(R.id.down);
        down.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();

            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    counter++;
                    String text = "d " + counter;
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis());
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "inapoi", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown,SystemClock.uptimeMillis() + 50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    counter = 0;
                    Tx.setText("" + counter);
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    return true;
                }

                return false;
            }
        });

        Button left = (Button) findViewById(R.id.left);
        left.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();

            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    counter++;
                    String text = "l " + counter;
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis());
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "stanga", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown,SystemClock.uptimeMillis() + 50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    counter = 0;
                    Tx.setText("" + counter);
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    return true;
                }

                return false;
            }
        });

        Button right = (Button) findViewById(R.id.right);
        right.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();

            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    counter++;
                    String text = "d " + counter;
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis());
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                int action = arg1.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "dreapta", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown,SystemClock.uptimeMillis() + 50);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    counter = 0;
                    Tx.setText("" + counter);
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    return true;
                }

                return false;
            }
        });

        bta = BluetoothAdapter.getDefaultAdapter();

        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else{
            initiateBluetoothProcess();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess();
        }
    }

    public void initiateBluetoothProcess(){
        if(bta.isEnabled()){
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            //create socket
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]","Connected to: "+mmDevice.getName());
            }catch(IOException e){
                try{mmSocket.close();}catch(IOException c){return;}
            }

            Log.i("[BLUETOOTH]", "Creating handler");

            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if(msg.what == ConnectedThread.RESPONSE_MESSAGE){
                        String txt = (String)msg.obj;
                    }
                }
            };

            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket,mHandler);
            btt.start();
        }
    }

    public void goBack(View v){
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }
}


