package com.gdtel.eshore.androidframework.common.util.terminalability;

import android.content.Context;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 重力传感器 <br/>
 * <p>
 * 通过该重力传感器类可以方便获取到重力传感器在x, y, z 轴的分量值 <br/>
 * <p>
 * 客户端通过实现{@link GravitySensorListener}该接口来获取到重力传感器的x, y, z分量值。
 * 
 * @author  dengfy
 * @version  [版本号, 2014-12-24]
 */
public class GravitySensor {
	
	private GravitySensorListener Listener;
	
	private SensorManager sensorMgr;
	
	/**
	 * 重力传感器监听器，客户端实现该接口，可以获取到重力传感器的x, y, z分量值。
	 */
	public interface GravitySensorListener {
		/**
		 * 重力传感器回调接口
		 * 
		 * @param x x轴分量值
		 * @param y y轴分量值
		 * @param z z轴分量值
		 */
		public void onGravitySensorChanged(float x, float y, float z);
	}
	
	/**
	 * 客户端通过该方法设置重力传感器的监听器
	 * @param Listener
	 */
	public void setOnSensorListener(GravitySensorListener Listener) {
		this.Listener = Listener;
	}

	/**
	 * 注册重力传感器，该方法应在{@link Activity#onPause()}方法中调用
	 * ， 默认的获取重力传感器的速率是{@link SensorManager#SENSOR_DELAY_NORMAL}
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @return true表示注册成功，false表示失败（本机不支持重力传感器）
	 */
	public boolean registerListener(Context context) {
		if (context == null) {
			throw new NullPointerException("context 对象不能为空");
		}
		
		return registerListener(context, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * 注册重力传感器，该方法应在{@link Activity#onPause()}方法中调用
	 * 
	 * @param context context为空时抛出NullPointerException异常
	 * @param rate 获取重力传感器速率，可以是以下其中之一
	 * 		{@link SensorManager#SENSOR_DELAY_NORMAL},  <br />
	 * 		{@link SensorManager#SENSOR_DELAY_UI},      <br />
	 * 		{@link SensorManager#SENSOR_DELAY_GAME},    <br />
	 * 		{@link SensorManager#SENSOR_DELAY_FASTEST}  <br />
	 * @return true表示注册成功，false表示失败（本机不支持重力传感器）
	 */
	public boolean registerListener(Context context, int rate) {
		if (context == null) {
			throw new NullPointerException("context 对象不能为空");
		}
		
		sensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensorMgr != null) {
			Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorMgr.registerListener(sel, sensor, rate);
		}
		return sensorMgr != null ? true : false;
	}
	
	/**
	 * 取消已注册的重力传感器， 该方法应在{@link Activity#onPause()}方法中调用
	 */
	public void unregisterListener() {
		if (sensorMgr != null) {
			sensorMgr.unregisterListener(sel);
		}	
	}
	
	private SensorEventListener sel = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			//不是重力感应就返回
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {   
				 return;   
			}
			
			if (Listener != null) {
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];

				Listener.onGravitySensorChanged(x, y, z);
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}
