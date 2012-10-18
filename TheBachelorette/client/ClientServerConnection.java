package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;



public class ClientServerConnection implements Runnable {
	
	public Queue<String> m_incomingMsgQueue;
	public Queue<String> m_outgoingMsgQueue;

	private Socket m_socket;
	
	public ClientServerConnection(String host, int port) throws UnknownHostException, IOException
	{
		this.m_incomingMsgQueue = new ConcurrentLinkedQueue<String>();
		this.m_outgoingMsgQueue = new ConcurrentLinkedQueue<String>();
		m_socket = new Socket(host, port);
		
		
		 // clientSocket.close();
	
	}
	
	public void run()
	{

		try {
			DataOutputStream outToServer = new DataOutputStream(m_socket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
			PrintWriter out = new PrintWriter(m_socket.getOutputStream(), true);
			
			while (true) {
				m_outgoingMsgQueue.add("{\"Blah\" : \"Blah\"}");
				//send all outgoing messages
				while (!m_outgoingMsgQueue.isEmpty())  {
					out.println(m_outgoingMsgQueue.remove());
				}
				
				//read incoming messages and put into incoming queue
				while (inFromServer.ready()) {
					m_incomingMsgQueue.add(inFromServer.readLine());
				}
				Thread.sleep(100);
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("broke in client server connection - run");
			System.err.println(e.getMessage());
		}
	}
	
	
}





