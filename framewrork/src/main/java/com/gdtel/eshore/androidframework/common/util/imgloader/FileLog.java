/*
 * Copyright (C) 2012 TRI-SUN ELECTRNICS Limited,All rights reserved.
 */
package com.gdtel.eshore.androidframework.common.util.imgloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;

public class FileLog {

	public static final boolean LOG_FILE = true;//AppConstant.LOG_FILE;
	
	private static final String DELIM = "++++++";
//	boolean 
	static File sLogFile;
	static RandomAccessFile sRAF;
	static {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			sLogFile = new File(Environment.getExternalStorageDirectory() + File.separator + "packageName"/*MainApp.getInstance().getPackageName()*/ + File.separator + "log.log");
			File parentDIr = sLogFile.getParentFile();
			if(!parentDIr.exists()) {
				parentDIr.mkdirs();
			}
			if(!sLogFile.exists()) {
				try {
					sLogFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				sRAF = new RandomAccessFile(sLogFile, "rw");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static boolean nd(String tag, String msg) {
		if(LOG_FILE) {
			printMessage("D", tag, msg);
		}
		return !LOG_FILE;
	}
	
	public static boolean ni(String tag, String msg) {
		if(LOG_FILE) {
			printMessage("I", tag, msg);
		}
		return !LOG_FILE;
	}
	
	public static boolean nw(String tag, String msg) {
		if(LOG_FILE) {
			printMessage("W", tag, msg);
		}
		return !LOG_FILE;
	}
	
	public static boolean ne(String tag, String msg) {
		if(LOG_FILE) {
			printMessage("E", tag, msg);
		}
		return !LOG_FILE;
	}

	public static void fd(String tag, String msg) {
		printMessage("D", tag, msg);
	}
	
	private static void printMessage(String level, String tag, String msg) {
		if(sRAF == null) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(tag);
		sb.append(DELIM);
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
		sb.append(sdf.format(new Date()));
		sb.append(DELIM);
		sb.append(android.os.Process.myPid());
		sb.append(DELIM);
		sb.append(android.os.Process.myTid());
		sb.append(DELIM);
//		sb.append(MainApp.getInstance().getPackageName());
		sb.append(DELIM);
		sb.append(tag);
		sb.append(DELIM);
		sb.append(msg);
		sb.append("\r\n");
		
		try {
			long len = sRAF.length();
			sRAF.seek(len);
			sRAF.writeUTF(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
