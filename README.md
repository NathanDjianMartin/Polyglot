# Polyglot
Integrating a support for multi-languages messages in a Bukkit/Spigot plugin can be difficult. Polyglot provides an easy way to manage every player's language on a server. It comes with an API for developpers who want to support multi-languages messages in their plugin. In addition to managing multiple languages on your server, Polyglot stores each player's selected language in the `players_languages.yml` configuration file.

Please note that Polyglot uses the [**ISO 639-1**](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) language naming convention. **_It is necessary to stick with this convention when using Polyglot._**

## Polyglot _/language_ command
Polyglot comes with the /language command to manually manage a player's language.
- `/language help` displays in-game help for the command
- `/language set <player> <language ISO 639-1 code>` sets a player's language using the language's ISO 639-1 code
- `/language see <player>` displays a player's language
- `/language reload`reloads the player_languages.yml file and all the plugin's language files
- `/language save`saves the player_languages.yml file and all the plugin's language files
Please note that currently, you can only use this command with online players. Support for offline players will be added in the future.

 ## Polyglot for plugin developpers
 This section describes all the requirements your plugin has to comply with to correctly work with Polyglot. It is now assumed that you imported Polyglot as a dependency in your favorite IDE.
 
 ### Creating your language files
 The first thing you need to do is to create at least one language file. Language files must comply with the following naming convention: `language_xx.yml`, where you replace *xx* with the language [**ISO 639-1**](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) code. A language file is a `.yml` file which associates a message key with a message value in this language. For example if you want to create a file for english messages you have to name it `language_en.yml`. Let's take a look at our example `language_en.yml` file:
 
 ```yaml
 #language_en.yml
 welcome: "Welcome %1$s to the server!"
 permission-denied: "Sorry but you don't have the permission to do this"
 ```
You can create as many language files as you want. You must put these files at the same level as your `plugin.yml` file. You may have noticed the `%1$s` in the `welcome` message. This is a Java `String.format()` placeholder. These placeholders allow you to include dynamic parameter values in your messages when you send them to a player (see the **Sending messages** section). If you don't know how to use such placeholders, you are strongly advised to look at some documentation before writing your own messages.

 ### The PluginLanguageAssets interface
 Then you have to create a class which implements the `PluginLanguageAssets` interface. This interface allow you to pass necessary assets to Polyglot so he can correctly manage your plugin's languages. This interface has three methods:
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
    // You can store your message keys in a enum (or in an HashMap, or a List for example)
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
 Finally you have to register your plugin using the `LanguageManager`'s instance. When registering your plugin, the `LanguageManager` will also return a `PluginLanguageManager` instance. This is **your plugin's** instance that Polyglot created just for **you**, so you need store it somewhere. One way is to store it as a static field in your class which extends `JavaPlugin` and to add a static getter. It is necessary to store your `PluginLanguageManager` because you need it to retrieve and to send messages to players in the correct language automatically. Here's an example of how you can register your plugin and store your `PluginLanguageManager`instance.
 
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
 Even if you can use the in-game command `/language set <player> <language ISO 639-1 code>` to set a player's language, you may want to also include this functionality in a plugin. To set a player's language, simply do the following:
 ```java
 LanguageManager.getInstance().setLanguageName(UUID playerUniqueId, String languageISOCode);
 ```
 
 ### Sending messages to players
 Congratulation! You are now ready to send your messages. This where you need your `PluginLanguageManager`. There are currently two methods for sending messages to a player. You can either send a raw message from the language file, or, send a message which contains dynamic parameter values.
 ```java
 PluginLanguageManager pluginLanguageManager = ... // get your PluginLanguageManager instance
 
 pluginLanguageManager.sendMessage(Player player, String messageKey);
 
 pluginLanguageManager.sendMessageWithParameters(Player player, String messageKey, Object... parameters);
```

Thanks to Polyglot you don't have to worry in which language the message will be sent, your `PluginLanguageManager` will take care of this. However, you must understand how "Java String format" placeholders work. 

Now is the time to use our `PluginLanguageManager` in two practical examples. We are going to use the two messages shown in our `language_en.yml` file above. Let's suppose that we want to prevent players without the `block.break` permission to break blocks on the server. Given a user who does not have the `block.break` permission, when they try to break a block, then we  send them the `permission-denied` message as shown below:
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
 
 Let's also suppose that we want to welcome each player personnaly when they join our server. We could send a message with the player name as a dynamic parameter value:
 
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
 In the `PlayerJoinEvent` example, the placeholder `%1$s` from the `welcome` message will be replaced by the player's name because `%1$` means the first parameter and `s`means that it is a `String`. Again, the Java documentation will detail which placeholders you can use and how to use them. You can define as many parameters as you like after the `messageKey` parameter, indeed the last parameter's type is `Object...`. 
 
### Limitations
Polyglot has the following limitations:
- When a player connects to the server, if the player never joined the server, or was not saved in the `players_lanuages.yml` file, its default language will be set to `en` (english). This means that all your server plugins which use Polyglot must support the english language. In the future, you will be able to change the default language in a configuration file. 
- A player cannot set their own language, you must use the `setLanguageName(UUID playerUniqueID)` method in your plugin code, or manually set it in-game using the `/language set <player> <language ISO 639-1 code>` command.
 
### Conclusion
You can now use Polyglot to easily add multi-language support to your plugins. If you have any issue or recommandation, feel free to private message me. 
 
