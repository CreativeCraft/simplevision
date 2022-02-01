package org.creativecraft.simplevision.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.creativecraft.simplevision.SimpleVisionPlugin;

import java.io.File;

public class MessagesConfig {
    private final SimpleVisionPlugin plugin;
    private FileConfiguration messages;
    private File messagesFile;

    public MessagesConfig(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
        this.register();
        this.setDefaults();
    }

    /**
     * Register the messages configuration.
     */
    public void register() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();

        try {
            messages.load(messagesFile);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Register the messages defaults.
     */
    public void setDefaults() {
        messages.addDefault("messages.generic.prefix", "&a&lNight&fvision &8> &f");

        messages.addDefault("messages.toggle.on", "You have toggled nightvision &aon&f.");
        messages.addDefault("messages.toggle.action-bar", "Nightvision is &aenabled");
        messages.addDefault("messages.toggle.off", "You have toggled nightvision &coff&f.");
        messages.addDefault("messages.toggle.description", "Toggle nightvision mode.");

        messages.addDefault("messages.list.header", "&a&m+&8&m                              &a&l Night&fVision &8&m                              &a&m+");
        messages.addDefault("messages.list.format", "&8➝ &fPlayers:&a {players}");
        messages.addDefault("messages.list.delimiter", "&7,&a ");
        messages.addDefault("messages.list.footer", "&a&m+&8&m                                                                            &a&m+");
        messages.addDefault("messages.list.description", "List players with nightvision active.");

        messages.addDefault("messages.reload.success", "Nightvision has been &asuccessfully&f reloaded.");
        messages.addDefault("messages.reload.failed", "Nightvision &cfailed&f to reload. Check console for details.");
        messages.addDefault("messages.reload.description", "Reload the plugin configuration.");

        messages.addDefault("messages.help.header", "&a&m+&8&m                              &a&l Night&fVision &8&m                              &a&m+");
        messages.addDefault("messages.help.format", "&8➝ &a/{command} &7{parameters} &f- {description}");
        messages.addDefault("messages.help.footer", "&a&m+&8&m                                                                            &a&m+");
        messages.addDefault("messages.help.description", "View the plugin help.");

        messages.options().copyDefaults(true);

        saveMessages();
    }

    /**
     * Retrieve the messages configuration.
     *
     * @return FileConfiguration
     */
    public FileConfiguration getMessages() {
        return messages;
    }

    /**
     * Retrieve the messages file.
     *
     * @return File
     */
    public File getMessagesFile() {
        return messagesFile;
    }

    /**
     * Save the messages file.
     *
     * @return void
     */
    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (Exception e) {
            //
        }
    }
}
