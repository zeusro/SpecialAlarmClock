package com.gdtel.eshore.androidframework.common.util.imgloader;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.campusorder/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.campusorder/files";
		}
	}
}
