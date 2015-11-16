package zeusro.specialalarmclock.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.AlarmPreference;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.adapter.AlarmPreferenceListAdapter;
public class AlarmPreferencesActivity extends BaseActivity {

    ImageButton deleteButton;
    TextView okButton;
    TextView cancelButton;
    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private ListAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            setListAdapter(new AlarmPreferenceListAdapter(this, getMathAlarm()));
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
                final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter.getItem(position);

                AlertDialog.Builder alert;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                switch (alarmPreference.getType()) {
                    case BOOLEAN:
                        CheckedTextView checkedTextView = (CheckedTextView) v;
                        boolean checked = !checkedTextView.isChecked();
                        ((CheckedTextView) v).setChecked(checked);
                        switch (alarmPreference.getKey()) {
                            case ALARM_ACTIVE:
                                alarm.setAlarmActive(checked);
                                break;
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
                    case STRING:

                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        // Set an EditText view to get user input
                        final EditText input = new EditText(AlarmPreferencesActivity.this);

                        input.setText(alarmPreference.getValue().toString());

                        alert.setView(input);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                alarmPreference.setValue(input.getText().toString());

                                if (alarmPreference.getKey() == AlarmPreference.Key.ALARM_NAME) {
                                    alarm.setAlarmName(alarmPreference.getValue().toString());
                                }

                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        });
                        alert.show();
                        break;
                    case LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] items = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < items.length; i++)
                            items[i] = alarmPreference.getOptions()[i];

                        alert.setItems(items, new OnClickListener() {

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
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }

                        });

                        alert.show();
                        break;
                    case MULTIPLE_LIST:
                        alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

                        alert.setTitle(alarmPreference.getTitle());
                        // alert.setMessage(message);

                        CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
                        for (int i = 0; i < multiListItems.length; i++)
                            multiListItems[i] = alarmPreference.getOptions()[i];

                        boolean[] checkedItems = new boolean[multiListItems.length];
                        for (int day : getMathAlarm().getDays()) {
                            checkedItems[day] = true;
                        }
                        alert.setMultiChoiceItems(multiListItems, checkedItems, new OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

                                int thisDay = Alarm.Values()[which];

                                if (isChecked) {
                                    alarm.addDay(thisDay);
                                } else {
                                    // Only remove the day if there are more than 1
                                    // selected
                                    if (alarm.getDays().length > 1) {
                                        alarm.removeDay(thisDay);
                                    } else {
                                        // If the last day was unchecked, re-check
                                        // it
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                                    }
                                }

                            }
                        });
                        alert.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();

                            }
                        });
                        alert.show();
                        break;
                    case TIME:
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmPreferencesActivity.this, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                Calendar newAlarmTime = Calendar.getInstance();
                                newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
                                newAlarmTime.set(Calendar.MINUTE, minutes);
                                newAlarmTime.set(Calendar.SECOND, 0);
                                alarm.setAlarmTime(newAlarmTime);
                                alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
                                alarmPreferenceListAdapter.notifyDataSetChanged();
                            }
                        }, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY), alarm.getAlarmTime().get(Calendar.MINUTE), true);
                        timePickerDialog.setTitle(alarmPreference.getTitle());
                        timePickerDialog.show();
                    default:
                        break;
                }
            }
        });
    }




    private CountDownTimer alarmToneTimer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("alarm", getMathAlarm());
        outState.putSerializable("adapter", (AlarmPreferenceListAdapter) getListAdapter());
    };

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public Alarm getMathAlarm() {
        return alarm;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
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

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void onClick(View v) {
        // super.onClick(v);

    }
}
