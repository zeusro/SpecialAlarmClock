package zeusro.specialalarmclock.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

import zeusro.specialalarmclock.Alarm;
import zeusro.specialalarmclock.R;
import zeusro.specialalarmclock.StaticWakeLock;
import zeusro.specialalarmclock.view.SlideView;

public class AlarmAlertActivity extends AppCompatActivity implements View.OnClickListener {
    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private Vibrator vibrator;

    private boolean alarmActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            alarm = (Alarm) bundle.getSerializable("alarm");
            if (null != alarm) {
                this.setTitle(alarm.getAlarmName());
            }
        }
        SlideView slideView = (SlideView) findViewById(R.id.slider);
        slideView.setSlideListener(new SlideView.SlideListener() {
            @Override
            public void onDone() {

                Toast.makeText(AlarmAlertActivity.this, "Slide OK!", Toast.LENGTH_SHORT).show();
            }
        });

//        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        PhoneStateListener phoneStateListener = new PhoneStateListener() {
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                switch (state) {
//                    case TelephonyManager.CALL_STATE_RINGING:
//                        Log.d(getClass().getSimpleName(), "Incoming call: "
//                                + incomingNumber);
//                        try {
//                            mediaPlayer.pause();
//                        } catch (IllegalStateException e) {
//
//                        }
//                        break;
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        Log.d(getClass().getSimpleName(), "Call State Idle");
//                        try {
//                            mediaPlayer.start();
//                        } catch (IllegalStateException e) {
//
//                        }
//                        break;
//                }
//                super.onCallStateChanged(state, incomingNumber);
//            }
//        };
//
//        telephonyManager.listen(phoneStateListener,
//                PhoneStateListener.LISTEN_CALL_STATE);
//
//        // Toast.makeText(this, answerString, Toast.LENGTH_LONG).show();
//
//        startAlarm();

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    private void startAlarm() {

        if (alarm.getAlarmTonePath() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.IsVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {1000, 200, 200, 200};
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this, Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (!alarmActive)
            return;
        String button = (String) v.getTag();
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (button.equalsIgnoreCase("clear")) {
            if (answerBuilder.length() > 0) {
                answerBuilder.setLength(answerBuilder.length() - 1);
            }
        } else if (button.equalsIgnoreCase(".")) {
            if (!answerBuilder.toString().contains(button)) {
                if (answerBuilder.length() == 0)
                    answerBuilder.append(0);
                answerBuilder.append(button);
            }
        } else if (button.equalsIgnoreCase("-")) {
            if (answerBuilder.length() == 0) {
                answerBuilder.append(button);
            }
        } else {
            answerBuilder.append(button);
            if (isAnswerCorrect()) {
                alarmActive = false;
                if (vibrator != null)
                    vibrator.cancel();
                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException ise) {

                }
                try {
                    mediaPlayer.release();
                } catch (Exception e) {

                }
                this.finish();
            }
        }

    }

    public boolean isAnswerCorrect() {
        boolean correct = false;
        try {
//            correct = mathProblem.getAnswer() == Float.parseFloat(answerBuilder.toString());
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
