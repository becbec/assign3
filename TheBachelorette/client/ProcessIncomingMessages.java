package client;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessIncomingMessages implements Runnable {
	ClientServerConnection csConnection;
	
	public ProcessIncomingMessages(ClientServerConnection connection) {
		csConnection = connection;
	}

	@Override
	public void run() {
		
		String s;
		
		while(true) {
			if (!csConnection.m_incomingMsgQueue.isEmpty()) {
				s = csConnection.m_incomingMsgQueue.remove();
				try {
					JSONObject j = new JSONObject(s);
					System.out.println(j.get("Message"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Error in PIM - converting string to JSON");
				}
				
				
				
				System.out.println(csConnection.m_incomingMsgQueue.remove()); 
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
