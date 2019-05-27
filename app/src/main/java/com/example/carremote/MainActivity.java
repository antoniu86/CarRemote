package com.example.carremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter BTAdapter;
    private Set<BluetoothDevice> PairedDevices;
    private ArrayAdapter<String> BTArrayAdapter;
    private ListView DevicesListView;
    private String BTelement = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapterListPopulate();
    }

    public void goNext(View v){
        EditText nume = (EditText)findViewById(R.id.numeSofer);

        if (nume.getText().toString().equals("")) {
            nume.setError("Nu poate fi gol");
            nume.requestFocus();
            Toast.makeText(getApplicationContext(), "Introdu un nume de sofer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BTelement.equals("")) {
            Toast.makeText(getApplicationContext(), "Alege un dispozitiv", Toast.LENGTH_SHORT).show();
            return;
        }

        final String address = BTelement.substring(BTelement.length() - 17);
        final String name = BTelement.substring(0, BTelement.length() - 17);

        Intent myIntent = new Intent(getBaseContext(), ControllerActivity.class);
        myIntent.putExtra("numeSofer", nume.getText().toString());
        myIntent.putExtra("numeDispozitiv", name);
        myIntent.putExtra("AdresaDispozitiv", address);
        startActivity(myIntent);
    }

    public void refresh(View v){
        adapterListPopulate();
    }

    private void adapterListPopulate() {
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (BTAdapter.isEnabled()) {
            PairedDevices = BTAdapter.getBondedDevices();

            for (BluetoothDevice device : PairedDevices) {
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            DevicesListView = (ListView)findViewById(R.id.devicesListView);
            DevicesListView.setAdapter(BTArrayAdapter); // assign model to view
            DevicesListView.setOnItemClickListener(DeviceClickListener);
        } else {
            Toast.makeText(getApplicationContext(), "Activeaza modulul de Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener DeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            if (!BTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Activeaza modulul de Bluetooth", Toast.LENGTH_SHORT).show();
                return;
            }

            BTelement = ((TextView) v).getText().toString();
        }
    };
}
