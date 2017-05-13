package com.controller.model;

public class ComputerModel {
	private String tempid;
	private int screenwidth;
	private int screenheight;
	private String os;
	private String devip;
	public static ComputerModel model = null;
	
	public  ComputerModel(){
		
	}

	public int getScreenwidth() {
		return screenwidth;
	}
	public void setScreenwidth(int screenwidth) {
		this.screenwidth = screenwidth;
	}
	public int getScreenheight() {
		return screenheight;
	}
	public void setScreenheight(int screenheight) {
		this.screenheight = screenheight;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getDevip() {
		return devip;
	}
	public void setDevip(String devip) {
		this.devip = devip;
	}

	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
}
