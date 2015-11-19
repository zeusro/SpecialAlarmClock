package zeusro.specialalarmclock.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.Database;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.adapter.AlarmListAdapter;

public class AlarmActivity extends BaseActivity {

    AlarmListAdapter alarmListAdapter;
    ListView mathAlarmListView;
    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toast toast = Toast.makeText(this, R.string.Thank, Toast.LENGTH_SHORT);
        //显示toast信息
        toast.show();
        SetlistView();
        SetAddItem();
    }


    private void SetAddItem() {
        add = (ImageButton) findViewById(R.id.Add);
        if (add != null) {
            add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent newAlarmIntent = new Intent(getApplicationContext(), AlarmPreferencesActivity.class);
                    startActivityForResult(newAlarmIntent, 0);
//                    startActivity(newAlarmIntent);
                }

            });
        }
    }


    private void SetlistView() {
        mathAlarmListView = (ListView) findViewById(R.id.listView);
        Log.d("debug", String.valueOf(mathAlarmListView != null));
        if (mathAlarmListView != null) {
            mathAlarmListView.setLongClickable(true);
            mathAlarmListView.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                    Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
                    dialog.setTitle("Delete");
                    dialog.setMessage("Delete this alarm?");
                    dialog.setPositiveButton("Ok", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Database.init(AlarmActivity.this);
                            Database.deleteEntry(alarm);
                            AlarmActivity.this.callMathAlarmScheduleService();

                            updateAlarmList();
                        }
                    });
                    dialog.setNegativeButton("Cancel", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });

            callMathAlarmScheduleService();

            alarmListAdapter = new AlarmListAdapter(this);
            this.mathAlarmListView.setAdapter(alarmListAdapter);
            mathAlarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                    Intent intent = new Intent(AlarmActivity.this, AlarmPreferencesActivity.class);
                    intent.putExtra("alarm", alarm);
                    startActivity(intent);
                }

            });
        }
    }


    public void updateAlarmList() {
        Database.init(AlarmActivity.this);
        final List<Alarm> alarms = Database.getAll();
        alarmListAdapter.setMathAlarms(alarms);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // reload content
                AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();
                TextView text = (TextView) findViewById(R.id.textView);
                if (alarms != null && alarms.size() > 0) {
                    text.setVisibility(View.INVISIBLE);
                } else {
                    text.setText(R.string.NoClockAlert);
                    text.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        Database.deactivate();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAlarmList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", String.valueOf(resultCode));
        switch (resultCode) {
            case RESULT_OK:
                Bundle b = data.getExtras();
                Alarm alarm =(Alarm) b.getSerializable("object");//回传的值
                if (alarm!=null){
                        Log.d("data", alarm.getAlarmName());

                }

                break;
            default:
                break;
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checkBox_alarm_active) {
            CheckBox checkBox = (CheckBox) v;
            Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
            alarm.setAlarmActive(checkBox.isChecked());
            Database.update(alarm);
            AlarmActivity.this.callMathAlarmScheduleService();
            if (checkBox.isChecked()) {
                Toast.makeText(AlarmActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
