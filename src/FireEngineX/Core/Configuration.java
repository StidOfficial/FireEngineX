package FireEngineX.Core;

import java.io.IOException;
import java.nio.file.Files;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Configuration {
	
	private static Console Console = new Console("Configuration");
	
	public static java.io.File ConfigFile = new java.io.File("config.json");
	public static JSONParser ConfigParser = new JSONParser();
	public static JSONObject Config;
	
	public static JSONArray ServerList;
	
	private String ContentFile = "";
	
	public Configuration() {
		Console.WriterLine("Initialize...");
		if(!ConfigFile.exists()) {
			Console.Error("Configuration file not found !");
			System.exit(0);
		} else if(!ConfigFile.canRead()) {
			Console.Error("Configuration doesn't read !");
			System.exit(0);
		}
		
		try {
			for (String Line : Files.readAllLines(ConfigFile.toPath())) {
				ContentFile += Line;
			}
			Config = (JSONObject)new JSONParser().parse(ContentFile);
			if(Config.get("ServerList") == null)
			{
				Console.Error("Not found 'ServerList' to config file !");
				System.exit(0);
			}
			
			ServerList = (JSONArray)Config.get("ServerList");
		} catch (IOException e) {
			Console.Error(e.getMessage());
		} catch (ParseException e) {
			Console.Error(e.getUnexpectedObject());
		}
	}
}
