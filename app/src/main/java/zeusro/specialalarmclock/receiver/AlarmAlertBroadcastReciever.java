package zeusro.specialalarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.StaticWakeLock;
import zeusro.specialalarmclock.activity.AlarmAlertActivity;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {
    public AlarmAlertBroadcastReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mathAlarmServiceIntent = new Intent(context, AlarmServiceBroadcastReciever.class);
        context.sendBroadcast(mathAlarmServiceIntent, null);
        StaticWakeLock.lockOn(context);
        Bundle bundle = intent.getExtras();
        final Alarm alarm = (Alarm) bundle.getSerializable("alarm");
        Intent alarmAlertActivityIntent = new Intent(context, AlarmAlertActivity.class);
        alarmAlertActivityIntent.putExtra("alarm", alarm);
        alarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmAlertActivityIntent);
    }
}
