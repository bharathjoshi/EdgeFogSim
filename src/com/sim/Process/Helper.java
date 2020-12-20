package com.sim.Process;
import java.util.Random;
import com.sim.DeviceManager.VirtualMachine;
import com.sim.MobilityManager.Location;
import com.sim.Tasks.Task;

public class Helper {
	
	public static int nUsers = 45;
	public static int nTasks = 25*10;
	public static int time = 25;

	
	public static float getdistance(Location l1,Location l2) {
		return (float) (Math.sqrt((l1.getXpos()-l2.getXpos())*(l1.getXpos()-l2.getXpos())+(l1.getYpos()-l2.getYpos())*(l1.getYpos()-l2.getYpos())));
	}
	
	public static boolean computationPossible(Task t, VirtualMachine vm) {
		return t.getCCRequired()<= vm.getCC(); 
	}
	
	
	public static float computationalTimeDevice(Task t, VirtualMachine vm) {
		return t.getCCRequired()/vm.getRate();
	}
	
	public static boolean securityCompatible(Task t, String level) {
		if (t.getP1()<=0.33) {
			return true ;
		}
		else if ((t.getP1()>0.33 && t.getP1()<=0.66) && (level == "Moderate" || level == "High") ){
			return true;
		}
		else if(t.getP1()>0.66 && level == "High") {
			return true;
		}
		return false;
	}
	
	public static float getRTT(Task t , VirtualMachine vm) {
		float distance = Helper.getdistance(t.getLL(), vm.getLL());
		float factor=0;
		Random random = new Random();
		if(distance==0) {
			factor  = 0 ;
		}
		else if (distance < 235) {
			factor  =  random.nextInt(20) + 50;
		}
		else if (distance < 293) {
			factor = random.nextInt(20)+70;
		}
		else {
			factor  = random.nextInt(50)+120;
		}
		
		
		return factor/1000;
	}
	public static float dataTransmission(Task t, VirtualMachine vm) {
		float distance =  Helper.getdistance(t.getLL() , vm.getLL());
		if (distance !=0) {
			if (distance < 235) {
				return t.getCCRequired()/600;
			}
			else if (distance < 292) {
				return t.getCCRequired()/300;
			}
			else {
				return t.getCCRequired()/200;
			}
			
		}
		return 0;
	}
	
}
