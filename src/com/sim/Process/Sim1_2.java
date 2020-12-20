package com.sim.Process;
import java.util.ArrayList;
import java.util.Random;
import com.sim.Tasks.Task;
import com.sim.DeviceManager.Cloud;
import com.sim.DeviceManager.Cloudlet;
import com.sim.DeviceManager.BrokerCloudlet;
import com.sim.DeviceManager.UserDevice;
import com.sim.DeviceManager.VirtualMachine;


public class Sim1_2 {
	
	public Sim1_2() {	
	}
	
	
	public static void main(String[] args) {
		
		Resources sim = new Resources();
		ArrayList<Cloudlet> cloudlets = sim.generateCloudlets(300, 196, 84);
		Cloud cloud  = sim.generateCloud();
		ArrayList<BrokerCloudlet> brokerCloudlets =sim.generateBrokerCloudlets(300, 196, 84);
		ArrayList<UserDevice> users = sim.generateUsers(300, 196, 84);
		ArrayList<Task> tasks= sim.generateTasks();
		Random random = new Random(19);
		for (BrokerCloudlet brokerCloudlet : brokerCloudlets) {
			brokerCloudlet.findNearestBrokerCloudlet(brokerCloudlets);
			brokerCloudlet.setCloud(cloud);
		}
		sim.assignCloudletsBroker(cloudlets, brokerCloudlets);
				
		for (int i = 0 ; i < Helper.nUsers ;i ++) {
			users.get(i).findBrokerCloudlet(brokerCloudlets);
			users.get(i).setInbuiltVM(new VirtualMachine(140,(float) random.nextInt(10)+40));

		
		}
	
		 
		for ( int i = 0 ; i  < Helper.nTasks ; i++) {
			int user = random.nextInt(Helper.nUsers);
			
			users.get(user).AddTask(tasks.get(i));		
			
		}
		ArrayList<Float> waitingTime = new ArrayList<Float>();
		ArrayList<Float> burstTime = new ArrayList<Float>();
		ArrayList<Float> traversingTime = new ArrayList<Float>();
		for (int i = 0 ; i < Helper.time ; i ++) {
			waitingTime.add((float) 0);
			burstTime.add((float) 0);
			traversingTime.add((float) 0);
			
		}
	   for ( int i = 0; i < Helper.nTasks ; i++) {
		   Task task = tasks.get(i);
		   int index = (int)task.getArrivalTime();
		  
		   waitingTime.set(index, (float) (waitingTime.get(index) + task.getWaitingTime()) );
		   burstTime.set(index, burstTime.get(index)+task.getBurstTime());
		   traversingTime.set(index, traversingTime.get(index)+task.getTurnAroundTime()-task.getWaitingTime()-task.getBurstTime());
		   	   
	   }
	  System.out.println("Waiting Time");
	  for (int i = 0 ; i < Helper.time ; i++) {
		  System.out.print(waitingTime.get(i));
		  System.out.print(" ");
	  }
	  System.out.println();
	  System.out.println("Burst Time");
	  for (int i = 0 ; i < Helper.time ; i++) {
		  System.out.print(burstTime.get(i));
		  System.out.print(" ");
	  }
	  System.out.println();
	  System.out.println("Traversing Time");
	  for (int i = 0 ; i < Helper.time ; i++) {
		  System.out.print(traversingTime.get(i));
		  System.out.print(" ");
	  }
	}

}
