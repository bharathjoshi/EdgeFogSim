package com.sim.DeviceManager;
import java.lang.Math;
import com.sim.Process.Helper;
import java.util.ArrayList;
import com.sim.MobilityManager.Location;
import com.sim.Tasks.*;
public class BrokerCloudlet extends BasicDevice {

	public BrokerCloudlet(Location mm) {
		super(mm);
	}

	public BrokerCloudlet() {
		
	}
	
	public void addCloudlet(Cloudlet cl) {
		treasury.add(cl);
	}
	
	public void scheduleTaskOptimized(Task t, UserDevice ud ) {
		/*
		System.out.println("In Broker");
		System.out.println(t);
		*/
		//Probing Host device
		VirtualMachine hostVM=ud.getInbuiltVM();
		//System.out.println(hostVM.getCC());
		float TimeonHost = Float.MAX_VALUE ; 
		
		if (Helper.computationPossible(t,ud.getInbuiltVM())) {
			TimeonHost = (float) (Math.max(ud.getInbuiltVM().getQueueTime()-t.getArrivalTime(), 0.0)+Helper.computationalTimeDevice(t, ud.getInbuiltVM()));
		}
		//Probing SelfCloudlets
		VirtualMachine selfVM = null;
		
		float TimeonSelfCloudLet = Float.MAX_VALUE;
		for (Cloudlet cl : treasury) {
			for (VirtualMachine vm : cl.getListVM()) {

				if (Helper.securityCompatible(t, cl.getSecurityLevel()) && Helper.computationPossible(t, vm)) {
					
					float timeonThisVM = Math.max(0 , vm.getQueueTime()-t.getArrivalTime())+ Helper.computationalTimeDevice(t,vm)+ Helper.getRTT(t,vm) + 2 *  Helper.dataTransmission(t,vm) ;
					if (timeonThisVM < TimeonSelfCloudLet) {
						selfVM = vm;
						TimeonSelfCloudLet= timeonThisVM;
					}
					
				}
			}
		}
	
		if ((TimeonHost == Float.MAX_VALUE && TimeonSelfCloudLet == Float.MAX_VALUE) || (Math.min(TimeonHost, TimeonSelfCloudLet) > t.getDeadline())) {
			//Probe the nearest BrokerCloudlet
			VirtualMachine otherBrokerCLoudletVM = null;
			float TimeonOtherCloudLet = Float.MAX_VALUE;
			for (Cloudlet cl : getNearestBrokerCloudlet().getTreasury()) {
				for (VirtualMachine vm : cl.getListVM()) {
					if (Helper.securityCompatible(t, cl.getSecurityLevel()) && Helper.computationPossible(t, vm)) {
						float timeonThisVM = Math.max(0,vm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vm)+ Helper.getRTT(t,vm) + 2 * Helper.dataTransmission(t,vm) ;
						if (timeonThisVM < TimeonOtherCloudLet) {
							otherBrokerCLoudletVM = vm;
							TimeonOtherCloudLet= timeonThisVM;
						}
					}
				}
			}
			//Probing the cloud 
			float TimeonCloud = Float.MAX_VALUE;
			VirtualMachine cloudVM = null;
			for (VirtualMachine vm : getCloud().getListVM()) {
				
				if (Helper.securityCompatible(t, getCloud().getSecurityLevel()) && Helper.computationPossible(t, vm)) {
					float timeonThisVM = Math.max(0, vm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vm)+ Helper.getRTT(t,vm) + 2 * Helper.dataTransmission(t,vm) ;
					if (timeonThisVM < TimeonCloud) {
						cloudVM= vm;
						TimeonCloud= timeonThisVM;
					}
				}
			}
			if (Math.min(TimeonCloud, TimeonOtherCloudLet) > t.getDeadline()){
				//Task not possible within Deadline 
				System.out.println("Not possible");
			}
			
			else if(TimeonCloud < TimeonOtherCloudLet) {
				assignTask(t, cloudVM);
			}
			
			else {
				assignTask(t, otherBrokerCLoudletVM);

			}
			
		}
		else {
		
	
			if (TimeonHost<TimeonSelfCloudLet) {
				assignTask(t,hostVM );

			}
			else {
				assignTask(t, selfVM);
			}	
		}	

	}
	
	public void assignTask(Task t, VirtualMachine vm) {
		t.setDoneby(vm.getID());
		t.setBurstTime(Helper.computationalTimeDevice(t, vm));
		if (vm.getQueueTime()-t.getArrivalTime() >= 0) {
			t.setWaitingTime(vm.getQueueTime()-t.getArrivalTime());

		}
		else {
			t.setWaitingTime(0);

		}
		t.setDoneby(vm.getID());
		t.setTurnAroundTime(t.getBurstTime() + t.getWaitingTime() + 2*(Helper.getRTT(t, vm) + Helper.dataTransmission(t, vm) ));
		vm.addTasks(t);
	}
	
	public void findNearestBrokerCloudlet(ArrayList<BrokerCloudlet> brokerCloudlets) {
		float dist =  Float.MAX_VALUE ; 
		for (BrokerCloudlet dcl : brokerCloudlets ) {
			if (this.getdistance(this.getLL(), dcl.getLL()) < dist && this.getdistance(this.getLL(), dcl.getLL()) !=0) {
				setNearestBrokerCloudlet(dcl);
				dist = this.getdistance(this.getLL(), dcl.getLL());
			}
		}
	}
	
	
	public BrokerCloudlet getNearestBrokerCloudlet() {
		return nearestBrokerCloudlet;
	}

	public void setNearestBrokerCloudlet(BrokerCloudlet nearestBrokerCloudlet) {
		this.nearestBrokerCloudlet = nearestBrokerCloudlet;
	}

	public ArrayList<Cloudlet> getTreasury() {
		return treasury;
	}

	
	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}


	public String toString() {
		return ( "Cloudlets " + treasury.size() + "Cloud" + cloud.getID());
	}


	private BrokerCloudlet nearestBrokerCloudlet;
	private Cloud cloud; 
	private ArrayList<Cloudlet>treasury = new ArrayList<Cloudlet>();


}
