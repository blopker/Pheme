package test;

import models.clients.SocketClient;
import models.datatypes.DataType;
import models.datatypes.Log;

import org.junit.Test;

public class SocketTest {
	
	class TestSocketClient extends SocketClient{
		public boolean hasData(DataType data) {
			return this.dataQueue.contains(data);
		}
	}
	
	@Test
	public void testSocketClient() {
		Log log = new Log(); 
		TestSocketClient client = new TestSocketClient();
		client.send(log);
		client.hasData(log);
	}
}
