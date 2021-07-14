package com.sim.MobilityManager;

public class Location {
	public Location(float x,float y, float z){
		this.xpos = x;
		this.ypos = y;
		this.zpos = z;
		
	}
	public Location(){
		this.xpos = 0;
		this.ypos = 0;
		this.zpos = 0;
	}
	public void setCurrentLocation(float x,float y,float z) {
		this.xpos = x;
		this.ypos = y;
		this.zpos = z;
	}
	public void changeLocation() {
		
	}
	public float getXpos() {
		return xpos;
	}
	public float getYpos() {
		return ypos;
	}
	
	public float getZpos() {
		return zpos;
	}
	public void setZpos(float zpos) {
		this.zpos = zpos;
	}
	public String toString() {
		return ("( "+this.xpos+","+this.ypos+","+this.zpos+")");
	}

	private float xpos;
	private float ypos;
	private float zpos;

}
