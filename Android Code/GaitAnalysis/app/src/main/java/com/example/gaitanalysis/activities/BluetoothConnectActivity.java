package com.example.gaitanalysis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gaitanalysis.R;

import java.util.ArrayList;

import bluetooth.BluetoothCallback;
import bluetooth.BluetoothParcelable;
import list.ListOnClickCallback;
import list.RecyclerList;

import bluetooth.Bluetooth;
import list.RecyclerViewItem;

public class BluetoothConnectActivity extends AppCompatActivity implements BluetoothCallback, ListOnClickCallback {
    private static final String TAG = "TAG";
    private Bluetooth bluetooth;
    RecyclerList list;
    private BluetoothParcelable bluetoothDevices;
    private BluetoothDevice leftFoot;
    private BluetoothDevice rightFoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetooth = new Bluetooth(this, this);
        list = new RecyclerList((RecyclerView) findViewById(R.id.recyclerView), this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.unregisterReciever();
    }

    public void enableBluetoothClick(View view) {
        Intent intent;
        if(bluetooth.enableBluetooth() != null) {
            startActivity(bluetooth.enableBluetooth());
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void discoverDevicesClick(View view) {
        findViewById(R.id.prograssBar).setVisibility(View.VISIBLE);
        bluetooth.scanForDevices();
    }

    @Override
    public void updateDevices() {
        Log.d("TAG", "updateDevices: ");
        ArrayList<String> deviceNames = bluetooth.getDeviceNames();
        ArrayList<String> macAddresses = bluetooth.getDevicesMAC();
        list.removeAllCards();
        for (int i=0; i<deviceNames.size(); i++){
            list.addCard(new RecyclerViewItem(deviceNames.get(i), macAddresses.get(i), 0));
        }
//        findViewById(R.id.prograssBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void finishedDiscovery() {
        findViewById(R.id.prograssBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListClick(int position) {
        bluetooth.bondDevice(position);
        String leftDeviceName = new String("Left_Device");
        String rightDeviceName = new String("Right_Device");
        String name = bluetooth.getBTDevice(position).getName();
        if(bluetooth.getBTDevice(position).getName().equals(leftDeviceName)){
            Log.d(TAG, "if passed");
            list.getCard(position).setImage1(R.drawable.left_foot);
            leftFoot = bluetooth.getBTDevice(position);
        }
        if(bluetooth.getBTDevice(position).getName().equals(rightDeviceName)){
            Log.d(TAG, "onListClick: right");
            list.getCard(position).setImage1(R.drawable.right_foot);
            rightFoot = bluetooth.getBTDevice(position);
        }


    }

    private void sendDevicesToMetrics(){
        Intent intent = new Intent(BluetoothConnectActivity.this, MetricsActivity.class);
        intent.putExtra("Devices", bluetoothDevices);
        startActivity(intent);
    }

    public void startMetricsClick(View view) {
        bluetoothDevices = new BluetoothParcelable(leftFoot,rightFoot);
        if(leftFoot != null && rightFoot != null){
            sendDevicesToMetrics();
        } else {
            Toast.makeText(getApplicationContext(), "Please select the devices", Toast.LENGTH_SHORT).show();
        }
    }
}
