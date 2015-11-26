package zeusro.specialalarmclock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.Database;
import zeusro.specialalarmclock.service.SchedulingService;

/**
 * 谷歌的实现
 */
public class AlarmServiceBroadcastReciever extends WakefulBroadcastReceiver {

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(context.getPackageName(), Thread.currentThread().getStackTrace()[2].getMethodName());
        Intent service = new Intent(context, SchedulingService.class);
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);

    }

    public void setAlarm(Context context) {
        Log.d(context.getPackageName(), Thread.currentThread().getStackTrace()[2].getMethodName());
        Intent intent = new Intent(context, AlarmServiceBroadcastReciever.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Alarm alarm = getNext(context);
        if (alarm != null) {
            Calendar calendar = alarm.getAlarmTime();
            Calendar now = (Calendar) calendar.clone();
            now.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            now.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
            now.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
            if (now.getTimeInMillis() > calendar.getTimeInMillis()) {
                CancelAlarm(context);
                return;
            }
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Log.d(this.getClass().getSimpleName(), calendar.getTime().toString());
//            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),  AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
            // device is rebooted.
            ComponentName receiver = new ComponentName(context, BootReceiver.class);
            PackageManager pm = context.getPackageManager();
            //可用状态
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            return;
        }
        Log.d(context.getPackageName(), "没有闹钟");
        CancelAlarm(context);

    }


    /**
     * Cancels the alarm.
     *
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void CancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    private Alarm getNext(Context context) {
        Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                int result = 0;
                long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return result;
            }
        });
        Database.init(context);
        List<Alarm> alarms = Database.getAll();
        for (Alarm alarm : alarms) {
            if (alarm.IsAlarmActive())
                alarmQueue.add(alarm);
        }
        if (alarmQueue.iterator().hasNext()) {
            return alarmQueue.iterator().next();
        } else {
            return null;
        }
    }
}
