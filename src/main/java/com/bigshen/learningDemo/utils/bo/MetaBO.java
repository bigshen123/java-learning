package com.bigshen.learningDemo.utils.bo;

import java.util.Map;

public class MetaBO {
	
private long appid;

private String appName;

private String name;

private String version;

private String code;

private String linuxPath;

private String windowsPath;

Map<String,Map<String,String>> files;

 Map<String,String> xmlFiles;

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getVersion() {
	return version;
}

public void setVersion(String version) {
	this.version = version;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public Map<String, Map<String, String>> getFiles() {
	return files;
}

public void setFiles(Map<String, Map<String, String>> files) {
	this.files = files;
}

public long getAppid() {
	return appid;
}

public void setAppid(long appid) {
	this.appid = appid;
}

public Map<String, String> getXmlFiles() {
	return xmlFiles;
}

public void setXmlFiles(Map<String, String> xmlFiles) {
	this.xmlFiles = xmlFiles;
}

public String getAppName() {
	return appName;
}

public void setAppName(String appName) {
	this.appName = appName;
}

public String getLinuxPath() {
	return linuxPath;
}

public void setLinuxPath(String linuxPath) {
	this.linuxPath = linuxPath;
}

public String getWindowsPath() {
	return windowsPath;
}

public void setWindowsPath(String windowsPath) {
	this.windowsPath = windowsPath;
}

	@Override
	public String toString() {
		return "MetaBO{" +
				"appid=" + appid +
				", appName='" + appName + '\'' +
				", name='" + name + '\'' +
				", version='" + version + '\'' +
				", code='" + code + '\'' +
				", linuxPath='" + linuxPath + '\'' +
				", windowsPath='" + windowsPath + '\'' +
				", files=" + files +
				", xmlFiles=" + xmlFiles +
				'}';
	}
}
