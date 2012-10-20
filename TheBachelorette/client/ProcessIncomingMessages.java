package client;

import java.io.IOException;

import game.PlayerCharacter;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessIncomingMessages implements Runnable {
	ClientServerConnection csConnection;
	GameClient gc;
	PlayerCharacter character;
	
	public ProcessIncomingMessages(ClientServerConnection connection, GameClient gc, PlayerCharacter character) {
		csConnection = connection;
		this.gc = gc;
		this.character = character;
	}

	@Override
	public void run() {
		
		String s;
		
		while(true) {
			if (!csConnection.m_incomingMsgQueue.isEmpty()) {
				s = csConnection.m_incomingMsgQueue.remove();
				System.out.println("PIM:" + s);
				try {
					JSONObject j = new JSONObject(s);
					if (j.has("Message")) {
						System.out.println(j.get("Message"));
						if (j.has("UpdateCharacter")) {
							gc.updateCharacterFlag = true;
						}
						
					} else if (j.has("PlayerID")) {
						System.out.println("playerId geting set " + j.getString("PlayerID"));
						gc.playerID = j.getString("PlayerID");
					}
				} catch (JSONException e) {
					System.err.println("Error in PIM - converting string to JSON");
				}
				
				
				
				//System.out.println(csConnection.m_initcomingMsgQueue.remove()); 
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
