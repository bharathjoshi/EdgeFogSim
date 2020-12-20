package com.sim.DeviceManager;

import java.util.ArrayList;

import com.sim.MobilityManager.Location;
import com.sim.Tasks.Task;

public class VirtualMachine {
	public VirtualMachine() {
		setQueueTime(0);
	}
	public VirtualMachine(float c, float r) {
		if (this.ID == null) {
			setID("VM-"+no);
			no++;
		}
		setQueueTime(0);
		this.setCC(c); 
		this.setRate(r);
		
	}


	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}


	public Location getLL() {
		return LL;
	}
	public void setLL(Location lL) {
		LL = lL;
	}


	public ArrayList<Task> getTasksQueue() {
		return tasksQueue;
	}
	public void addTasks(Task t) {
		this.tasksQueue.add(t);
		if (tasksQueue.size()>0) {
			setQueueTime(getQueueTime()+tasksQueue.get(tasksQueue.size()-1).getBurstTime());
		}
		
	}


	public float getQueueTime() {
		return QueueTime;
	}
	public void setQueueTime(float queueTime) {
		QueueTime = queueTime;
	}


	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getCC() {
		return CC;
	}
	public void setCC(float cC) {
		CC = cC;
	}



	private static int no = 0 ;
	private float CC; //Computational Capability
	private String ID;
	private Location LL;	
	private ArrayList<Task> tasksQueue = new ArrayList<Task>();
	private float QueueTime;
	private float rate;
}
