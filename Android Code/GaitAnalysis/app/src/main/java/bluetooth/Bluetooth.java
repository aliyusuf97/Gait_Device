package bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;

public class Bluetooth {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    private BluetoothCallback updateDevices;
    private boolean recieverRegistered = false;


    public Bluetooth(Context context, BluetoothCallback bluetoothCallback) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        updateDevices = bluetoothCallback;

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()){
                    case BluetoothDevice.BOND_BONDED:
//                        Toast.makeText(context, "This device is already bonded", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Toast.makeText(context, "Bonding device", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Toast.makeText(context, "Bond broken", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                updateDevices.finishedDiscovery();
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                removeAllDevices();
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                if(!device.equals(null)){
//                    Log.d("TAG", deviceName);
                    if(device.getName() != null && device.getAddress() != null){
                        devices.add(device);
                        updateDevices.updateDevices();
                    }
                }
            }
        }
    };

    public void bondDevice(int index){
        bluetoothAdapter.cancelDiscovery();
        int bondState = devices.get(index).getBondState();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            //If device isn't already bonded then bond
            if(bondState == 10) {
                devices.get(index).createBond();
            }
        }
    }

    private void checkBTPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = context.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += context.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    public Intent enableBluetooth(){
        if(bluetoothAdapter == null){
            Toast.makeText(context, "This device does not support bluetooth", Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            return enableBluetooth;
        }
        return null;
    }

    public void scanForDevices(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            startDiscovery();
        } else {
            startDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    private void startDiscovery() {
        checkBTPermission();
        setIntentFilter();
        bluetoothAdapter.startDiscovery();
    }

    private boolean isDiscovering(){
        return bluetoothAdapter.isDiscovering();
    }

    private void setIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(receiver, filter);
        recieverRegistered = true;
    }

    public void unregisterReciever() {
        if (recieverRegistered) {
            context.unregisterReceiver(receiver);
            recieverRegistered = false;
        }
    }

    private void removeAllDevices(){
        devices.clear();
    }

    public String getDeviceAddress(int index){
        return devices.get(index).getAddress();
    }

    public String getDeviceName(int index){
        return devices.get(index).getName();
    }

    public ArrayList<String> getDeviceNames(){
        ArrayList<String> names = new ArrayList<String>();
        for(int i = 0; i < devices.size(); i++){
            names.add(devices.get(i).getName());
        }
        return names;
    }

    public ArrayList<String> getDevicesMAC(){
        ArrayList<String> addresses = new ArrayList<String>();
        for(int i = 0; i < devices.size(); i++){
            addresses.add(devices.get(i).getAddress());
        }
        return addresses;
    }

    public BluetoothDevice getBTDevice(int index){
        return devices.get(index);
    }
}
