package com.sim.DeviceManager;
import java.util.ArrayList;

import com.sim.MobilityManager.Location;

public class Cloudlet extends BasicDevice {

	public Cloudlet(Location LL) {
		super(LL);
		this.ID = "CL-"+counter++;
	}

	public Cloudlet() {
		super();
		if (this.ID == null) {
			this.ID = "CL-"+counter++; 
		}
	}
	
	
	public ArrayList<VirtualMachine> getListVM() {
		return listVM;
	}

	public void addVM(VirtualMachine VM) {
		VM.setID(this.ID +"VM-" +listVM.size());
		VM.setLL(this.getLL());
		this.listVM.add(VM);
	}





	public String getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}


	public String toString() {
		return this.ID;
	}


	private ArrayList<VirtualMachine> listVM = new ArrayList<VirtualMachine>();
	private static int counter = 0;
	private String ID;
	private String securityLevel;

	

}
