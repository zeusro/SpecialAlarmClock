package zeusro.specialalarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhoneStateChangedBroadcastReciever extends BroadcastReceiver {
    public PhoneStateChangedBroadcastReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getClass().getSimpleName(), "onReceive()");
    }
}
