package bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Handler handler;

    public ReceiveThread(BluetoothSocket bluetoothSocket, Handler handler){
        this.bluetoothSocket = bluetoothSocket;
        this.handler = handler;
        InputStream tempIn = null;
        OutputStream tempOut = null;

        try {
            tempIn = bluetoothSocket.getInputStream();
            tempOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tempIn;
        outputStream  = tempOut;
    }

    public void run(){
        byte[] buffer = new byte[1024];
        int bytes;
        while(true){
            try {
                bytes = inputStream.read(buffer);

                    String tempMsg = new String(buffer, 0, bytes);
                handler.obtainMessage(BluetoothConstants.STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
