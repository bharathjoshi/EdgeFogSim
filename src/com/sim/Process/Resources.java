package com.sim.Process;
import java.util.ArrayList;
import java.util.Random;
import com.sim.MobilityManager.Location;
import com.sim.Tasks.Task;
import com.sim.DeviceManager.Cloud;
import com.sim.DeviceManager.Cloudlet;
import com.sim.DeviceManager.BrokerCloudlet;
import com.sim.DeviceManager.UserDevice;
import com.sim.DeviceManager.VirtualMachine;


public class Resources {
	
	public Resources() {	
	}
	
	
	
	//Algos
	

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
	
	public void assignCloudletsBroker(ArrayList<Cloudlet> cloudlets, ArrayList<BrokerCloudlet> brokerCloudlets) {
		for (Cloudlet cloudlet : cloudlets) {
			BrokerCloudlet nearestBrokerCloudlet = brokerCloudlets.get(0);
			float dist = cloudlet.getdistance(cloudlet.getLL(), nearestBrokerCloudlet.getLL());
			for (BrokerCloudlet brokerCloudlet : brokerCloudlets) {
				if (brokerCloudlet.getdistance(brokerCloudlet.getLL(), cloudlet.getLL()) <  dist) {
					dist = brokerCloudlet.getdistance(brokerCloudlet.getLL(), cloudlet.getLL());
					nearestBrokerCloudlet = brokerCloudlet;
				}
			}
			nearestBrokerCloudlet.addCloudlet(cloudlet);
		}
	}
	
	public void scheduleCloudOnly(Cloud cloud, Task t) {
		
		VirtualMachine cloudVM = null;
		float timeonCloud = Float.MAX_VALUE;
		for (VirtualMachine vm : cloud.getListVM()) {
			if (Helper.securityCompatible(t, cloud.getSecurityLevel()) && Helper.computationPossible(t, vm)) {
				float timeonThisVM = Math.max(0, vm.getQueueTime() - t.getArrivalTime()) + Helper.computationalTimeDevice(t,vm) + Helper.getRTT(t, vm) + 2 * Helper.dataTransmission(t, vm) ;
				if (timeonThisVM < timeonCloud && timeonThisVM < t.getDeadline()) {
					cloudVM = vm;
					timeonCloud = timeonThisVM;
				}
			}
		}
		if (cloudVM != null) {
			this.assignTask(t, cloudVM);
		}
		else {
			System.out.println("Cannot");
			System.out.println(t);

			
		}	
		
	}
	
	public void scheduleNearestCloudlet(ArrayList<Cloudlet> cloudLetList, Task t) {
		Cloudlet nearestCloudLet= null;
		float distance  = Float.MAX_VALUE;
		for (Cloudlet cloudLet : cloudLetList) {
			if (cloudLet.getdistance(cloudLet.getLL(), t.getLL()) < distance) {
				distance = cloudLet.getdistance(cloudLet.getLL(), t.getLL());
				nearestCloudLet = cloudLet; 
			}
		}
		

		VirtualMachine vmonCloud = null;
		float waitingTime = Float.MAX_VALUE;
		for (VirtualMachine vm : nearestCloudLet.getListVM()) {
			if (Math.max(0,vm.getQueueTime()-t.getArrivalTime()) <  waitingTime && (Math.max(0,vm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vm) + Helper.getRTT(t, vm) + 2 * Helper.dataTransmission(t, vm)) < t.getDeadline() ) {
				waitingTime = Math.max(0, vm.getQueueTime()-t.getArrivalTime()) ;
				vmonCloud = vm;		
			}
		}

		
		if (vmonCloud != null && nearestCloudLet != null) {
			this.assignTask(t,vmonCloud);
		}
		else {
			System.out.println("Cannot");
			System.out.println(t);
		}
		
	}
	
	public void scheduleRandom(ArrayList<Cloudlet>cloudList,Cloud cloud, Task t ) {
		Random random = new Random(19);
		
		int toss = random.nextInt(2);
		if (toss == 1) {
			VirtualMachine _vm = null;
			while(_vm==null) {
				int i = random.nextInt(cloudList.size());
				int j = random.nextInt(cloudList.get(i).getListVM().size());
				VirtualMachine vm = cloudList.get(i).getListVM().get(j);
				if( (Math.max(0,vm.getQueueTime()-t.getArrivalTime())+ Helper.computationalTimeDevice(t,vm) + Helper.getRTT(t, vm) + 2 * Helper.dataTransmission(t, vm)) < t.getDeadline()) {
					_vm = vm ;
				}
				else {
					_vm = null ;
				}
			}
			this.assignTask(t,_vm);
			
		}
		else {
			VirtualMachine _vm = null;
			while(_vm==null) {
				int i = random.nextInt(cloud.getListVM().size());
				VirtualMachine vm = cloud.getListVM().get(i);
				if( (Math.max(0,vm.getQueueTime()-t.getArrivalTime())+ Helper.computationalTimeDevice(t,vm) + Helper.getRTT(t, vm) + 2 * Helper.dataTransmission(t, vm)) < t.getDeadline()) {
					_vm = vm ;
				}
				else {
					_vm = null ;
				}
			}
			this.assignTask(t,_vm);
		}
		
		
		
	}
	
	public void scheduleDAA(ArrayList<Cloudlet> cloudLetList, Task t){
		 Cloudlet nearestCloud= null;
			float distance  = Float.MAX_VALUE;
			for (Cloudlet cloud : cloudLetList) {
				if (cloud.getdistance(cloud.getLL(), t.getLL()) < distance) {
					distance = cloud.getdistance(cloud.getLL(), t.getLL());
					nearestCloud = cloud; 
				}
			}
		
		for (VirtualMachine vm : nearestCloud.getListVM()) {
			if (vm.getQueueTime()==0) {
				assignTask(t, vm);
				return;
			}
		}
		Random random = new Random(19);
		int i = random.nextInt(cloudLetList.size());
		int j = random.nextInt(cloudLetList.get(i).getListVM().size());
		int x = random.nextInt(cloudLetList.size());
		int y = random.nextInt(cloudLetList.get(x).getListVM().size());
		VirtualMachine vm = cloudLetList.get(i).getListVM().get(j).getQueueTime() < cloudLetList.get(x).getListVM().get(y).getQueueTime() ? cloudLetList.get(i).getListVM().get(j):cloudLetList.get(x).getListVM().get(y);
		int clno = cloudLetList.get(i).getListVM().get(j).getQueueTime() < cloudLetList.get(x).getListVM().get(y).getQueueTime() ? i : x;
		if (t.getP2()>0.5) {
			VirtualMachine ovm= null;
			float time = Float.MAX_VALUE;
			for (VirtualMachine  vmit: nearestCloud.getListVM()) {
				if (Math.max(0,vmit.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vmit) + Helper.getRTT(t, vmit) + 2 * Helper.dataTransmission(t, vmit) <time) {
					ovm = vmit;
					time = Math.max(0,vmit.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vmit) + Helper.getRTT(t, vmit) + 2 * Helper.dataTransmission(t, vmit);
					
				}
			}
			if (ovm!=null) {
				if (Math.max(0, vm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,vm) + Helper.getRTT(t, vm) + 2 * Helper.dataTransmission(t, vm) > time){
					vm = ovm;
					
				}
			}
			assignTask(t, vm);
			return;
			
		}
		else {
			for (VirtualMachine evm :   cloudLetList.get(clno).getListVM()) {
				if (evm.getQueueTime()==0) {
					assignTask(t, evm);
					return;	
				}
				
			}
			VirtualMachine _dvm = null;
			Float _dvmtimeFloat = Float.MAX_VALUE;
			for (VirtualMachine dvm:nearestCloud.getListVM()) {
				if (Math.max(0, dvm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,dvm) + Helper.getRTT(t, dvm) + 2 * Helper.dataTransmission(t, dvm) < _dvmtimeFloat) {
					_dvm = dvm;
					_dvmtimeFloat = Math.max(0, dvm.getQueueTime()-t.getArrivalTime()) + Helper.computationalTimeDevice(t,dvm) + Helper.getRTT(t, dvm) + 2 * Helper.dataTransmission(t, dvm);
				}
					
			}
			if (_dvmtimeFloat < t.getDeadline()) {
				assignTask(t, _dvm);
				return;
			}
			else {
				System.out.println("Cannot");
				System.out.println(t);
			}
		}	
		 
	 }
	  
	// Generation 
	
	public ArrayList<Location> generateUserLocations(int x, int y , int z) {
			ArrayList<Location> userLocations = new ArrayList<Location>();
	        Random random= new Random(19); 
	        for (int j = 0 ; j <3 ;j++ ) {
	        	for (int i = 0 ; i < Helper.nUsers/3 ; i++) {
	    			
	    			float x1 = random.nextInt(x/3)+(j*x)/3;
	    			float y1,z1;
	    			if ( i <  Helper.nUsers/9 ) {
	    				y1 = random.nextInt(y/3);
	    			}
	    			else if (i< (Helper.nUsers*2)/9 ) {
	    				y1 = random.nextInt(y/3) + y/3;
	    					
	    			}
	    			else {
	    				y1 = random.nextInt(y/3) + (2*y)/3;

	    			}
	    			if (i<Helper.nUsers/2) {
	    				z1 = random.nextInt(z/3);
	    			}
	    			else {
	    				z1 = random.nextInt(z/3) + z/3;
	    				
	    			}
	    			userLocations.add(new Location(x1,y1,z1));
	  
	    		}
	        }
			
	        return userLocations;
	        
		}
		
	public ArrayList<Location> generateBrokerLocations(int x, int y, int z){
			ArrayList<Location> BrokerLocations = new ArrayList<Location>();
			BrokerLocations.add(new Location(x/3,(2*y)/3, z/3));
			BrokerLocations.add(new Location((2*x)/3,y/3, (2*z)/3));

			return BrokerLocations;	
		}
		
	public ArrayList<Location> generateCloudLetLocations(int x ,int y ,int z){
			ArrayList<Location> cloudLetLocations = new ArrayList<Location>();
	        Random random= new Random(19); 
			for (int  j = 0 ; j < 2 ; j++) {
				for (int i = 0 ; i < 4 ; i++ ) {
					int x1 = random.nextInt(x/2) + (j*x)/2,y1,z1;
					if (i<2) {
						y1 = random.nextInt(y/2);
			
					}
					else {
						y1 = random.nextInt(y/2)+y/2;
					}
					if (i<2) {
						z1 = random.nextInt(z/2);
			
					}
					else {
						z1 = random.nextInt(z/2)+z/2;
					}
					cloudLetLocations.add(new Location(x1,y1,z1));
						
				}
			}
			return cloudLetLocations;
			
		}
	
	public ArrayList<VirtualMachine> generateUserVm(){
			ArrayList<VirtualMachine> userVMList = new ArrayList<VirtualMachine>();
			Random random = new Random(19);
			for (int i = 0 ; i <45 ; i++) {
				float rate = (float) random.nextInt(10)+40;
				VirtualMachine vm = new VirtualMachine(140, rate);
				userVMList.add(vm);
				
			}
			return userVMList;
		}
		
	public ArrayList<VirtualMachine> generateCloudletVm(){
			ArrayList<VirtualMachine> cloudletVMList = new ArrayList<VirtualMachine>();
			Random random = new Random(19);

			for (int i = 0 ; i <2 ; i++) {
				float CC = (float) random.nextInt(10)+175;
				float rate = (float) random.nextInt(10)+60;
				VirtualMachine vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
				CC = (float) random.nextInt(10)+165;
				rate = (float) random.nextInt(10)+50;
			    vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
				CC = (float) random.nextInt(10)+165;
				rate = (float) random.nextInt(10)+50;
				vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
				CC = (float) random.nextInt(10)+165;
				rate = (float) random.nextInt(10)+50;
				vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
				CC = (float) random.nextInt(10)+175;
				rate = (float) random.nextInt(10)+60;
				 vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
				CC = (float) random.nextInt(10)+150;
				rate = (float) random.nextInt(10)+50;
			    vm = new VirtualMachine(CC, rate);
				cloudletVMList.add(vm);
			}
			return cloudletVMList;
			
		}
		
	public ArrayList<VirtualMachine> generateCloudVm(){
			ArrayList<VirtualMachine> cloudVmList = new ArrayList<VirtualMachine>();
			Random random = new Random(19);
			float CC = (float) random.nextInt(10)+175;
			float rate = (float) random.nextInt(10)+60;
			VirtualMachine vm = new VirtualMachine(CC, rate);
			cloudVmList.add(vm);
			CC = (float) random.nextInt(10)+165;
			rate = (float) random.nextInt(10)+50;
			vm = new VirtualMachine(CC, rate);
			cloudVmList.add(vm);
			
			return cloudVmList;
			
		}
	public ArrayList<BrokerCloudlet> generateBrokerCloudlets(int x ,int y ,int z) {
		ArrayList<BrokerCloudlet> brokerCloudlets = new ArrayList<BrokerCloudlet>();
		ArrayList<Location> brokerLocations = this.generateBrokerLocations(x, y, z);
		for (Location location : brokerLocations) {
			BrokerCloudlet brokerCloudlet = new BrokerCloudlet(location);
			brokerCloudlets.add(brokerCloudlet);
		}
		return brokerCloudlets;
	}
		
	public ArrayList<UserDevice> generateUsers(int x,int y, int z) {
			ArrayList<UserDevice> userDevices  = new ArrayList<UserDevice>();
			ArrayList<Location> userLocations = this.generateUserLocations(x, y, z);
			ArrayList<VirtualMachine> userVMs = this.generateUserVm();
			for (int i = 0 ; i < Helper.nUsers ; i++) {
				userDevices.add(new UserDevice(userLocations.get(i),userVMs.get(i)));
			}
			return userDevices;
			
		}
			
	public 	ArrayList<Cloudlet> generateCloudlets(int x, int y,  int z) {
			ArrayList<Cloudlet> cloudLets = new ArrayList<Cloudlet>();

			ArrayList<Location> cloudletLocations = this.generateCloudLetLocations(x, y, z);
			ArrayList<VirtualMachine> cloudletVM= this.generateCloudletVm();
			for (int i = 0 ; i< 6 ; i ++) {
				Cloudlet cloudlet = new Cloudlet(cloudletLocations.get(i));
				cloudlet.addVM(cloudletVM.get(2*i));
				cloudlet.addVM(cloudletVM.get(2*i +1));
				cloudlet.setSecurityLevel("High");
				cloudLets.add(cloudlet);
			}
			return cloudLets;
		}
		
	public 	 ArrayList<Task>  generateTasks() {
		
			ArrayList<Task> tasks = new ArrayList<Task>();

			Random random = new Random(19);
			for(float i = 0 ; i < 25; i++) {
				for (int j = 0 ; j < 10 ; j++) {
					int cc ;
					float p1,p2,p3;
					float deadline;
					if (j < 2 ) {
						cc = random.nextInt(30)+110;
					}
					else if (j < 4){
						cc = random.nextInt(10)+165;
					}
					else if (j < 7) {
						cc = random.nextInt(25)+140;
						
					}
					else {
						cc = random.nextInt(10)+165;
					}
					p1 = random.nextFloat();
					p2 = 1-p1;
					p3 = 0 ;
					deadline = i+200;
					Task task = new Task(cc,deadline,p1,p2,p3,i);
					tasks.add(task);
				}
			}
			return tasks;
		}
			
	public Cloud generateCloud() {
		Location cloudLocation  = new Location(2000, 1500, 900); 
		Cloud cloud = new Cloud(cloudLocation);
		Random random = new Random(19);
		float CC1 = (float) random.nextInt(10)+200;
		float rate1 = (float) random.nextInt(10) + 70;
		float CC2 = (float) random.nextInt(10)+200;
		float rate2 = (float) random.nextInt(10) + 70;
		cloud.addVM(new VirtualMachine(CC1,rate1));
		cloud.addVM(new VirtualMachine(CC2,rate2));
		cloud.setSecurityLevel("High");
		return cloud;
	}
	

	public static void main(String[] args) {
		Resources sim = new Resources();
		ArrayList<Cloudlet> cloudlets = sim.generateCloudlets(300, 196, 84);
		Cloud cloud  = sim.generateCloud();
		ArrayList<BrokerCloudlet> brokerCloudlets =sim.generateBrokerCloudlets(300, 196, 84);
		ArrayList<UserDevice> users = sim.generateUsers(300, 196, 84);
		ArrayList<Task> tasks= sim.generateTasks();

		for (BrokerCloudlet brokerCloudlet : brokerCloudlets) {
			brokerCloudlet.findNearestBrokerCloudlet(brokerCloudlets);
			brokerCloudlet.setCloud(cloud);
		}
		sim.assignCloudletsBroker(cloudlets, brokerCloudlets);
				
		for (int i = 0 ; i < 45 ;i ++) {
			users.get(i).findBrokerCloudlet(brokerCloudlets);
		
		}
		Random random = new Random(19);
	
		 
		for ( int i = 0 ; i  < 250 ; i++) {
			int user = random.nextInt(45);
			
			users.get(user).AddTask(tasks.get(i));		
			
		}
		ArrayList<Float> waitingTime = new ArrayList<Float>();
		ArrayList<Float> burstTime = new ArrayList<Float>();
		ArrayList<Float> traversingTime = new ArrayList<Float>();
		for (int i = 0 ; i < 25 ; i ++) {
			waitingTime.add((float) 0);
			burstTime.add((float) 0);
			traversingTime.add((float) 0);
			
		}
	   for ( int i = 0; i < 250 ; i++) {
		   Task task = tasks.get(i);
		   int index = (int)task.getArrivalTime();
		  
		   waitingTime.set(index, (float) (waitingTime.get(index) + task.getWaitingTime()) );
		   burstTime.set(index, burstTime.get(index)+task.getBurstTime());
		   traversingTime.set(index, traversingTime.get(index)+task.getTurnAroundTime()-task.getWaitingTime()-task.getBurstTime());
		   	   
	   }
	  System.out.println("Waiting Time");
	  for (int i = 0 ; i < 25 ; i++) {
		  System.out.print(waitingTime.get(i));
		  System.out.print(" ");
	  }
	  System.out.println();
	  System.out.println("Burst Time");
	  for (int i = 0 ; i < 25 ; i++) {
		  System.out.print(burstTime.get(i));
		  System.out.print(" ");
	  }
	  System.out.println();
	  System.out.println("Traversing Time");
	  for (int i = 0 ; i < 25 ; i++) {
		  System.out.print(traversingTime.get(i));
		  System.out.print(" ");
	  }
	}
}
