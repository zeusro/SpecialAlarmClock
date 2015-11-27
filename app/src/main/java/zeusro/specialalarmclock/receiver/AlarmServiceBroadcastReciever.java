package zeusro.specialalarmclock.receiver;

import android.app.Activity;
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
 *
 */
public class AlarmServiceBroadcastReciever extends WakefulBroadcastReceiver {

    Alarm alarm;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(context.getPackageName(), Thread.currentThread().getStackTrace()[2].getMethodName());
        try {
            //            11-27 13:43:39.020 14674-14674/zeusro.specialalarmclock D/TEST: null
//            11-27 13:43:39.020 14674-14674/zeusro.specialalarmclock D/TEST: android.intent.extra.ALARM_COUNT
//            11-27 13:43:39.020 14674-14674/zeusro.specialalarmclock D/TEST: 1
//            11-27 13:43:39.020 14674-14674/zeusro.specialalarmclock D/AlarmServiceBroadcastReciever: false
//            Log.d("TEST", String.valueOf(intent.getAction()));
//            Bundle bundle = intent.getExtras();
//            Set<String> keySet = bundle.keySet();
//            for (String key : keySet) {
//                Object value = bundle.get(key);
//                Log.d("TEST", key.toString());
//                Log.d("TEST", value.toString());
//            }
//            Log.d(this.getClass().getSimpleName(), String.valueOf(bundle.getSerializable("alarm") != null));
//            alarm = (Alarm) bundle.getSerializable("alarm");
//            if (alarm == null)
//                throw new Exception("参数没有啊混蛋");
            //            service.putExtra("alarm", alarm);
            // FIXME: 2015/11/27 通过对象传递找到对象而不是查数据库
            Intent service = new Intent(context, SchedulingService.class);
            service.putExtra("alarm", getNext(context));


            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, service);
            setResultCode(Activity.RESULT_OK);
        } catch (Exception e) {
            Log.wtf("WTF", e);
        }
    }


    public void setAlarm(Context context, Alarm alarm) {
        Log.d(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
        Intent intent = new Intent(context, AlarmServiceBroadcastReciever.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarm == null)
            alarm = getNext(context);
        if (alarm == null) {
            Log.d(context.getPackageName(), "没有闹钟");
            CancelAlarm(context);
        }
        intent.setAction("zeusro.action.alert");
        intent.putExtra("alarm", alarm);
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
    }


    /**
     * Cancels the alarm.
     *
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void CancelAlarm(Context context) {
        if (null == alarmIntent) {
            Intent intent = new Intent(context, AlarmServiceBroadcastReciever.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        }
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
