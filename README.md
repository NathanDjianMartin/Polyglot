# Polyglote
Integrating a support for multi-languages messages in a Bukkit/Spigot plugin can be difficult. Polyglot provides you an easy way to manage every player's language on a server. It comes with an API for developpers who wants to support multi-languages messages in their plugin. 

Please note that Polyglot uses the **ISO 639-1** language naming convention. **_It is necessary to stick with this convention when using Polyglot._**

## Polyglot _/language_ command
Polyglot comes with the /language command to manually manage a player's language. The current limitation of this command is that you can only use it on online players. Support for offline players will be added in the future.<br/>

`/language help` displays in-game help for the command<br/>
`/language set <player> <language ISO 639-1 code>` sets a player's language using the language's ISO 639-1 code<br/>
`/language see <player>` displays a player's language<br/>
`/language reload`reloads the player_languages.yml file and all the plugin's language files<br/>
`/language save`saves the player_languages.yml file and all the plugin's language files<br/>

 ## Polyglot for developpers
 This section describes all the requirements your plugin has to comply with to correctly implement Polyglot. It is now assumed that you imported Polyglot as a dependance in your favorite IDE.
 
 ### Create your language files
 The first thing you need to do is to create at least one language file.
 
 ### The PluginLanguageAssets interface
 Then you have to create a class which implements the `PluginLanguageAssets` interface and override its 3 methods.
 
 ### Register your plugin
 Finally you have to register your plugin using the `LanguageManager`'s instance. When registering your plugin, the `LanguageManager` will also return a `PluginLanguageManager` instance. This is **your** instance that Polyglot created just for **you** (how cute is that?) so store it somewhere. The easiest way would be to store it as a static field in your class which extends `JavaPlugin` and add a static getter. It is necessary to store your `PluginLanguageManager` because it allows you to get and send messages to players in the correct language automatically. Here's an example of how you could register your plugin and store your `PluginLanguageManager`instance.
 ```java
 public class YourPlugin extends JavaPlugin {
 
    private static PluginLanguageManager yourPluginLanguageManager;
	
	@Override
	public void onEnable() {
		yourPluginLanguageManager = LanguageManager.getInstance().registerPlugin(this, YourPluginLanguageAssets.getInstance());
	}
	
	public static PluginLanguageManager getPluginLanguageManager() {
		return yourPluginLanguageManager;
	}
 }
 ```
 
 

 
