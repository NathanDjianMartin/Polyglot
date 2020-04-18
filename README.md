# Polyglote
Integrating a support for multi-languages messages in a Bukkit/Spigot plugin can be difficult. Polyglot provides you an easy way to manage every player's language on a server. It comes with an API for developpers who wants to support multi-languages messages in their plugin. In addition of managing multiple languages on your server, Polyglot stores every player's language in a the `players_languages.yml` configuration file.

Please note that Polyglot uses the [**ISO 639-1**](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) language naming convention. **_It is necessary to stick with this convention when using Polyglot._**

## Polyglot _/language_ command
Polyglot comes with the /language command to manually manage a player's language. The current limitation of this command is that you can only use it on online players. Support for offline players will be added in the future.<br/>
- `/language help` displays in-game help for the command
- `/language set <player> <language ISO 639-1 code>` sets a player's language using the language's ISO 639-1 code
- `/language see <player>` displays a player's language
- `/language reload`reloads the player_languages.yml file and all the plugin's language files
- `/language save`saves the player_languages.yml file and all the plugin's language files

 ## Polyglot for developpers
 This section describes all the requirements your plugin has to comply with to correctly implement Polyglot. It is now assumed that you imported Polyglot as a dependance in your favorite IDE.
 
 ### Creating your language files
 The first thing you need to do is to create at least one language file. The language files must be named like this `language_xx.yml` replace *xx* with the language [**ISO 639-1**](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) code. For example if you wanted to create a file for english messages you would name it `language_en.yml`. Let's take a look at our example `language_en.yml` file:
 
 ```yaml
 #language_en.yml
 welcome: "Welcome %1$s to the server!"
 permission-denied: "Sorry but you don't have the permission to do this"
 ```
You can create as much files as you want. You must put those files at the same level of your `plugin.yml` file. You may have noticed the `%1$s` in the `welcome` message. This is a Java `String.format()` placeholder. These placeholder are used to allow you to include variables in your messages when you send them to a player (see the **Sending messages** section). If you don't know how to use those placeholders, it is strongly advised to look at some documentation before writing your own messages.

 ### The PluginLanguageAssets interface
 Then you have to create a class which implements the `PluginLanguageAssets` interface. This interface has three methods:
 - `Set<String> getMessageKeys();`
 - `Set<File> getLanguagesFiles();`
 - `JavaPlugin getPlugin();`

Here is an example of how you could override those methods:

```java
public class YourPluginLanguageAssets implements PluginLanguageAssets {

    // We have to provide a Set of all of our message keys
    @Override
    public Set<String> getMessagesKeys() {
        Set<String> messagesKeys = new HashSet<>();
        for (MessageKey messageKey : MessageKey.values()) {
            messagesKeys.add(messageKey.getKey());
        }
        return messagesKeys;
    }

    // We have to provide a Set of all of our language files 
    @Override
    public Set<File> getLanguagesFiles() {
        // Here we give two hardcoded files to Polyglot
        // You could do this more elegantly by scanning your jar's resource folder and 
        // look for languages files for example.
        Set<File> languagesFiles = new HashSet<>();
        languagesFiles.add(new File("language_en.yml"));
        languagesFiles.add(new File("language_fr.yml"));
        return languagesFiles;
    }

    // We have to provide the instance of our class which extends JavaPlugin
    @Override
    public JavaPlugin getJavaPlugin() {
        // return the instance of your class which extends JavaPlugin
    }
}
```

And here is our `MessageKey` enum:

```java
    // You can store your message keys in a enum for example
    enum MessageKey {
        WELCOME("welcome"), PERMISSION_DENIED("permission-denied");
        
        private String key;
        
        MessageKey(String key) {
            this.key = key;
        }
        
        public String getKey() {
            return this.key;
        }
    }
```
	
	
 
 ### Registering your plugin
 Finally you have to register your plugin using the `LanguageManager`'s instance. When registering your plugin, the `LanguageManager` will also return a `PluginLanguageManager` instance. This is **your** instance that Polyglot created just for **you** (how cute is that?) so store it somewhere. The easiest way would be to store it as a static field in your class which extends `JavaPlugin` and add a static getter. It is necessary to store your `PluginLanguageManager` because it allows you to get and send messages to players in the correct language automatically. Here's an example of how you could register your plugin and store your `PluginLanguageManager`instance.
 
 ```java
 public class YourPlugin extends JavaPlugin {
 
    private static PluginLanguageManager yourPluginLanguageManager;
	
	@Override
	public void onEnable() {
		yourPluginLanguageManager = LanguageManager.getInstance().registerPlugin(this, new YourPluginLanguageAssets());
	}
	
	public static PluginLanguageManager getPluginLanguageManager() {
		return yourPluginLanguageManager;
	}
 }
 ```
 
 ### Setting and a player's language using code
 Even if you can use the in-game command `/language set <player> <language ISO 639-1 code>` to set a player's language. You may want to include this functionnality in a plugin. To set a language simply do:
 ```java
 LanguageManager.getInstance().setLanguageName(UUID playerUniqueId, String languageISOCode);
 ```
 
 ### Sending messages to players
 Congratulation! You are now ready to send your messages. That's where your `PluginLanguageManager` becomes important. He has all the methods to send messages to players. You have currently two ways of sending messages to a player. You can either send the raw message from the language file or send a message where you integrate some parameters.
 ```java
 PluginLanguageManager pluginLanguageManager = ... // get your PluginLanguageManager instance
 
 pluginLanguageManager.sendMessage(Player player, String messageKey);
 
 pluginLanguageManager.sendMessageWithParameters(Player player, String messageKey, Object... parameters);
```

Thanks to Polyglot you don't how to worry in which language the message will be sent, your `PluginLanguageManager` will take care of this. However, you must understand how Java String format placeholders work. 

Now it is time to use our `PluginLanguageManager` in two concrete cases. We are going to use the two messages of our example `language_en.yml` file from the beginning. Lets suppose that we want to prevent players without the `block.break` permission to break blocks on the server. When they try to break a block and the event is cancelled, we want to inform them that they don't have the permission, so we send him the `permission-denied` message when he tries to break a block:
 ```java
 public class YourPluginEvents implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PluginLanguageManager pluginLanguageManager = YourPlugin.getPluginLanguageManager();
        if (player.hasPermission("block.break") == false) {
            event.setCancelled(true);
            pluginLanguageManager.sendMessage(player, MessageKey.PERMISSION_DENIED.getKey());
        }
    }
}
 ```
 
 Now we want to warmly welcome our player when they join our server. But we want our message to be personnal and include the player's name. That's a case where we would send a message with parameters:
 
 ```java
 public class YourPluginEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PluginLanguageManager pluginLanguageManager = YourPlugin.getPluginLanguageManager();
        pluginLanguageManager.sendMessageWithParameters(player, MessageKey.WELCOME.getKey(), player.getName());
    }

}
 ```
 In the `PlayerJoinEvent` example, the placeholder `%1$s` from the `welcome` message will be replaced by the player name because `%1$` means the first parameter and `s`means that it is a `String`. Again, the Java documentation will detail which placeholders you can use and when to use them. You can put as many parameters as you want after the `messageKey` parameter, indeed the last parameter's type is `Object...`. 
 
### Limitations
Polyglot has currently a few limitations that you want to be aware of.
- When a player connects to the server, if the player never joined the server or simply saved in the `players_lanuages.yml` file, its default language will be set to `en`. It means that all of your server plugins which use Polyglot must support the english language. In the future you will be able to change the default language in configuration file. 
- A player cannot set its own language, you must provide a plugin which will set a player's language or manually set it using the `/language set <player> <language ISO 639-1 code>` in-game command.
 
### Conclusion
You have learned how to use Polyglot to easily add multi-languages support to your plugins. If you have any issue or recommandation feel free to private message me. 
 
