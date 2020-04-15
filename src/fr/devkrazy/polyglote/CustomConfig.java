package fr.devkrazy.polyglote;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private JavaPlugin plugin;
    private String name;
    private File file;
    private FileConfiguration config;

   public CustomConfig(JavaPlugin plugin, String name) {
       this.plugin = plugin;
       this.name = name;

       this.file = new File(plugin.getDataFolder(), this.name);
       //TODO factoriser ce code
       if (this.file.exists() == false) {
           file.getParentFile().mkdir();
           plugin.saveResource(this.name, false);
       }

       this.config = new YamlConfiguration();
       try {
           this.config.load(this.file);
       } catch (IOException | InvalidConfigurationException e) {
           e.printStackTrace();
       }
   }

    /**
     * Reloads the config.
     */
   public void reload() {
       if (this.file.exists() == false) {
           file.getParentFile().mkdir(); // Re-creates the parent directory of the file
           plugin.saveResource(this.name, false); // If the file doesn't exist, extract the file from the jar into the directory
       }
       try {
           this.config.load(this.file);
       } catch (IOException | InvalidConfigurationException e) {
           e.printStackTrace();
       }
   }

    /**
     * Saves the config.
     */
   public void save() {
       try {
           this.config.save(this.file);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    /**
     * Returns the FileConfiguration instance.
     * @return
     */
   public FileConfiguration getConfig() {
       return this.config;
   }
}
