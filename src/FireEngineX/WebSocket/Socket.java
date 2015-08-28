package FireEngineX.WebSocket;

import java.io.IOException;

import org.json.simple.JSONObject;

import FireEngineX.Core.*;
import FireEngineX.Protocol.http.HttpProtocol;

public class Socket {

	private static Console Console = new Console("Socket");
	
	public static void Open(JSONObject ServerSettings) {
		Console.WriterLine("Open connection to " + ServerSettings.get("WebServer_Port"));
		try {
			@SuppressWarnings("resource")
			java.net.ServerSocket Socket = new java.net.ServerSocket(Integer.parseInt(ServerSettings.get("WebServer_Port").toString()));
			
			while(true) {
				final java.net.Socket WebSocket;
				try {
			        WebSocket = Socket.accept();
			        
			        new Thread(new Runnable() {
						@Override
						public void run() {
							HttpProtocol _HttpProtocol = new HttpProtocol(ServerSettings, WebSocket);
					        
					        String[] Header = _HttpProtocol.GetHTTPHeader();
					        Logger.Add(Header);
					        
					        if(Header.length != 0 && !WebSocket.isClosed()) {
					        	_HttpProtocol.GetResponse(Header);
					        }
					        
					        _HttpProtocol.GetDisconnect();
						}
					}).start();
				} catch(Exception e) {
					Console.Error(e.getMessage());
					e.printStackTrace();
					
					Console.Error("Socket Closed !");
				}
			}
		} catch(IOException e) {
			Console.Error("e " + e.getMessage());
			e.printStackTrace();
		}
		Console.WriterLine("Close connection to " + ServerSettings.get("WebServer_Port"));
	}
}
