package com.sim.Process;
import java.util.ArrayList;
import java.util.Random;
import com.sim.Tasks.Task;
import com.sim.DeviceManager.Cloud;
import com.sim.DeviceManager.Cloudlet;
import com.sim.DeviceManager.UserDevice;
import com.sim.DeviceManager.VirtualMachine;


public class Sim2 {
	
	public Sim2() {	
	}
		
	public static void main(String[] args) {
		Resources sim = new Resources();
		ArrayList<Cloudlet> cloudlets= sim.generateCloudlets(300, 196, 84);
		Cloud cloud = sim.generateCloud();
		for(Cloudlet cloudlet : cloudlets) {
			for (VirtualMachine vm : cloudlet.getListVM()) {
				cloud.addVM(vm);
			}
		}
		ArrayList<UserDevice> users = sim.generateUsers(300, 196, 84);
		ArrayList<Task> tasks = sim.generateTasks();
		Random random = new Random(19);
		for ( int i = 0 ; i  < Helper.nTasks ; i++) {
			int user = random.nextInt(Helper.nUsers);
			tasks.get(i).setLL(users.get(user).getLL());
			sim.scheduleCloudOnly(cloud, tasks.get(i));
			/*
			System.out.print(i + " ");
			System.out.print(tasks.get(i));
			System.out.println();
			*/
			
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
	
	

