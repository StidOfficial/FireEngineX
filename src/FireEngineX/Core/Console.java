package FireEngineX.Core;

public class Console {
	
	public String ClassName;
	
	public Console(String Class) {
		ClassName = Class;
	}
	
	public void WriterLine(Object Value) {
		System.out.println("[CONSOLE][" + ClassName + "] " + Value);
		Logger.Add("[CONSOLE][" + ClassName + "] " + Value);
	}
	
	public void Writer(Object Value) {
		System.out.print("[CONSOLE][" + ClassName + "] " + Value);
		Logger.Add("[CONSOLE][" + ClassName + "] " + Value);
	}
	
	public void Error(Object Value) {
		System.err.println("[CONSOLE][" + ClassName + "][ERROR] " + Value);
		Logger.Add("[CONSOLE][" + ClassName + "][ERROR] " + Value);
	}
}
