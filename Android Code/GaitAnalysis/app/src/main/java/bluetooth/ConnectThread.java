package bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private Handler handler;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    private ReceiveThread receiveThread;
    private static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//    private static final UUID MY_UUID=UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");
//    private static final UUID MY_UUID=UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");

    public ConnectThread(Handler handler, BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        this.handler = handler;
        this.device = device;
        this.bluetoothAdapter = bluetoothAdapter;
        Log.d("TAasdasdasdasd", device.getName());
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){
            try {
                bluetoothAdapter.cancelDiscovery();
                if(!socket.isConnected()) {
                    socket.connect();
                }
                Message message = Message.obtain();
                message.what = BluetoothConstants.STATE_CONNECTED;
                handler.sendMessage(message);

                receiveThread = new ReceiveThread(socket,handler);
                receiveThread.start();

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = BluetoothConstants.STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
                return;
            }
    }

}
