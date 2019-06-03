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
    // identificator unic
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // atribute private utilizate pentru comunicarea bt
    private BluetoothAdapter bta;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private ConnectedThread btt = null;

    // handelrul folosit
    public Handler mHandler;

    // campurile tx/rx declarate ca atribute
    private TextView Tx;
    private TextView Rx;

    // atribut in care e salvata adresa MAC a dispozitivului conectat
    public static String MODULE_MAC;

    // variabila de delay a datelor trimise prin bt
    private int delay = 50;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layoutul folsit
        setContentView(R.layout.activity_controller);

        // ia valoarea primita pentru nume sofer si afiseaza pe ecran
        String numeSofer = getIntent().getStringExtra("numeSofer");
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        numeSoferText.setText(numeSofer);

        // ia valoarea primita pentru nume dispozitiv si afiseaza pe ecran
        String numeDispozitiv = getIntent().getStringExtra("numeDispozitiv");
        TextView numeDispozitivText = (TextView) findViewById(R.id.numeDispozitiv);
        numeDispozitivText.setText(numeDispozitiv);

        // ia valoarea primita pentru adresa dispozitiv si afiseaza pe ecran
        String adresaDispozitiv = getIntent().getStringExtra("adresaDispozitiv");
        TextView adresaDispozitivText = (TextView) findViewById(R.id.adresaDispozitiv);
        adresaDispozitivText.setText(adresaDispozitiv);

        // atribuie adresa atributului clasei
        MODULE_MAC = adresaDispozitiv;

        // atribuie viewurile pentru tx/rx atributelor clasei
        Tx = (TextView) findViewById(R.id.Tx);
        Rx = (TextView) findViewById(R.id.Rx);

        // ia adaptorul
        bta = BluetoothAdapter.getDefaultAdapter();

        // initializeaza procesul bt
        initiateBluetoothProcess();

        // actiune button inainte
        Button up = (Button) findViewById(R.id.up);
        up.setOnTouchListener(new View.OnTouchListener() {
            private Handler aHandler = new Handler();

            private Runnable mUpdateTaskdown = new Runnable() {
                public void run() {
                    String text = "u";
                    // se trimite prin BT
                    btt.write(text.getBytes());
                    // se scrie valoare in ecran la Tx
                    Tx.setText(text);
                    aHandler.postAtTime(this, SystemClock.uptimeMillis() + delay);
                }//end run
            };// end runnable

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // cat timp e apasat buton executa primul block de cod
                // cand e luat degetul de pe buton trimite caracterul 's'
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    // afisare notificare pe ecran
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

        // actiune buton inapoi
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

        // actiune buton stanga
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

        // actiune buton dreapta
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

        // actiune buton A
        Button aBtn = (Button) findViewById(R.id.aBtn);
        aBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // cand se atinge butonul se trimite o valoare
                // se scrie in Tx
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = "a";
                    // se trimite valoarea prin BT
                    btt.write(text.getBytes());
                    // se scrie in tx
                    Tx.setText(text);
                    return true;
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    // se goleste tx de pe ecran
                    Tx.setText("");
                    return true;
                }

                return false;
            }
        });

        // actiune buton B
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

        // actiune buton C
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

        // actiune buton X
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

        // actiune buton Y
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

        // actiune button Z
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

    // actiune button Dispozitive
    public void goBack(View v) {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        myIntent.putExtra("numeSofer", numeSoferText.getText().toString());
        startActivity(myIntent);
    }

    // metoda de initializare a procesului de transfer date prin bt
    public void initiateBluetoothProcess() {
        // verifica daca bt e activat, daca nu e nu fa nimica
        if (bta.isEnabled()) {
            // se ia dispozitivul dupa adresa MAC
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            // create socket
            try {
                // se creaza un socket si se face conexiunea
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket.connect();
            } catch (IOException e) {
                // daca aoare eroare se incearca inchiderea socketului cu prinderea erorii
                try {
                    mmSocket.close();
                } catch (IOException c) {
                    return;
                }
            }

            // cu ajutorul handlerului se face un loop pentru a prin response message-ul
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if (msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String)msg.obj;
                    }
                }
            };

            // se creaza un nou Thread pentru a nu supraincarca aplicatia
            btt = new ConnectedThread(mmSocket, mHandler);
            btt.start();
        }
    }
}


