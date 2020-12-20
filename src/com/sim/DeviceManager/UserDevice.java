package com.sim.DeviceManager;

import java.util.ArrayList;
import com.sim.MobilityManager.Location;
import com.sim.Tasks.Task;

public class UserDevice extends BasicDevice {

	public UserDevice(Location LL,VirtualMachine vm) {
		super(LL);
		setID("UD-"+counter++);
		defaultBrokerCloudlet = null;
		this.setInbuiltVM(vm);
		
	}

	public UserDevice() { 
		super();
		defaultBrokerCloudlet = null;

		if (this.ID == null) {
			this.ID = "UD-"+counter++; 
		}

	}
	
	public void AddTask(Task t) {
		
		t.setID(this.ID + "T-"+listTasks.size());
	    t.setLL(this.getLL());
		this.listTasks.add(t);
		this.getDefaultBrokerCloudlet().scheduleTaskOptimized(t, this);
	}
	public void findBrokerCloudlet(ArrayList<BrokerCloudlet> brokerCloudlets) {
		float dist =  Float.MAX_VALUE ; 
		
		for (BrokerCloudlet dcl : brokerCloudlets ) {
			if (this.getdistance(this.getLL(), dcl.getLL()) < dist) {
				setDefaultBrokerCloudlet(dcl);
				dist = this.getdistance(this.getLL(), dcl.getLL());
			}
		}
				
	}	
	

	public BrokerCloudlet getDefaultBrokerCloudlet() {
		return defaultBrokerCloudlet;
	}

	public void setDefaultBrokerCloudlet(BrokerCloudlet defaultDaemonCloudlet) {
		this.defaultBrokerCloudlet = defaultDaemonCloudlet;
	}



	public VirtualMachine getInbuiltVM() {
		return inbuiltVM;
	}

	public void setInbuiltVM(VirtualMachine inbuiltVM) {
		inbuiltVM.setLL(this.getLL());
		
		this.inbuiltVM = inbuiltVM;
		inbuiltVM.setID(getID()+"VM-0");
	}
	public void setID (String id) {
		this.ID = id;
		
	}
	public String getID() {
		return this.ID;
	}


	private static int counter = 0 ;
	private String ID; 
	private ArrayList<Task> listTasks = new ArrayList<Task>();
	private BrokerCloudlet defaultBrokerCloudlet ;
	private VirtualMachine inbuiltVM;
	
}
