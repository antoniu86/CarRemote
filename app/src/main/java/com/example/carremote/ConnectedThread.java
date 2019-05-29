package com.example.carremote;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    public static final int RESPONSE_MESSAGE = 10;
    Handler uih;

    public ConnectedThread(BluetoothSocket socket, Handler uih) {
        mmSocket = socket;
        this.uih = uih;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch(IOException e) {}

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        try {
            mmOutStream.flush();
        } catch (IOException e) {
            return;
        }
    }

    public void run() {
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(mmInStream));
        while (true){
            try {
                String resp = br.readLine();
                Message msg = new Message();
                msg.what = RESPONSE_MESSAGE;
                msg.obj = resp;
                uih.sendMessage(msg);
            } catch(IOException e) {
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch(IOException e) {}
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch(IOException e) {}
    }
}
