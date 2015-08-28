package FireEngineX.Core;

import java.io.IOException;

public class Logger {
	
	private static Console Console = new Console("Logger");
	
	public static String FILE_NAME = new java.text.SimpleDateFormat("'fireenginex'_yyyy-MM-dd_hh-mm-ss").format(new java.util.Date());
	public static String FILE;
	
	public Logger() {			
		java.io.File LoggerFile = new java.io.File("logs/" + FILE_NAME + ".log");
		FILE = LoggerFile.getPath();
		
		if(!LoggerFile.getParentFile().exists())
			LoggerFile.getParentFile().mkdir();
		if(LoggerFile.exists()) {
			Console.Error("Configuration file exist ! ( " + LoggerFile.getPath() + " )");
			System.exit(0);
		} else if(LoggerFile.canWrite()) {
			Console.Error("Configuration doesn't have access to write !  ( " + LoggerFile.getPath() + " )");
			System.exit(0);
		} else {
			try {
				LoggerFile.createNewFile();
			} catch (IOException e) {
				Console.Error(e.getMessage());
				System.exit(0);
			}
		}
	}
	
	public static void Initialize() {
		Console.WriterLine("Log output to " + FILE);
	}
	
	public static void Add(Object Log) {
		try(java.io.PrintWriter OutputLog = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter("logs/" + FILE_NAME + ".log", true)))) {
		    OutputLog.println(Log);
		}catch (IOException e) {
		    Console.Error(e.getMessage());
		}
	}
	
	public static void Add(String Log) {
		try(java.io.PrintWriter OutputLog = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter("logs/" + FILE_NAME + ".log", true)))) {
		    OutputLog.println(Log);
		}catch (IOException e) {
		    Console.Error(e.getMessage());
		}
	}
	
	public static void Add(String[] Log) {
		try(java.io.PrintWriter OutputLog = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter("logs/" + FILE_NAME + ".log", true)))) {
			for (String value : Log) {
				OutputLog.println(value);
			}
		}catch (IOException e) {
		    Console.Error(e.getMessage());
		}
	}
}
