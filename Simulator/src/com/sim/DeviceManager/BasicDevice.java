package com.sim.DeviceManager;
import  com.sim.MobilityManager.*;

public class BasicDevice {
	
	public BasicDevice(Location mm) {
		this.setLL(mm) ;
	}
	public BasicDevice() {
		this.setLL(null);
	}
	
	public Location getLL() {
		return LL;
	}
	public void setLL(Location lL) {
		this.LL = lL;
	}
	public float getdistance(Location l1, Location l2) {
		return (float) (Math.sqrt((l1.getXpos()-l2.getXpos())*(l1.getXpos()-l2.getXpos())+(l1.getYpos()-l2.getYpos())*(l1.getYpos()-l2.getYpos())));
	}

	private Location LL;
	
	

	
}
