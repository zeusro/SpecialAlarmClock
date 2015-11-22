package com.gdtel.eshore.androidframework.component.upgrade;

import java.io.Serializable;

public class VersionInfo implements Serializable{
	
	private static final long serialVersionUID = -1798463859107069285L;
	
	public String localVersion;
	public String latestVersion;//最新版本。
	public String updateForce;//0：强制更新 1：可选择更新
	public String updateURL;
	public String updateDesc;
	public String Version;//最新版本。
	public String IsUpdateForcibly;//0：强制更新 1：可选择更新
	public String DownloadLink;
	public String ReleaseNote;
	public String softlength;
	public String appCode;
	public String result;
}
