import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.*;

public class MessageServer implements Runnable {
	
	public Queue<String> m_incomingMsgQueue;
	public Queue<String> m_client1Queue;
	public Queue<String> m_client2Queue;
	private int m_player = 1;
	private ServerSocket m_socket;
	
	public MessageServer()
	{
		this.m_incomingMsgQueue = new ConcurrentLinkedQueue<String>();
		this.m_client1Queue = new ConcurrentLinkedQueue<String>();
		this.m_client2Queue = new ConcurrentLinkedQueue<String>();
		try {
			this.m_socket = new ServerSocket(1337);
		}
		catch (Exception e) {
			System.err.println("broken in MessageServer.init");
		}
	}
	
	public void run()
	{
		while (true) {
			try {
			Socket listenSocket = this.m_socket.accept();
			if (listenSocket != null) {
				HandleRequest requestHandler = new HandleRequest(listenSocket, this, m_player++);
				Thread t = new Thread(requestHandler);
				t.start();
			}
			}
			catch (Exception e) {
				System.err.println("broken in MessageServer.run");
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
			this.m_input = this.m_socket.getInputStream();
			
			while (true)
			{
				this.m_input.read();  // remember this blocks! Make client send something every 1-100 ms
				this.m_msgServer.m_incomingMsgQueue.add(null);
				
				// TODO:
				if (m_player == 1 && !this.m_msgServer.m_client1Queue.isEmpty()) 
				{
					this.m_socket.getOutputStream().write(null);
				} else if (m_player == 2 && !this.m_msgServer.m_client2Queue.isEmpty())
				{
					// this.m_socket.getOutputStream().write(this.m_msgServer.m_client2Queue.remove());
				}
			}
		}
		catch (Exception e) {
		}
	}
}