package bluetooth;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class HandlePacket {

    private Float[] metrics = new Float[6];
    private HandlerCallback callback;
    private String foot;

    public HandlePacket(HandlerCallback callback, String foot) {
        this.callback = callback;
        this.foot = foot;
    }

    public Handler getHandler() {
        return handler;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case BluetoothConstants.STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[])msg.obj;
                    String tempMsg = new String(readBuffer,0,msg.arg1);
                    handlePacket(tempMsg);
                    break;
            }

            return true;
        }
    });

    private void handlePacket(String tempMsg) {
        String[] tempMsgSplit = tempMsg.split("\r\n");
        for(int i = 0; i < tempMsgSplit.length; i++) {
            if (tempMsgSplit[0].length() > 5) {
                metrics = decodeString(tempMsgSplit[0]);
                if (metrics.length == 8) {
//                    updateLeftValues(metrics);
                    callback.updateMetrics(foot);
                }
            }
            break;
        }
    }

    public Float[] decodeString(String stringToDecode) {
        String[] stringSplit = stringToDecode.split(",");
        Float[] stringToFloat = new Float[stringSplit.length];
        for (int i = 0; i < stringSplit.length; i++) {
            if (stringSplit[i] != null && stringSplit[i].length() > 0 && stringSplit[i].length() < 7) {
                try {
                    stringToFloat[i] = Float.parseFloat(stringSplit[i].trim());
                } catch (Exception e) {

                }
            } else {
                stringToFloat[i] = 123F;
            }
        }
        //if (stringSplit.length == 6) {
            return stringToFloat;
//        } else {
//            Float[] emptyFloat = new Float[] {0F,0F,0F,0F,0F,0F};
//            return emptyFloat;
//        }
    }

    public Float[] getMetrics() {
        return metrics;
    }
}
