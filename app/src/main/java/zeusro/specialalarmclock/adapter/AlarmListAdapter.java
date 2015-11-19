package zeusro.specialalarmclock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.Database;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.activity.AlarmActivity;

/**
 * Created by Z on 2015/11/16.
 */
public class AlarmListAdapter extends BaseAdapter {
    private AlarmActivity alarmActivity;
    private List<Alarm> alarms = new ArrayList<Alarm>();

    public static final String ALARM_FIELDS[] = {Database.COLUMN_ALARM_ACTIVE, Database.COLUMN_ALARM_TIME, Database.COLUMN_ALARM_DAYS};

    public AlarmListAdapter(AlarmActivity alarmActivity) {
        this.alarmActivity = alarmActivity;
//		Database.init(alarmActivity);
//		alarms = Database.getAll();
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (null == view)
            view = LayoutInflater.from(alarmActivity).inflate(R.layout.list_element, null);

        Alarm alarm = (Alarm) getItem(position);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_alarm_active);
        checkBox.setChecked(alarm.getAlarmActive());
        checkBox.setTag(position);
        checkBox.setOnClickListener(alarmActivity);

        TextView alarmTimeView = (TextView) view.findViewById(R.id.textView_alarm_time);
        alarmTimeView.setText(alarm.getAlarmTimeString());


        TextView alarmDaysView = (TextView) view.findViewById(R.id.textView_alarm_days);

        alarmDaysView.setText(alarm.getRepeatDaysString());


        return view;
    }

    public List<Alarm> getMathAlarms() {
        return alarms;
    }

    public void setMathAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }
}
