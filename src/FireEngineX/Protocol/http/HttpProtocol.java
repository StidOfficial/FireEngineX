package FireEngineX.Protocol.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import FireEngineX.Core.Console;

public class HttpProtocol {
	
	private Console Console = new Console("HttpProtocol");
	
	private JSONObject ServerSettings;
	
	private Socket WebSocket;
	private BufferedReader ReaderMessage;
	private PrintWriter WriterMessage;
	
	public HttpProtocol(JSONObject _ServerSettings, Socket _WebSocket) {
		ServerSettings = _ServerSettings;
		
		WebSocket = _WebSocket;
		try {
			ReaderMessage = new BufferedReader(new InputStreamReader(_WebSocket.getInputStream()));
			WriterMessage = new PrintWriter(new BufferedWriter(new OutputStreamWriter(WebSocket.getOutputStream())), true);
		} catch (IOException e) {
			Console.Error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String[] GetHTTPHeader() {
		List<String> Header = new ArrayList<String>();
		try {
			String ReadMessage;
			while ((ReadMessage = ReaderMessage.readLine()) != null && ReadMessage.length() != 0) {
				Console.WriterLine(ReadMessage);
				if(Header.size() == 0 && !(ReadMessage.startsWith("GET") || ReadMessage.startsWith("POST"))) {
					Console.Error("Header Error (" + ReadMessage + ")");
					ReaderMessage.close();
					WriterMessage.close();
		        	WebSocket.close();
				}
				Header.add(ReadMessage);
			}
		} catch (IOException e) {
			Console.Error(e.getMessage());
			e.printStackTrace();
		}
		return Header.toArray(new String[Header.size()]);
	}
	
	public void GetResponse(String[] Header) {
		HttpFile HttpFile = new HttpFile(ServerSettings);
		
		for (String HeaderLine : HttpFile.GetFile(Header)) {
			WriterMessage.println(HeaderLine);
		}
	}
	
	public void GetDisconnect() {
		try {
			ReaderMessage.close();
			WriterMessage.close();
		    WebSocket.close();
		} catch (IOException e) {
			Console.Error("GetDisconnect : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
