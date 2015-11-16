package zeusro.specialalarmclock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import zeusro.specialalarmclock.receiver.AlarmServiceBroadcastReciever;

/**
 * Created by Z on 2015/11/16.
 */
public class BaseActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }
}
