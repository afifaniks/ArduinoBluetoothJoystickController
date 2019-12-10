package io.github.afifaniks.heimerx;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothDevicesActivity extends AppCompatActivity {

    private final String UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB";
    private ListView btDevices;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> deviceNames = new ArrayList<>();

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInputStream;
    private OutputStream mmOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

        // Setting the new layout bounds
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * 0.6), (int)(height * 0.7));

        initBluetooth();

        btDevices = findViewById(R.id.listDevices);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
        btDevices.setAdapter(adapter);
        btDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setParams(parent.getItemAtPosition(position).toString());
            }
        });
    }

    private void setParams(String deviceName) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        UUID uuid = UUID.fromString(UUID_STRING); //Standard SerialPortService ID

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if (device.getName().equals(deviceName)) {
                    mmDevice = device;
                    try {
                        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                        mmSocket.connect();

                        if (mmSocket.isConnected()) {
                            mmOutputStream= mmSocket.getOutputStream();
                            mmInputStream = mmSocket.getInputStream();
                            Toast.makeText(this, "Connected to: " + device.getName(), Toast.LENGTH_SHORT).show();

                            // Setting in MainActivity
                            MainActivity.setConnectedDevice(mmDevice, mmSocket, mmInputStream, mmOutputStream);

                        } else {
                            Toast.makeText(this, "Couldn't connect to: " + device.getName(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Couldn't connect to: " + device.getName(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
            }
        }
    }

    private void initBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null)
        {
            System.out.println("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                deviceNames.add(device.getName());
            }
        }
    }
}

