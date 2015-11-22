package com.gdtel.eshore.androidframework.component.mediaplayer;

import java.io.File;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gdtel.eshore.anroidframework.R;
import com.gdtel.eshore.demo.component.mediaplayer.ListActivity;

public class PlayActivity extends Activity {
    private AudioManager mAudioManager = null;
    private TextView playtime = null;
    private TextView durationTime = null;
    private TextView sound;
    private SeekBar seekbar = null;
    private SeekBar soundBar = null;
    private SurfaceView surfaceView;
    private File videofile;
    private MediaPlayer mediaPlayer;
    private Handler handler = null;
    private int position;
    private int currentPosition;
    private String video_path ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        video_path = getIntent().getStringExtra("video_path");
//        DisplayMetrics dm = new DisplayMetrics();
        Display mDisplay = getWindowManager().getDefaultDisplay();
        int mWidth = mDisplay.getWidth();
        int mHeight = mDisplay.getHeight();
        mediaPlayer = new MediaPlayer();
        playtime = (TextView) findViewById(R.id.playtime);
        durationTime = (TextView) findViewById(R.id.duration);
        sound = (TextView) findViewById(R.id.soundsize);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(mWidth, mHeight);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        ButtonClickListener listener = new ButtonClickListener();
        ImageButton playbutton = (ImageButton) this.findViewById(R.id.playBtn);
        playbutton.setAlpha(00000000);
        ImageButton pausebutton = (ImageButton) this.findViewById(R.id.pauseBtn);
        ImageButton resetbutton = (ImageButton) this.findViewById(R.id.resetBtn);
        ImageButton stopbutton = (ImageButton) this.findViewById(R.id.stopBtn);
        playbutton.setOnClickListener(listener);
        pausebutton.setOnClickListener(listener);
        resetbutton.setOnClickListener(listener);
        stopbutton.setOnClickListener(listener);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) mediaPlayer.seekTo(progress);
            }
        });
        soundBar = (SeekBar) findViewById(R.id.soundBar);
        soundBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    int ScurrentPosition = soundBar.getProgress();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ScurrentPosition, 0);

                }
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        String path = savedInstanceState.getString("path");
        if (path != null && !"".equals(path)) { 
            videofile = new File(path);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", position);
        if (videofile != null) outState.putString("path", videofile.getAbsolutePath());

        super.onSaveInstanceState(outState);
    }

    private final class ButtonClickListener implements View.OnClickListener {
        private boolean pause;

        public void onClick(View v) {
        	int id = v.getId();
        	if (id == R.id.playBtn) {
        		String filename = ListActivity.uri.toString();
                videofile = new File(filename);
                play(video_path);
			}else if (id == R.id.pauseBtn) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause = true;
                } else {
                    if (pause) {
                        mediaPlayer.start();
                        pause = false;
                    }
                }
			}else if (id == R.id.resetBtn) {
				 if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                     mediaPlayer.seekTo(0);
                 } else {
                     play(video_path);
                 }
			}else if (id == R.id.stopBtn) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                    // mediaPlayer.release();
                    // mediaPlayer=null;
                }
			}
//            switch (v.getId()) {
//                case R.id.playBtn:
//                    String filename = ListActivity.uri.toString();
//                    videofile = new File(filename);
//                    play(video_path);
//                    break;
//                case R.id.pauseBtn:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                        pause = true;
//                    } else {
//                        if (pause) {
//                            mediaPlayer.start();
//                            pause = false;
//                        }
//                    }
//                    break;
//
//                case R.id.resetBtn:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        mediaPlayer.seekTo(0);
//                    } else {
//                        play(video_path);
//                    }
//                    break;
//                case R.id.stopBtn:
//                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//
//                        mediaPlayer.stop();
//                        // mediaPlayer.release();
//                        // mediaPlayer=null;
//                    }
//                    break;
//                default:
//                    break;
//            }

        }
    }
    /**
     * 播放
     * <功能详细描述>
     * @param path 视频路径
     * @see [类、类#方法、类#成员]
     */
    public void play(String path) {
        if(TextUtils.isEmpty(path)) return;
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(surfaceView.getHolder());
                mediaPlayer.setDataSource(path);
                setup();
                mediaPlayer.start();
            }

        } catch (Exception e) {
            System.out.println("play is wrong");
        }
    }
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param mContext
     * @param uri
     * @see [类、类#方法、类#成员]
     */
    public void play(Context mContext,Uri uri) {
        if(mContext==null||uri==null) return;
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(surfaceView.getHolder());
                mediaPlayer.setDataSource(mContext,uri);
                setup();
                mediaPlayer.start();
            }

        } catch (Exception e) {
            System.out.println("play is wrong");
        }
    }

    /** {@inheritDoc} */

    @Override
    protected void onDestroy() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException ex) {

            ex.printStackTrace();
        }
        super.onDestroy();
    }
    /**
     * 格式化时间
     * <功能详细描述>
     * @param time
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String toTime(int time) {

        time /= 1000;
        int minute = time / 60;

        int second = time % 60;
        return String.format("%02d:%02d", minute, second);
    }

    public String toFotmat(int num) {

        return String.format("%02d", num);
    }

    private void setup() {
        init();
        try {
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(final MediaPlayer mp) {
                    seekbar.setMax(mp.getDuration());
                    handler.sendEmptyMessage(1);
                    playtime.setText(toTime(mp.getCurrentPosition()));
                    durationTime.setText(toTime(mp.getDuration()));
                    mp.seekTo(currentPosition);
                    handler.sendEmptyMessage(2);
                    sound.setText(toFotmat(CurrentSound) + "/" + toFotmat(MaxSound));

                }
            });
        } catch (Exception e) {
            System.out.println("wrong");
        }
    }

    int MaxSound;
    int CurrentSound;
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Log.d("init", "position=" + position + " currentPosition=" + currentPosition);
                        if (mediaPlayer != null && MaxSound != CurrentSound) currentPosition = mediaPlayer
                                .getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                        playtime.setText(toTime(currentPosition));
                        handler.sendEmptyMessage(1);
                        break;
                    case 2:
                        mAudioManager = (AudioManager) PlayActivity.this.getSystemService(PlayActivity.AUDIO_SERVICE);
                        MaxSound = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        CurrentSound = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        sound.setText(toFotmat(CurrentSound) + "/" + toFotmat(MaxSound));
                        handler.sendEmptyMessage(2);
                        break;
                    default:
                        break;
                }

            }
        };
    }

}