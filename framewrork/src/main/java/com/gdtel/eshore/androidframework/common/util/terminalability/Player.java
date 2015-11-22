package com.gdtel.eshore.androidframework.common.util.terminalability;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

/**
 * 音视频Player，单例设计
 * 
 * <p>
 * 通过该类可以地方便的播放来自raw文件夹、assets文件夹、sd卡和网络的音频文件<br/>
 * <p>
 * 注意：播放sd卡中的音频文件需要"android.permission.READ_EXTERNAL_STORAGE"这个权限
 * 
 * @author fayuan
 * @version  [版本号, 2014-12-24]
 */
public class Player {

	private volatile static Player mPlayer;
	
	private MediaPlayer mMediaPlayer;

	private Player() {		
	}
	
	/**
	 * 获取Player对象
	 * 
	 * @return Player对象
	 */
	public static Player getPlayer() {
		//双重校验
		if (mPlayer == null) {
			synchronized (Player.class) {
				if (mPlayer == null) {
					mPlayer = new Player();
				}
			}
		}
		return mPlayer;
	}
	
	/**
	 * 设置来自raw文件夹的音频文件
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param resid raw文件夹里的音频资源的id(R.raw.<something>)
	 */
	public void setDataSource(Context context, int resid) {
		checkContext(context);
		
		release();
		mMediaPlayer = MediaPlayer.create(context, resid);
	}
	
	/**
	 * 设置来自assets文件夹的音频文件
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param audioAssetsPath 音频详细路径，包括后缀名
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void setDataSource(Context context, String audioAssetsPath) throws
			IllegalArgumentException, IllegalStateException, IOException {		
		checkContext(context);
		
		release();
		mMediaPlayer = new MediaPlayer();
		
		AssetManager assetManager = context.getAssets();
		AssetFileDescriptor audioDescriptor = assetManager.openFd(audioAssetsPath);
	    mMediaPlayer.setDataSource(audioDescriptor.getFileDescriptor(),
	                               audioDescriptor.getStartOffset(),
	                               audioDescriptor.getLength());
	    mMediaPlayer.prepare();
	}
	
	/**
	 * 设置来自sd卡或网络的音频文件
	 * 
	 * @param audioPath 音频文件的详细路径，包括后缀名
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void setDataSource(String audioPath) throws IllegalArgumentException, 
			SecurityException, IllegalStateException, IOException {		
		release();		
		mMediaPlayer = new MediaPlayer();
	    mMediaPlayer.setDataSource(audioPath);
	    mMediaPlayer.prepare();
	}
	
	/**
	 * 设置来自sd卡或网络的音频文件
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param audioUri 包含音频文件路径的Uri对象
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void setDataSource(Context context, Uri audioUri) throws 
		IllegalArgumentException, SecurityException, IllegalStateException,
			IOException {		
		checkContext(context);
		
		release();
		mMediaPlayer = new MediaPlayer();	
	    mMediaPlayer.setDataSource(context, audioUri);
	}
	
	/**
	 * 开始播放音乐，在这之前必须通过setDataSource方法来设置播放源。
	 */
	public void start() {
		if (mMediaPlayer != null) {
			mMediaPlayer.start();
		}		
	}
	
	/**
	 * 暂停播放音乐
	 */
	public void pause() {
		if (mMediaPlayer != null && isPlaying()) {
			mMediaPlayer.pause();
		}	
	}
	
	/**
	 * 停止播放音乐
	 */
	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
		}
	}
	
	/**
	 * 停止播放音乐，并释放资源（MediaPlayer对象）
	 */
	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	/**
	 * 设置播放视频的Surface，用于播放视频
	 * 
	 * @param surface
	 */
	public void setSurface(Surface surface) {
		if (mMediaPlayer != null && surface != null) {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
			mMediaPlayer.setSurface(surface);
		}
	}
	
	/**
	 * 是否真正播放
	 * 
	 * @return true表示正在播放，false反之
	 */
	public boolean isPlaying() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}
	
	/**
	 * 是否重复播放，默认只播放一次
	 * 
	 * @param isRepeat true表示重复播放，false只播放一次
	 */
	public void repeat(boolean isRepeat) {
		if (mMediaPlayer != null) {
			mMediaPlayer.setLooping(isRepeat);
		}
	}
	
	/**
	 * 展示音效控制面板，如果系统存在的话。
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @return true表示系统存在音效控制面板， false反之
	 */
	public boolean displayAudioEffectControlPanel(Context context) {	
		checkContext(context);
		
		Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        if (context.getPackageManager().resolveActivity(i, 0) != null) {    				
			i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,
					mMediaPlayer == null ? 0 : mMediaPlayer.getAudioSessionId());       	
			context.startActivity(i);
			return true;
        }
        return false;
	}
	
	/**
	 * 调用音乐app播放音频
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param audioPath audioPath为0-length或null时抛出NullPointerException
	 * @return true表示手机中存在音乐播放app,false表示没有。
	 */
	public static boolean playAudio(Context context, String audioPath) {
		checkContext(context);
		
		Log.d("Player playAudio", "audioPath = " + audioPath);
		if (TextUtils.isEmpty(audioPath)) {
			throw new NullPointerException("audioPath不能为0-length或null");
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW);

		File audioFile = new File(audioPath); 
        intent.setDataAndType(Uri.fromFile(audioFile), "audio/*");        
        
        if (context.getPackageManager().resolveActivity(intent, 0) != null) {
       	 	context.startActivity(intent);
       	 	return true;
        }
        return false;
	}
	
	/**
	 * 调用视频app播放视频
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param videoPath videoPath为0-length或null时抛出NullPointerException
	 * @return true表示手机中存在视频播放app,false表示没有。
	 */
	public static boolean playVideo(Context context, String videoPath) {
		checkContext(context);
		
		Log.d("Player playVideo", "videoPath = " + videoPath);
		if (TextUtils.isEmpty(videoPath)) {
			throw new NullPointerException("videoPath不能为0-length或null");
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW);	

		File videoFile = new File(videoPath);
		intent.setDataAndType(Uri.fromFile(videoFile), "video/*");

		if (context.getPackageManager().resolveActivity(intent, 0) != null) {
			context.startActivity(intent);
			return true;
		}
		return false;
	}
		
	/**
	 * 检查用户传进来的Context对象是否为空
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 */
	private static void checkContext(Context context) {
		if (context == null) {
			throw new NullPointerException("context 对象不能为空");
		}
	}
}
