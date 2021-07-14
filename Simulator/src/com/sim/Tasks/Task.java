package com.sim.Tasks;


import com.sim.MobilityManager.Location;

public class Task {
	
	public Task() {
		this.setStatus("undone");
	}
	
	public Task (int ccr, float ddl, float p1, float p2, float p3, float t) {
		this.setCCRequired(ccr);
		this.setDeadline(ddl);
		this.setP1(p1);
		this.setP2(p2);
		this.setP3(p3);
		this.setArrivalTime(t);
		this.setStatus("undone");

	}
	public Task (int ccr, float ddl, float p1, float p2, float p3, String iD) {
		this.setCCRequired(ccr);
		this.setDeadline(ddl);
		this.setP1(p1);
		this.setP2(p2);
		this.setP3(p3);
		this.setID(iD);
		this.setStatus("undone");

	}
	public int getCCRequired() {
		return CCRequired;
	}

	public void setCCRequired(int cCRequired) {
		CCRequired = cCRequired;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public float getDeadline() {
		return deadline;
	}

	public void setDeadline(float deadline) {
		this.deadline = deadline;
	}

	public float getP1() {
		return p1;
	}

	public void setP1(float p1) {
		this.p1 = p1;
	}

	public float getP2() {
		return p2;
	}

	public void setP2(float p2) {
		this.p2 = p2;
	}

	public float getP3() {
		return p3;
	}

	public void setP3(float p3) {
		this.p3 = p3;
	}

	public Location getLL() {
		return LL;
	}

	public void setLL(Location lL) {
		LL = lL;
	}

	public String Status() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public float getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(float waitingTime) {
		this.waitingTime = waitingTime;
	}

	public float getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(float burstTime) {
		this.burstTime = burstTime;
	}

	public float getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(float turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public float getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(float arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDoneby() {
		return doneby;
	}

	public void setDoneby(String doneby) {
		this.doneby = doneby;
	}
	public String toString() {
		return ("Done by " + doneby+ " CCRequired "+ CCRequired + " deadline " + deadline + " P1 " + p1  +" Waiting Time : " + waitingTime + " Burst Time : " + burstTime + " Turn Around Time : " + turnAroundTime );
	}

	private int CCRequired;
	private String ID;
	private float deadline;
	private float p1;
	private float p2;
	private float p3;
	private Location LL;
	private String status;
	private float waitingTime;
	private float  burstTime;
	private float turnAroundTime;
	private float arrivalTime;
	private String doneby;
	
}
