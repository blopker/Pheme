package adapters.rmi.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import adapters.rmi.api.DTOs.DTO;

public class Pheme {
	final BlockingQueue<DTO> messageQueue = new LinkedBlockingQueue<DTO>();
	final Sender sender;
	
	public Pheme(String hostname) {
		String serverDomainName = (hostname == null) ? "localhost" : hostname;
		String url = "//" + serverDomainName + "/" + PhemeAPI.SERVICE_NAME;
		
		sender = new Sender(url, messageQueue);
		Thread t = new Thread(sender);
		t.start();
	}
	
	public Component register(String component, ComponentType type){
		return new Component(component, type, messageQueue);
	}
}
