package org.creativecraft.simplevision.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.creativecraft.simplevision.SimpleVisionPlugin;

import java.io.File;

public class UserDataConfig {
    private final SimpleVisionPlugin plugin;
    private FileConfiguration userData;
    private File userDataFile;

    public UserDataConfig(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
        this.register();
    }

    /**
     * Register the user data configuration.
     */
    public void register() {
        userDataFile = new File(plugin.getDataFolder(), "userdata.yml");

        if (!userDataFile.exists()) {
            userDataFile.getParentFile().mkdirs();
            plugin.saveResource("userdata.yml", false);
        }

        userData = new YamlConfiguration();

        try {
            userData.load(userDataFile);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Retrieve the user data configuration.
     *
     * @return FileConfiguration
     */
    public FileConfiguration getUserData() {
        return userData;
    }

    /**
     * Retrieve the user data file.
     *
     * @return File
     */
    public File getUserDataFile() {
        return userDataFile;
    }

    /**
     * Save the user data.
     */
    public void saveUserData() {
        try {
            userData.save(userDataFile);
        } catch (Exception e) {
            //
        }
    }
}
