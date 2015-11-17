package zeusro.specialalarmclock.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.AlarmPreference;
import zeusro.specialalarmclock.Key;
import zeusro.specialalarmclock.Type;

/**
 * Created by Z on 2015/11/16.
 */

public class AlarmPreferenceListAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private Alarm alarm;
    private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
    private final String[] repeatDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] alarmTones;
    private String[] alarmTonePaths;

    public AlarmPreferenceListAdapter(Context context, Alarm alarm) {
        setContext(context);

        Log.d("AlarmPreferenceListAdapter", "Loading Ringtones...");

        RingtoneManager ringtoneMgr = new RingtoneManager(getContext());

        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);

        Cursor alarmsCursor = ringtoneMgr.getCursor();

        alarmTones = new String[alarmsCursor.getCount() + 1];
        alarmTones[0] = "静默模式";
        alarmTonePaths = new String[alarmsCursor.getCount() + 1];
        alarmTonePaths[0] = "";

        if (alarmsCursor.moveToFirst()) {
            do {
                alarmTones[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
                alarmTonePaths[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
            } while (alarmsCursor.moveToNext());
        }
        Log.d("AlarmPreferenceListAdapter", "Finished Loading " + alarmTones.length + " Ringtones.");
        alarmsCursor.close();

        setMathAlarm(alarm);
    }

    @Override
    public int getCount() {
        return preferences.size();
    }

    @Override
    public Object getItem(int position) {
        return preferences.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        switch (alarmPreference.getType()) {
            case BOOLEAN:
                // FIXME: 2015/11/16
                //|| convertView.getId() != (int) android.R.layout.simple_list_item_checked
                if (null == convertView )
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);

                CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
                checkedTextView.setText(alarmPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmPreference.getValue());
                break;
            case INTEGER:
            case STRING:
            case LIST:
            case MULTIPLE_LIST:
            case TIME:
            default:

                //// FIXME: 2015/11/16
//|| convertView.getId() !=  android.R.layout.simple_list_item_2
                int ddd=17367045;
                if (null == convertView )
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);

                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setTextSize(18);
                text1.setText(alarmPreference.getTitle());

                TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
                text2.setText(alarmPreference.getSummary());
                break;
        }

        return convertView;
    }

    public Alarm getMathAlarm() {
        for (AlarmPreference preference : preferences) {
            switch (preference.getKey()) {
                case ALARM_ACTIVE:
                    alarm.setAlarmActive((Boolean) preference.getValue());
                    break;
                case ALARM_NAME:
                    alarm.setAlarmName((String) preference.getValue());
                    break;
                case ALARM_TIME:
                    alarm.setAlarmTime((String) preference.getValue());
                    break;
                case ALARM_DIFFICULTY:
                    // FIXME: 2015/11/16
//                    alarm.setDifficulty(Alarm.Difficulty.valueOf((String)preference.getValue()));
                    break;
                case ALARM_TONE:
                    alarm.setAlarmTonePath((String) preference.getValue());
                    break;
                case ALARM_VIBRATE:
                    alarm.setVibrate((Boolean) preference.getValue());
                    break;
                case ALARM_REPEAT:
                    alarm.setDays((int[]) preference.getValue());
                    break;
            }
        }

        return alarm;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
        preferences.clear();
//        preferences.add(new AlarmPreference(Key.ALARM_ACTIVE, context.getString(R.string.AlarmStatus), null, null, alarm.getAlarmActive(), Type.BOOLEAN));
        preferences.add(new AlarmPreference(Key.ALARM_NAME, "标签", alarm.getAlarmName(), null, alarm.getAlarmName(), Type.STRING));
        preferences.add(new AlarmPreference(Key.ALARM_TIME, "时间", alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME));
        //TODO: 弄6个image button出来
        preferences.add(new AlarmPreference(Key.ALARM_REPEAT, "重复","重复", repeatDays, alarm.getDays(), Type.MULTIPLE_ImageButton));

        Uri alarmToneUri = Uri.parse(alarm.getAlarmTonePath());
        Ringtone alarmTone = RingtoneManager.getRingtone(getContext(), alarmToneUri);

        if (alarmTone instanceof Ringtone && !alarm.getAlarmTonePath().equalsIgnoreCase("")) {
            preferences.add(new AlarmPreference(Key.ALARM_TONE, "铃声", alarmTone.getTitle(getContext()), alarmTones, alarm.getAlarmTonePath(), Type.LIST));
        } else {
            preferences.add(new AlarmPreference(Key.ALARM_TONE, "铃声", getAlarmTones()[0], alarmTones, null, Type.LIST));
        }

        preferences.add(new AlarmPreference(Key.ALARM_VIBRATE, "振动", null, null, alarm.getVibrate(), Type.BOOLEAN));
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getRepeatDays() {
        return repeatDays;
    }



    public String[] getAlarmTones() {
        return alarmTones;
    }

    public String[] getAlarmTonePaths() {
        return alarmTonePaths;
    }

}
