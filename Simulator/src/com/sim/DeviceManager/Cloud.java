package com.sim.DeviceManager;

import java.util.ArrayList;
import com.sim.MobilityManager.Location;

public class Cloud extends BasicDevice {

	public Cloud(Location mm) {
		super(mm);
		this.setID("Cloud-"+counter++);
	}

	public Cloud() {
		super();
	}
	public ArrayList<VirtualMachine> getListVM() {
		return listVM;
	}

	public void addVM(VirtualMachine VM) {
		VM.setID(this.getID()+ "VM-"+listVM.size());
		VM.setLL(this.getLL());
		this.listVM.add(VM);
	}


	public String getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
    public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	private String ID;
	private static int counter =0;
	private ArrayList<VirtualMachine> listVM = new ArrayList<VirtualMachine>();
	private String securityLevel;

}
