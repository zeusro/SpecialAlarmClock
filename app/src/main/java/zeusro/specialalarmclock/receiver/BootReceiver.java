package zeusro.specialalarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    AlarmServiceBroadcastReciever alarm = new AlarmServiceBroadcastReciever();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("apprun", "run");
            alarm.setAlarm(context);
        }
    }
}
