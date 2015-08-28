# FireEngineX
Web Server Engine writen on Java

## Configuration

Create a serveur exemple in config.json :

{
	"ServerList" : [
		{
			"WebServer_ID" : 0, // Id of your serveur
			"WebServer_Name" :  "Server Test", // Serveur name
			"WebServer_Port" : 80, // Serveur Port
			"WebServer_Directory" : "www", // Local link, exemple : www or External link, exemple : D:\www or /opt/www
			"IndexFiles" : "index.html;index.txt" // Index file of your server
		}
	]
}

Additionnal Option of WebServer_Directory :

Usage exemple : www/{$_HOSTNAME}

Output : www/localhost or www/domain.local

- {$_HOSTNAME} : DNS Hostname client contact
- {$_POST} : Port client contact
