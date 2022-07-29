package org.creativecraft.simplevision.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.creativecraft.simplevision.SimpleVisionPlugin;

import java.io.File;
import java.util.UUID;

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

        userData = YamlConfiguration.loadConfiguration(userDataFile);
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
     * Save the player to the user data file.
     */
    public void setPlayer(UUID player, Boolean value) {
        getUserData().set("players." + player, value);
        saveUserData();
    }

    /**
     * Save the player to the user data file.
     */
    public void setPlayer(Player player, Boolean value) {
        setPlayer(player.getUniqueId(), value);
    }

    /**
     * Determine if the player exists in the user data.
     *
     * @return Boolean
     */
    public Boolean hasPlayer(UUID player) {
        return plugin.getUserDataConfig().getBoolean("players." + player);
    }

    /**
     * Determine if the player exists in the userdata.
     *
     * @return Boolean
     */
    public Boolean hasPlayer(Player player) {
        return hasPlayer(player.getUniqueId());
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
