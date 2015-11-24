package zeusro.specialalarmclock.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.AlarmPreference;
import zeusro.specialalarmclock.Key;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.Type;

/**
 * Created by Z on 2015/11/16.
 */

public class AlarmSettingItemListAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private Alarm alarm;
    private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
    private final String[] repeatDays = {"一", "二", "三", "四", "五", "六", "日"};
    private String[] alarmTones;
    private String[] alarmTonePaths;

    public AlarmSettingItemListAdapter(Context context, Alarm alarm) {
        this.context = (context);

        Log.d("AlarmSettingItemListAdapter", "Loading Ringtones...");

        RingtoneManager ringtoneMgr = new RingtoneManager(getContext());
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        alarmTones = new String[alarmsCursor.getCount() + 1];
        alarmTones[0] = "静默模式";
        alarmTonePaths = new String[alarmsCursor.getCount() + 1];
        alarmTonePaths[0] = "";
        if (alarmsCursor.moveToFirst()) {
            do {
                Log.d("ITEM", ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext()));
                Log.d("ITEM", ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString());
                alarmTones[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
                alarmTonePaths[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
            } while (alarmsCursor.moveToNext());
        }
        Log.d("AlarmSettingItemListAdapter", "Finished Loading " + alarmTones.length + " Ringtones.");
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
        Log.d("tagText", "getViewAgain");
        AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        switch (alarmPreference.getType()) {
            case EditText:
                //标签
                if (null == convertView)
                    convertView = layoutInflater.inflate(R.layout.simple_edit_text, null);
                final EditText editText = (EditText) convertView.findViewById(R.id.tagText);
                editText.setText(alarm.getAlarmName());
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        alarm.setAlarmName(s.toString());
                    }
                });
                break;
            case TIME:
                if (null == convertView)
                    convertView = layoutInflater.inflate(R.layout.time_picker, null);
                final TimePicker timePicker1 = (TimePicker) convertView.findViewById(R.id.timePicker);
                int oldHour = alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY);
                int oldMinute =alarm.getAlarmTime().get(Calendar.MINUTE);
                timePicker1.setCurrentHour(oldHour);
                timePicker1.setCurrentMinute(oldMinute);
                notifyDataSetChanged();
//                Log.d("hort", String.valueOf(oldHour));
                final Calendar newAlarmTime = Calendar.getInstance();
                if (timePicker1 != null) {
                    timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            newAlarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            newAlarmTime.set(Calendar.MINUTE, minute);
                            alarm.setAlarmTime(newAlarmTime);
                            setMathAlarm(alarm);
                            timePicker1.setCurrentHour(hourOfDay);
                            timePicker1.setCurrentMinute(minute);
//
                        }
                    });
                }
                break;
            case MULTIPLE_ImageButton:
                if (null == convertView)
                    convertView = layoutInflater.inflate(R.layout.week_button, null);
                // http://stackoverflow.com/questions/12596199/android-how-to-set-onclick-event-for-button-in-list-item-of-listview
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Sunday), Calendar.SUNDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Monday), Calendar.MONDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Tuesday), Calendar.TUESDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Webnesday), Calendar.WEDNESDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Thursday), Calendar.THURSDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Friday), Calendar.FRIDAY);
                SetWeekButton((Button) convertView.findViewById(R.id.btn_Saturday), Calendar.SATURDAY);

                break;
            case BOOLEAN:
                if (null == convertView)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);
                CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
                checkedTextView.setText(alarmPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmPreference.getValue());
                break;
            default:
                if (null == convertView)
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

    final void SetWeekButton(Button button, final int dayOfWeek) {
        final Button week = button;
        if (week != null) {
            Boolean isRepeat = alarm.IsRepeat(dayOfWeek);
            if (isRepeat) {
                week.setTextColor(Color.WHITE);
                week.setBackgroundColor(Color.GRAY);
            } else {
                week.setTextColor(Color.BLACK);
                week.setBackgroundColor(Color.WHITE);
            }
            week.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldButtonTextColor = week.getCurrentTextColor();
                    //0 白色
                    if (oldButtonTextColor != -1) {  // 当前文本颜色为黑/-1

                        week.setTextColor(Color.WHITE);
                        week.setBackgroundColor(Color.GRAY);
                        //选中
                        if (alarm != null)
                            alarm.addDay(dayOfWeek);
                        Log.d("data", String.valueOf(dayOfWeek));

                    } else {
                        int[] days = alarm.getDays();
                        //至少选择一项才允许取消
                        if (days != null && days.length > 0) {
                            week.setTextColor(Color.BLACK);
                            week.setBackgroundColor(Color.WHITE);
                            //为取消
                            if (alarm != null)
                                alarm.removeDay(dayOfWeek);
                        }

                    }
                }
            });
        }

    }


    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
        preferences.clear();
//        preferences.add(new AlarmPreference(Key.ALARM_ACTIVE, context.getString(R.string.AlarmStatus), null, null, alarm.getAlarmActive(), Type.BOOLEAN));
        preferences.add(new AlarmPreference(Key.ALARM_NAME, "标签", alarm.getAlarmName(), null, alarm.getAlarmName(), Type.EditText));
        preferences.add(new AlarmPreference(Key.ALARM_TIME, "时间", alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME));
        preferences.add(new AlarmPreference(Key.ALARM_REPEAT, "重复", "重复", repeatDays, alarm.getDays(), Type.MULTIPLE_ImageButton));

        Uri alarmToneUri = Uri.parse(alarm.getAlarmTonePath());
        Ringtone alarmTone = RingtoneManager.getRingtone(getContext(), alarmToneUri);

        if (alarmTone instanceof Ringtone && !alarm.getAlarmTonePath().equalsIgnoreCase("")) {
            preferences.add(new AlarmPreference(Key.ALARM_TONE, "铃声", alarmTone.getTitle(getContext()), alarmTones, alarm.getAlarmTonePath(), Type.Ring));
        } else {
            preferences.add(new AlarmPreference(Key.ALARM_TONE, "铃声", getAlarmTones()[0], alarmTones, null, Type.Ring));
        }

        preferences.add(new AlarmPreference(Key.ALARM_VIBRATE, "振动", null, null, alarm.IsVibrate(), Type.BOOLEAN));
    }


    public Context getContext() {
        return context;
    }


    public String[] getAlarmTones() {
        return alarmTones;
    }

    public String[] getAlarmTonePaths() {
        return alarmTonePaths;
    }

}
