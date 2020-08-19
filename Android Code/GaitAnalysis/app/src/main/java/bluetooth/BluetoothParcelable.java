package bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothParcelable implements Parcelable {
    private BluetoothDevice leftDevice;

    private BluetoothDevice rightDevice;

    public BluetoothParcelable(BluetoothDevice leftDevice, BluetoothDevice rightDevice) {
        this.leftDevice = leftDevice;
        this.rightDevice = rightDevice;
    }

    protected BluetoothParcelable(Parcel in) {
        leftDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        rightDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    public static final Creator<BluetoothParcelable> CREATOR = new Creator<BluetoothParcelable>() {
        @Override
        public BluetoothParcelable createFromParcel(Parcel in) {
            return new BluetoothParcelable(in);
        }

        @Override
        public BluetoothParcelable[] newArray(int size) {
            return new BluetoothParcelable[size];
        }
    };

    public BluetoothDevice getRightDevice() {
        return rightDevice;
    }


    public BluetoothDevice getLeftDevice() {
        return leftDevice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(leftDevice, flags);
        dest.writeParcelable(rightDevice, flags);
    }
}
