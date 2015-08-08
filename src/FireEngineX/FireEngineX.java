package FireEngineX;

import org.json.simple.JSONObject;

import FireEngineX.Core.*;
import FireEngineX.WebSocket.Socket;

public class FireEngineX {

	private static Console Console = new Console("Main");
	
	public static void main(String[] args) {
		new Logger();
		
		Console.WriterLine("Initialize...");
		Logger.Initialize();
		new Configuration();
		
		for (int i = 0; i < Configuration.ServerList.size(); i++) {
			JSONObject ServerSettings = (JSONObject)Configuration.ServerList.get(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Socket.Open(ServerSettings);
				}
			}).start();
		}
	}

}