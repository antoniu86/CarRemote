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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ControllerActivity extends AppCompatActivity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bta;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private ConnectedThread btt = null;

    public Handler mHandler;

    private TextView Tx;
    private TextView Rx;

    public static String MODULE_MAC;

    private int delay = 50;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        String numeSofer = getIntent().getStringExtra("numeSofer");
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        numeSoferText.setText(numeSofer);

        String numeDispozitiv = getIntent().getStringExtra("numeDispozitiv");
        TextView numeDispozitivText = (TextView) findViewById(R.id.numeDispozitiv);
        numeDispozitivText.setText(numeDispozitiv);

        String adresaDispozitiv = getIntent().getStringExtra("adresaDispozitiv");
        TextView adresaDispozitivText = (TextView) findViewById(R.id.adresaDispozitiv);
        adresaDispozitivText.setText(adresaDispozitiv);

        bta = BluetoothAdapter.getDefaultAdapter();
        Tx = (TextView) findViewById(R.id.Tx);
        Rx = (TextView) findViewById(R.id.Rx);
        MODULE_MAC = adresaDispozitiv;

        initiateBluetoothProcess();

        Button up = (Button) findViewById(R.id.up);
        up.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();

            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    String text = "u";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis() + delay);
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "inainte", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown, SystemClock.uptimeMillis() + delay);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    String text = "s";
                    btt.write(text.getBytes());
                    Tx.setText("");
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
                    String text = "d";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis() + delay);
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "inapoi", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown, SystemClock.uptimeMillis() + delay);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    String text = "s";
                    btt.write(text.getBytes());
                    Tx.setText("");
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
                    String text = "l";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis() + delay);
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "stanga", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown, SystemClock.uptimeMillis() + delay);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    String text = "s";
                    btt.write(text.getBytes());
                    Tx.setText("");
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
                    String text = "r";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis() + delay);
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "dreapta", Toast.LENGTH_SHORT).show();
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    aHandler.postAtTime(mUpdateTaskdown, SystemClock.uptimeMillis() + delay);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    String text = "s";
                    btt.write(text.getBytes());
                    Tx.setText("");
                    aHandler.removeCallbacks(mUpdateTaskdown);
                    return true;
                }

                return false;
            }
        });

        Button aBtn = (Button) findViewById(R.id.aBtn);
        aBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "a";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        Button bBtn = (Button) findViewById(R.id.bBtn);
        bBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "b";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        Button cBtn = (Button) findViewById(R.id.cBtn);
        cBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "c";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        Button xBtn = (Button) findViewById(R.id.xBtn);
        xBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "x";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        Button yBtn = (Button) findViewById(R.id.yBtn);
        yBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "y";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        Button zBtn = (Button) findViewById(R.id.zBtn);
        zBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "z";
                    btt.write(text.getBytes());
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });
    }

    public void goBack(View v) {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        myIntent.putExtra("numeSofer", numeSoferText.getText().toString());
        startActivity(myIntent);
    }

    public void initiateBluetoothProcess() {
        if (bta.isEnabled()) {
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            // create socket
            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException c) {
                    return;
                }
            }

            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if (msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String)msg.obj;
                    }
                }
            };

            btt = new ConnectedThread(mmSocket, mHandler);
            btt.start();
        }
    }
}


