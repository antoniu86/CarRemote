package com.example.carremote;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket; // stocare socket creat in ControllerActivity
    private final InputStream mmInStream; // intrari
    private final OutputStream mmOutStream; // iesiri
    public static final int RESPONSE_MESSAGE = 10;
    Handler handler; // stocare handler creat in ControllerActivity

    // constructor
    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket; // atribuire socket
        this.handler = handler; // atribuire handler

        InputStream tmpIn = null; // intrari la creare pe null
        OutputStream tmpOut = null; // iesiri la creare pe null

        try{
            tmpIn = socket.getInputStream(); // se citesc intrarile din socket
            tmpOut = socket.getOutputStream(); // se citesc iesirile din socket
        } catch(IOException e) {}

        mmInStream = tmpIn; // se atribuie intrarile atributului
        mmOutStream = tmpOut; // se atribuie iesirile atributului

        try {
            mmOutStream.flush(); // se golesc iesirile
        } catch (IOException e) {
            return;
        }
    }

    public void run() {
        // se creaza un BufferReader pentru intrari
        BufferedReader br = new BufferedReader(new InputStreamReader(mmInStream));

        while (true){
            try {
                // se citesc intrarile linie cu line din buffer
                String resp = br.readLine();
                Message msg = new Message();
                msg.what = RESPONSE_MESSAGE;
                msg.obj = resp;
                handler.sendMessage(msg);
            } catch(IOException e) {
                break;
            }
        }
    }

    // se trimit datele prin bluetooth sub forma de bytes
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch(IOException e) {}
    }
}
