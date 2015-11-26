package zeusro.specialalarmclock.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.receiver.AlarmServiceBroadcastReciever;

/**
 * Created by Z on 2015/11/16.
 */
public class BaseActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.middle_title_actionbar);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
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

    /**
     * 设置闹钟服务
     */
    protected void callMathAlarmScheduleService() {
//        Intent serviceIntent = new Intent(this, AlarmService.class);
//        this.startService(serviceIntent);
        AlarmServiceBroadcastReciever reciever = new AlarmServiceBroadcastReciever();
        reciever.setAlarm(this);
//        bindService()

//        Intent serviceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
//        this.startService(serviceIntent);
    }
}
