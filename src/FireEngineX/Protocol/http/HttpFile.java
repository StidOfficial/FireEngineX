package FireEngineX.Protocol.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.json.simple.JSONObject;

import FireEngineX.Core.Console;

public class HttpFile {

	private Console Console = new Console("HttpFile");
	
	private JSONObject ServerSettings;
	
	private SimpleDateFormat DateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
	
	public String HOSTNAME = null;
	public int PORT = 80;
	
	public HttpFile(JSONObject _ServerSettings) {
		ServerSettings = _ServerSettings;
	}
	
	// START stackoverflow.com
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new java.text.DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	// END
	
	public String[] GetFile(String Header[]) {		
		List<String> ResponseHeader = new ArrayList<String>();
		
		// Beta Debug
		if(Header[0].split(" ").length < 2) {
			for (String string : Header) {
				Console.WriterLine(string);
			}
		}
		
		try {
			String UrlFile = java.net.URLDecoder.decode(Header[0].split(" ")[1], "UTF-8");
			
			UrlFile = (UrlFile.indexOf("?") > 0) ? UrlFile.substring(0, UrlFile.indexOf("?")) : UrlFile;
			
			String ServerFolderPath = ServerSettings.get("WebServer_Directory").toString().replace("{$_HOSTNAME}", HOSTNAME).replace("{$_PORT}", Integer.toString(PORT)).replace("\\/", "/");
			
			File _File = new File(ServerFolderPath + File.separator + UrlFile);
			
			if(_File.exists()) {
				if(_File.isFile()) {
					try (BufferedReader BufferRead = new BufferedReader(new InputStreamReader(new FileInputStream(_File))))
		    		{					
						ResponseHeader.add("HTTP/1.0 202 OK");
						ResponseHeader.add("Connection: keep-alive");
						ResponseHeader.add("Content-Type: " + Files.probeContentType(_File.toPath()));
						ResponseHeader.add("Date: " + DateFormat.format(_File.lastModified()));
						ResponseHeader.add("Server: FireEngineX");
						ResponseHeader.add("");
						
						String Line;
						while((Line = BufferRead.readLine()) != null) {
							ResponseHeader.add(Line);
						}
						
						BufferRead.close();
					} catch (IOException e) {
						ResponseHeader.add("HTTP/1.0 500 Internal Server Error");
						ResponseHeader.add("Connection: keep-alive");
						ResponseHeader.add("Content-Type: text/plain");
						ResponseHeader.add("Date: Wed, 05 Aug 2015 09:32:47 GMT");
						ResponseHeader.add("Server: FireEngineX");
						ResponseHeader.add("");
						ResponseHeader.add("ERROR : " + e.getMessage());
						
						Console.Error(e.getMessage());
						e.printStackTrace();
					}
				} else if(_File.isDirectory()) {
					String IndexFind = null;
					for (String FileName : _File.list()) {
						if(Arrays.asList(ServerSettings.get("IndexFiles").toString().split(";")).contains(FileName)) {
							IndexFind = FileName;
						}
					}
					
					if(IndexFind == null) {
						
						String UrlPathPage = _File.toURI().getPath().replaceFirst(new File(ServerFolderPath).toURI().getPath(), "");
						
						ResponseHeader.add("HTTP/1.0 202 OK");
						ResponseHeader.add("Connection: keep-alive");
						ResponseHeader.add("Content-Type: text/html");
						ResponseHeader.add("Date: Wed, 05 Aug 2015 09:32:47 GMT");
						ResponseHeader.add("Server: FireEngineX");
						ResponseHeader.add("");
						
						ResponseHeader.add("<!DOCTYPE html>");
						ResponseHeader.add("<html>");
						ResponseHeader.add("	<head>");
						ResponseHeader.add("		<title>Index of /" + UrlPathPage + "</title>");
						ResponseHeader.add("		<style>table #tr:nth-child(2n+1){background-color: rgba(0, 0, 0, 0.1);}</style>");
						ResponseHeader.add("	</head>");
						ResponseHeader.add("	<body>");
						ResponseHeader.add("		<pre>");
						ResponseHeader.add("			<table width=\"100%\" style=\"border-collapse:collapse;text-align:left;\">");
						ResponseHeader.add("				<caption><h1>Index of " + UrlPathPage + "</h1></caption>");
						ResponseHeader.add("				<thead style=\"border-bottom:1px solid black;\">");
						ResponseHeader.add("					<tr style=\"height: 25px;\">");
						ResponseHeader.add("						<th width=\"50%\">Name</th>");
						ResponseHeader.add("						<th width=\"25%\">Last Modified</th>");
						ResponseHeader.add("						<th width=\"25%\">Size</th>");
						ResponseHeader.add("					</tr>");
						ResponseHeader.add("				</thead>");
						ResponseHeader.add("				<tbody>");
						ResponseHeader.add("					<tr id=\"tr\" style=\"height: 25px;\">");
						ResponseHeader.add("						<td><a href= \"/" + UrlPathPage.replaceFirst(_File.getName() + "/", "") + "\">..</a></td>");
						ResponseHeader.add("						<td>" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).format(_File.lastModified()) + "</td>");
						ResponseHeader.add("						<td>" + readableFileSize(_File.length()) + "</td>");
						ResponseHeader.add("					</tr>");
						
						for (File File : _File.listFiles()) {
							ResponseHeader.add("					<tr id=\"tr\" style=\"height: 25px;\">");
							ResponseHeader.add("						<td><a href=\"http://" + HttpProtocol.getHeaderString(Header, "Host") + "/" + UrlPathPage + File.getName() +  "\">" + File.getName() + "</a></td>");
							ResponseHeader.add("						<td>" + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).format(File.lastModified()) + "</td>");
							ResponseHeader.add("						<td>" + readableFileSize(File.length()) + "</td>");
							ResponseHeader.add("					</tr>");
						}
						
						ResponseHeader.add("				</tbody>");
						ResponseHeader.add("				<tfoot style=\"border-top:1px solid black;\">");
						ResponseHeader.add("   					<tr style=\"height: 25px;\">");
						ResponseHeader.add("        				<th>FireEngineX Web Server - (c) 2015-2018</th>");
						ResponseHeader.add("        				<th></th>");
						ResponseHeader.add("        				<th style=\"text-align:right;\">" + _File.listFiles().length + " File(s)</th>");
						ResponseHeader.add("     				</tr>");
						ResponseHeader.add("				</tfoot>");

						ResponseHeader.add("			</table>");
						ResponseHeader.add("		</pre>");
						ResponseHeader.add("	</body>");
						ResponseHeader.add("</html>");
					} else {
						File _IndexFile = new File(ServerFolderPath + File.separator + UrlFile + IndexFind);
						try (BufferedReader BufferRead = new BufferedReader(new InputStreamReader(new FileInputStream(_IndexFile))))
			    		{						
							ResponseHeader.add("HTTP/1.0 202 OK");
							ResponseHeader.add("Connection: keep-alive");
							ResponseHeader.add("Content-Type: " + Files.probeContentType(_IndexFile.toPath()));
							ResponseHeader.add("Date: " + DateFormat.format(_IndexFile.lastModified()));
							ResponseHeader.add("Server: FireEngineX");
							ResponseHeader.add("");
							
							String Line;
							while((Line = BufferRead.readLine()) != null) {
								ResponseHeader.add(Line);
							}
							
							BufferRead.close();
						} catch (IOException e) {
							ResponseHeader.add("HTTP/1.0 500 Internal Server Error");
							ResponseHeader.add("Connection: keep-alive");
							ResponseHeader.add("Content-Type: text/plain");
							ResponseHeader.add("Date: Wed, 05 Aug 2015 09:32:47 GMT");
							ResponseHeader.add("Server: FireEngineX");
							ResponseHeader.add("");
							ResponseHeader.add("ERROR : " + e.getMessage());
							
							Console.Error(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			} else {
				ResponseHeader.add("HTTP/1.0 404 Not Found");
				ResponseHeader.add("Connection: keep-alive");
				ResponseHeader.add("Content-Type: text/plain");
				ResponseHeader.add("Date: Wed, 05 Aug 2015 09:32:47 GMT");
				ResponseHeader.add("Server: FireEngineX");
				ResponseHeader.add("");
				
				ResponseHeader.add("404 Not Found");
			}
		} catch (UnsupportedEncodingException e1) {
			Console.Error(e1.getMessage());
			e1.printStackTrace();
		};
		return ResponseHeader.toArray(new String[ResponseHeader.size()]);
	}
}
