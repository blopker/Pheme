package models;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

public class SystemInfo {
	int value;
	Map<String, Object> info;
	
	public SystemInfo(){
		info = new HashMap<>();
		Runtime rt = Runtime.getRuntime();
		info.put("Free Memory", rt.freeMemory());
		info.put("Avaliable Processors", rt.availableProcessors());
		info.put("Memory Used", rt.totalMemory());
		info.put("Heap Memory", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
		
	}
	
	public static void run() {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					SystemInfo.announce();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
	
	private static void announce() {
		EventBus.post(new SystemInfo());
	}
	
	public Map<String, Object> getInfo() {
		return info;
	}
}
