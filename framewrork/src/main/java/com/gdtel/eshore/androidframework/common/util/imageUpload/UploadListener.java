package com.gdtel.eshore.androidframework.common.util.imageUpload;

public interface UploadListener {
	public void UploadPre(long totalSize);
	public void Uploading(long transferedBytes,int percent,long totalSize);
	public void Uploaded(int code,String msg);
	public void UploadErr(int errCode);
}
