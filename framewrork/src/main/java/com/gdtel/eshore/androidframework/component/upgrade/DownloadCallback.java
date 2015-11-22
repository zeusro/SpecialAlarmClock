package com.gdtel.eshore.androidframework.component.upgrade;

/**
 * 下载文件时回调接口，通知下载进行时的信息
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2013-12-23]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface DownloadCallback {

	/**
	 * 通知下载进行时的信息
	 * @param progress 下载进度
	 * @param downloadSize 目前已下载的大小
	 * @param total 下载文件的总大小
	 * @return 是否取消下载
	 */ 
	public boolean onProgressChanged(int progress, int downloadSize, int total);
}
