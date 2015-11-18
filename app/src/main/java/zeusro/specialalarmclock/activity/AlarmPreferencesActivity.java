package zeusro.specialalarmclock.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.AlarmPreference;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.adapter.AlarmPreferenceListAdapter;

public class AlarmPreferencesActivity extends BaseActivity {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;
    private ListAdapter listAdapter;
    private ListView listView;
    private CountDownTimer alarmToneTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("alarm")) {
            setMathAlarm((Alarm) bundle.getSerializable("alarm"));
        } else {
            setMathAlarm(new Alarm());
        }
        if (bundle != null && bundle.containsKey("adapter")) {
            setListAdapter((AlarmPreferenceListAdapter) bundle.getSerializable("adapter"));
        } else {
            setListAdapter(new AlarmPreferenceListAdapter(this, alarm));
        }
//        EditText tagText= (EditText) findViewById(R.id.tagText);
//        if(tagText!=null){
//            tagText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }


        getListView().setItemsCanFocus(true);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) listAdapter;
                final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter.getItem(position);
                AlertDialog.Builder alert;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Log.d("alarmPreference.getType()",String.valueOf(alarmPreference.getType()));
                switch (alarmPreference.getType()) {
                    case TIME:
                        TimePicker timePicker1 = (TimePicker) v.findViewById(R.id.timePicker);
                        Toast toast = Toast.makeText(getBaseContext(),String.valueOf(timePicker1!=null) , Toast.LENGTH_SHORT);
                        //显示toast信息
                        toast.show();
                        if (timePicker1 != null) {
                            timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker view, int hours, int minute) {
                                    Calendar newAlarmTime = Calendar.getInstance();
                                    newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
                                    newAlarmTime.set(Calendar.MINUTE, minute);
                                    newAlarmTime.set(Calendar.SECOND, 0);
                                    alarm.setAlarmTime(newAlarmTime);
                                    alarmPreferenceListAdapter.setMathAlarm(alarm);
                                    alarmPreferenceListAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;
                    case MULTIPLE_ImageButton:

                        break;
//                    case MULTIPLE_ImageButton:
//                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);
//                        alert.setTitle(alarmPreference.getTitle());
//                        CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
//                        for (int i = 0; i < multiListItems.length; i++)
//                            multiListItems[i] = alarmPreference.getOptions()[i];
//                        boolean[] checkedItems = new boolean[multiListItems.length];
//                        for (int day = 0; day < alarm.getDays().length; day++) {
//                            checkedItems[day] = true;
//                        }
//                        alert.setMultiChoiceItems(multiListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which, boolean isChecked) {
//
//                                int thisDay = Alarm.Values()[which];
//
//                                if (isChecked) {
//                                    alarm.addDay(thisDay);
//                                } else {
//                                    // Only remove the day if there are more than 1
//                                    // selected
//                                    if (alarm.getDays().length > 1) {
//                                        alarm.removeDay(thisDay);
//                                    } else {
//                                        // If the last day was unchecked, re-check
//                                        // it
//                                        ((AlertDialog) dialog).getListView().setItemChecked(which, true);
//                                    }
//                                }
//
//                            }
//                        });
//                        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                alarmPreferenceListAdapter.setMathAlarm(alarm);
//                                alarmPreferenceListAdapter.notifyDataSetChanged();
//                            }
//                        });
//                        alert.show();
//                        break;
                    case BOOLEAN:
                        CheckedTextView checkedTextView = (CheckedTextView) v;
                        boolean checked = !checkedTextView.isChecked();
                        ((CheckedTextView) v).setChecked(checked);
                        switch (alarmPreference.getKey()) {
                            case ALARM_VIBRATE:
                                alarm.setVibrate(checked);
                                if (checked) {
                                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                }
                                break;
                        }
                        alarmPreference.setValue(checked);
                        break;
                    case LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);
                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);
                        CharSequence[] items = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < items.length; i++)
                            items[i] = alarmPreference.getOptions()[i];

                        alert.setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (alarmPreference.getKey()) {
                                    // FIXME: 2015/11/16
//                                    case ALARM_DIFFICULTY:
//                                        Alarm.Difficulty d = Alarm.Difficulty.values()[which];
//                                        alarm.setDifficulty(d);
//                                        break;
                                    case ALARM_TONE:
                                        alarm.setAlarmTonePath(alarmPreferenceListAdapter.getAlarmTonePaths()[which]);
                                        if (alarm.getAlarmTonePath() != null) {
                                            if (mediaPlayer == null) {
                                                mediaPlayer = new MediaPlayer();
                                            } else {
                                                if (mediaPlayer.isPlaying())
                                                    mediaPlayer.stop();
                                                mediaPlayer.reset();
                                            }
                                            try {
                                                // mediaPlayer.setVolume(1.0f, 1.0f);
                                                mediaPlayer.setVolume(0.2f, 0.2f);
                                                mediaPlayer.setDataSource(AlarmPreferencesActivity.this, Uri.parse(alarm.getAlarmTonePath()));
                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                                mediaPlayer.setLooping(false);
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();

                                                // Force the mediaPlayer to stop after 3
                                                // seconds...
                                                if (alarmToneTimer != null)
                                                    alarmToneTimer.cancel();
                                                alarmToneTimer = new CountDownTimer(3000, 3000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        try {
                                                            if (mediaPlayer.isPlaying())
                                                                mediaPlayer.stop();
                                                        } catch (Exception e) {

                                                        }
                                                    }
                                                };
                                                alarmToneTimer.start();
                                            } catch (Exception e) {
                                                try {
                                                    if (mediaPlayer.isPlaying())
                                                        mediaPlayer.stop();
                                                } catch (Exception e2) {

                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                alarmPreferenceListAdapter.setMathAlarm(alarm);
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }

                        });
                        alert.show();
                        break;

                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", alarm);
        outState.putSerializable("adapter", (AlarmPreferenceListAdapter) listAdapter);
    }

    ;

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();
        } catch (Exception e) {
        }
        // setListAdapter(null);
    }





    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }


    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        getListView().setAdapter(listAdapter);

    }

    public ListView getListView() {
        if (listView == null)
            listView = (ListView) findViewById(android.R.id.list);
        return listView;
    }


    @Override
    public void onClick(View v) {
        // super.onClick(v);

    }
}


