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
    // atribute private ale clasei
    private BluetoothAdapter BTAdapter;
    private Set<BluetoothDevice> PairedDevices;
    private ArrayAdapter<String> BTArrayAdapter;
    private ListView DevicesListView;
    private String BTelement = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // declara layoutul folosit
        setContentView(R.layout.activity_main);

        // verifica daca nu s-a primit valoarea de la ecranul anterior
        // daca s-a primit introdu in campul de nume sofer
        String numeSofer = getIntent().getStringExtra("numeSofer");
        TextView numeSoferText = (TextView) findViewById(R.id.numeSofer);
        numeSoferText.setText(numeSofer);

        // populeaza lista de dispozitive
        adapterListPopulate();
    }

    // onClick pe butonu lde GO
    public void goNext(View v){
        // citeste valoare din campul de nume sofer
        EditText nume = (EditText)findViewById(R.id.numeSofer);

        // verifica daca e goala valoarea
        if (nume.getText().toString().equals("")) {
            // daca este gol lanseaza eroare de notificare pe camp
            nume.setError("Nu poate fi gol");
            nume.requestFocus();
            // daca este gol lanseaza notificare pe ecran
            Toast.makeText(getApplicationContext(), "Introdu un nume de sofer", Toast.LENGTH_SHORT).show();
            // opreste continuarea executiei
            return;
        }

        // verifica daca BTelement este gol
        if (BTelement.equals("")) {
            // daca este gol lanseaza notificare pe ecran
            Toast.makeText(getApplicationContext(), "Alege un dispozitiv", Toast.LENGTH_SHORT).show();
            // opreste continuarea executiei
            return;
        }

        // BTelement informatie impartita in 2: nume si adresa
        final String address = BTelement.substring(BTelement.length() - 17).trim();;
        final String name = BTelement.substring(0, BTelement.length() - 17).trim();;

        // creeaza Intent pentru urmatoarea activitate
        Intent myIntent = new Intent(getBaseContext(), ControllerActivity.class);

        // pregateste datele de trimis
        myIntent.putExtra("numeSofer", nume.getText().toString());
        myIntent.putExtra("numeDispozitiv", name);
        myIntent.putExtra("adresaDispozitiv", address);

        // mergi la urmatoarea activitate
        startActivity(myIntent);
    }

    // onClick pe butonul Refresh
    public void refresh(View v){
        adapterListPopulate();
    }

    // metoda de populare a listei de dispozitive
    private void adapterListPopulate() {
        // obiect lista goala de dispozitive
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // adaptorul bluetooth al dispozitivului
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        // verifica daca bluetooth e activat
        if (BTAdapter.isEnabled()) {
            // ia lista de dispozitive asociate
            PairedDevices = BTAdapter.getBondedDevices();

            for (BluetoothDevice device : PairedDevices) {
                // adauga in lista de dispozitive doar numele si adresa
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

            // obiect lista de dispozitive din ecran, populare si evenimente onClick pe fiecare
            DevicesListView = (ListView)findViewById(R.id.devicesListView);
            DevicesListView.setAdapter(BTArrayAdapter);
            DevicesListView.setOnItemClickListener(DeviceClickListener);
        } else {
            // daca e inactiv lanseaza o notificare pe ecran
            Toast.makeText(getApplicationContext(), "Activeaza modulul de Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    // metoda apelata la click pe un element din lista
    private AdapterView.OnItemClickListener DeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            // verifica inca o data daca bt e activ
            if (!BTAdapter.isEnabled()) {
                // daca nu e activ lanseaza notificare pe ecran
                Toast.makeText(getBaseContext(), "Activeaza modulul de Bluetooth", Toast.LENGTH_SHORT).show();
                // opreste continuarea executiei
                return;
            }

            // atribuie valoarea selectata
            BTelement = ((TextView) v).getText().toString();
        }
    };

    long back_pressed = 0;

    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Apasa de doua ori pentru a iesi", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }

    }
}
