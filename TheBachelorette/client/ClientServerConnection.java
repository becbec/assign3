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
				m_outgoingMsgQueue.add("{\"blah\" : \"blah\"}");
				//send all outgoing messages
				while (!m_outgoingMsgQueue.isEmpty())  {
					out.println(m_outgoingMsgQueue.remove());
				}
				
				//read incoming messages and put into incoming queue
				while (inFromServer.ready()) {
					m_incomingMsgQueue.add(inFromServer.readLine());
				}
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("broke in client server connection - run");
			System.err.println(e.getMessage());
		}
	}
	
	
}


/*
 Message Types (server to client)
 
 TEXT/FEEDBACK ETC.
 MessageType : text
 Message : Message Text
 
 CHALLENGE.
 MessageType: challenge
 RoundType: round type eg. intelligence, charm etc
 Message: text
 
 
 CHALLENGE FEEDBACK.
 MessageType: ChallengeFeedback
 Correct: yes/no
 Girl: Did they get the number/ how close are they etc.
 
 
 
 
 Message Types (client to server)
 CHOOSING CHARACTER
"ChoosingCharacter" :
	Att1 : blah
	...
	
 
 CHOOSING GIRL
"ChoosingGirl" :
 	Player: playernumber
 	Girl: girlnumber
 
CHOOSING ROUND TYPE
ChoosingRoundType
	Player : playernumber
 	RoundType : roundtype
 
 
 SUBMITTING ANSWER
SubmittingAnswer
	Player: playernumber
 	Girl: Chosengirl //do we need this or will server know this
 	server will store current points? otherwise current points
 	Answer: answer
 

 
 */



