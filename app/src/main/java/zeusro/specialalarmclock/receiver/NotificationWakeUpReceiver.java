package zeusro.specialalarmclock.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.activity.AlarmActivity;

public class NotificationWakeUpReceiver extends BroadcastReceiver {
    public NotificationWakeUpReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("我要早起")
                .setContentText("Hello World!")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, AlarmActivity.class);
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(AlarmActivity.notificationId, mBuilder.build());

    }
}
