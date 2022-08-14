package com.bigshen.learningDemo.utils.VO;

import java.util.List;

public class FastdfsClientVO {
	
	private String tracker_server;
	
	private String tracker_nginx;
	
	private String out_tracker_nginx;
	
	private List<String> storage_server;

	

	public String getTracker_server() {
		return tracker_server;
	}

	public void setTracker_server(String tracker_server) {
		this.tracker_server = tracker_server;
	}

	public String getTracker_nginx() {
		return tracker_nginx;
	}

	public void setTracker_nginx(String tracker_nginx) {
		this.tracker_nginx = tracker_nginx;
	}

	public List<String> getStorage_server() {
		return storage_server;
	}

	public void setStorage_server(List<String> storage_server) {
		this.storage_server = storage_server;
	}

	public String getOut_tracker_nginx() {
		return out_tracker_nginx;
	}

	public void setOut_tracker_nginx(String out_tracker_nginx) {
		this.out_tracker_nginx = out_tracker_nginx;
	}


}
