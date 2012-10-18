package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.*;

public class MessageServer implements Runnable {
	
	public Queue<String> m_incomingMsgQueue;
	/*
	public Queue<String> m_client1OutgoingQueue;
	public Queue<String> m_client2OutgoingQueue;
	*/
	private int m_player = 0;
	private ServerSocket m_socket;
	public List<Socket> connections;
	public HashMap<Socket,Queue<String>> m_clientOutgoingQueues;
	
	public MessageServer()
	{
		this.m_incomingMsgQueue = new ConcurrentLinkedQueue<String>();
		/*
		this.m_client1OutgoingQueue = new ConcurrentLinkedQueue<String>();
		this.m_client2OutgoingQueue = new ConcurrentLinkedQueue<String>();
		*/
		this.connections = new ArrayList<Socket>();
		this.m_clientOutgoingQueues = new HashMap<Socket,Queue<String>>();
		try {
			this.m_socket = new ServerSocket(1338);
		}
		catch (Exception e) {
			System.err.println("broken in MessageServer.init");
		}
	}
	
	public void run()
	{
		try {
			this.m_socket.setSoTimeout(1000);
				
		} catch (SocketException se) {
			se.printStackTrace();
		}
		while (true) {
			try {

				Socket listenSocket = this.m_socket.accept();
				if (listenSocket != null) {
					//add to list of active connection
					connections.add(listenSocket);
					HandleRequest requestHandler = new HandleRequest(listenSocket, this, ++m_player);
					ConcurrentLinkedQueue<String> msgQueue = new ConcurrentLinkedQueue<String>();
					this.m_clientOutgoingQueues.put(listenSocket, msgQueue);
					
					Thread t = new Thread(requestHandler);
					t.start();
				}
			}
			catch (SocketTimeoutException se) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					break;
				}
			}
			catch (Exception e) {
				System.err.println("broken in MessageServer.run");
			}
		}
		
		for (Socket s : this.connections) {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

final class HandleRequest implements Runnable
{
	Socket m_socket;
	InputStream m_input;
	MessageServer m_msgServer;
	int m_player;
	
	public HandleRequest(Socket socket, MessageServer msgServer, int player)
	{
		this.m_socket = socket;
		this.m_msgServer = msgServer;
		this.m_player = player;
	}
	
	public void run()
	{
		try {
			// TODO:
			BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.m_socket.getInputStream()));
			PrintWriter out = new PrintWriter(m_socket.getOutputStream(),true);
			
			/*
			if (m_player == 1)
				this.m_msgServer.m_client1OutgoingQueue.add("{\"PlayerID\" : \"1\" }");
			else if (m_player == 2)
				this.m_msgServer.m_client2OutgoingQueue.add("{\"PlayerID\" : \"2\" }");
			*/
			while (true)
			{
				 // remember this blocks! Make client send something every 1-100 ms
				this.m_msgServer.m_incomingMsgQueue.add(br.readLine());
				
				Queue<String> outgoingQueue = this.m_msgServer.m_clientOutgoingQueues.get(this.m_socket);
				if (!outgoingQueue.isEmpty())
					out.println(outgoingQueue.remove());
				
				/*
				// TODO:
				if (m_player == 1 && !this.m_msgServer.m_client1OutgoingQueue.isEmpty()) 
				{
					// TODO: this is a stub, don't just write out a NULL
					
					out.println(this.m_msgServer.m_client1OutgoingQueue.remove());
				} else if (m_player == 2 && !this.m_msgServer.m_client2OutgoingQueue.isEmpty())
				{
					// this.m_socket.getOutputStream().write(this.m_msgServer.m_client2Queue.remove());
					out.println(this.m_msgServer.m_client2OutgoingQueue.remove());
				}
				*/
			}
		}
		catch (Exception e) {
		}
	}
}