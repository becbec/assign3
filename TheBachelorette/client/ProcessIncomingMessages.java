package client;

public class ProcessIncomingMessages implements Runnable {
	ClientServerConnection csConnection;
	
	public ProcessIncomingMessages(ClientServerConnection connection) {
		csConnection = connection;
	}

	@Override
	public void run() {
		while(true) {
			if (!csConnection.m_incomingMsgQueue.isEmpty()) {
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
