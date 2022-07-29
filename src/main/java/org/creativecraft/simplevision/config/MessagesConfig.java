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
    }

    /**
     * Register the message configuration.
     */
    public void register() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);

        setDefaults();
    }

    /**
     * Register the messages defaults.
     */
    public void setDefaults() {
        messages.addDefault("messages.generic.prefix", "&a&lNight&fvision &8> &f");
        messages.addDefault("messages.generic.enabled", "Enabled");
        messages.addDefault("messages.generic.disabled", "Disabled");

        messages.addDefault("messages.events.login", "Your &anightvision&f has been &are-enabled&f.");
        messages.addDefault("messages.events.death", "Your &anightvision&f persists through &adeath&f.");
        messages.addDefault("messages.events.milk-bucket", "Your &anightvision&f persists through &amilk bucket&f.");

        messages.addDefault("messages.toggle.enable", "You have toggled nightvision &aon&f.");
        messages.addDefault("messages.toggle.enable-other", "You have toggled &a{player}'s&f nightvision &aon&f.");
        messages.addDefault("messages.toggle.disable", "You have toggled nightvision &coff&f.");
        messages.addDefault("messages.toggle.disable-other", "You have toggled &a{player}'s&f nightvision &coff&f.");
        messages.addDefault("messages.toggle.action-bar", "Nightvision is &aenabled");
        messages.addDefault("messages.toggle.not-found", "The player &a{player}&f could not be found.");
        messages.addDefault("messages.toggle.no-permission", "You are not &aallowed&f to toggle another player's &anightvision&f.");
        messages.addDefault("messages.toggle.console", "Console must specify a &aplayer&f.");

        messages.addDefault("messages.toggle.description", "Toggle nightvision mode.");

        messages.addDefault("messages.list.header", "&a&m+&8&m                              &a&l Night&fvision &8&m                              &a&m+");
        messages.addDefault("messages.list.format", "&8➝ &fPlayers:&a {players}");
        messages.addDefault("messages.list.delimiter", "&7,&a ");
        messages.addDefault("messages.list.footer", "&a&m+&8&m                                                                            &a&m+");
        messages.addDefault("messages.list.empty", "There are no players using &anightvision&f.");
        messages.addDefault("messages.list.description", "List players with nightvision active.");

        messages.addDefault("messages.reload.success", "Nightvision has been &asuccessfully&f reloaded.");
        messages.addDefault("messages.reload.failed", "Nightvision &cfailed&f to reload. Check console for details.");
        messages.addDefault("messages.reload.description", "Reload the plugin configuration.");

        messages.addDefault("messages.help.header", "&a&m+&8&m                              &a&l Night&fvision &8&m                              &a&m+");
        messages.addDefault("messages.help.format", "&8➝ &a/{command} &7{parameters} &f- {description}");
        messages.addDefault("messages.help.footer", "&a&m+&8&m                                                                            &a&m+");
        messages.addDefault("messages.help.description", "View the plugin help.");

        messages.options().copyDefaults(true);

        try {
            messages.save(messagesFile);
        } catch (Exception e) {
            //
        }
    }

    /**
     * Retrieve the message configuration.
     *
     * @return FileConfiguration
     */
    public FileConfiguration getMessages() {
        return messages;
    }
}
