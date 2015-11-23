package zeusro.specialalarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import zeusro.specialalarmclock.service.AlarmService;

public class AlarmServiceBroadcastReciever extends BroadcastReceiver {
    public AlarmServiceBroadcastReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarm(context);
    }

    public void setAlarm(Context context) {

        Log.d("AlarmServiceBroadcastReciever", "onReceive()");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }
}
