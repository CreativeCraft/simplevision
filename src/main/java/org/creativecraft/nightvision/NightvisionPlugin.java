package org.creativecraft.nightvision;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.MessageType;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.creativecraft.nightvision.commands.NightvisionCommand;
import org.creativecraft.nightvision.config.MessagesConfig;
import org.creativecraft.nightvision.config.UserDataConfig;
import org.creativecraft.nightvision.integrations.PlaceholderApi;
import org.creativecraft.nightvision.listener.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NightvisionPlugin extends JavaPlugin {
    public static NightvisionPlugin plugin;
    private MessagesConfig messagesConfig;
    private UserDataConfig userDataConfig;

    @Override
    public void onEnable() {
        plugin = this;

        registerConfigs();
        registerListeners();
        registerSchedulers();
        registerCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderApi(this).register();
        }

        // new MetricsLite(this, 13987);
    }

    @Override
    public void onLoad() {
        //
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(plugin);
        userDataConfig.saveUserData();
    }

    /**
     * Register the plugin commands.
     */
    public void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        CommandReplacements replacements = commandManager.getCommandReplacements();

        replacements.addReplacement("nightvision", getConfig().get("command", "nightvision"));
        replacements.addReplacements(getConfig().getStringList("commands").toArray());

        commandManager.setFormat(MessageType.ERROR, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.SYNTAX, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.HELP, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.INFO, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);

        commandManager.registerCommand(new NightvisionCommand(this));
        commandManager.enableUnstableAPI("help");
    }

    /**
     * Register the plugin configuration.
     */
    public void registerConfigs() {
        getConfig().addDefaults("")
        getConfig().addDefault("command", "nightvision");
        getConfig().options().copyDefaults(true);
        saveConfig();

        messagesConfig = new MessagesConfig(this);
        userDataConfig = new UserDataConfig(this);
    }

    /**
     * Register the plugin event listeners.
     */
    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
    }

    /**
     * Register the plugin schedulers.
     */
    public void registerSchedulers() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (userDataConfig.getUserData().getBoolean("players." + player.getUniqueId())) {
                    plugin.sendActionMessage(player, plugin.localize("messages.toggle.action-bar"));
                }
            }
        }, 20L, 1L);
    }

    /**
     * Retrieve the messages configuration.
     *
     * @return MessagesConfig
     */
    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    /**
     * Retrieve the userdata configuration.
     *
     * @return MessagesConfig
     */
    public UserDataConfig getUserDataConfig() {
        return userDataConfig;
    }


    /**
     * Retrieve a localized message.
     *
     * @param  key The locale key.
     * @return String
     */
    public String localize(String key) {
        return ChatColor.translateAlternateColorCodes(
            '&',
            messagesConfig.getMessages().getString(key, key + " is missing.")
        );
    }

    /**
     * Send a message formatted with MineDown.
     *
     * @param sender The command sender.
     * @param value  The message.
     */
    public void sendMessage(CommandSender sender, String value) {
        sender.spigot().sendMessage(
            MineDown.parse(messagesConfig.getMessages().getString("messages.generic.prefix") + value)
        );
    }

    /**
     * Send a raw message formatted with MineDown.
     *
     * @param sender The command sender.
     * @param value  The message.
     */
    public void sendRawMessage(CommandSender sender, String value) {
        sender.spigot().sendMessage(
            MineDown.parse(value)
        );
    }

    /**
     * Send an action message formatted with MineDown.
     *
     * @param sender The command sender.
     * @param value  The message.
     */
    public void sendActionMessage(Player sender, String value) {
        sender.spigot().sendMessage(
            ChatMessageType.ACTION_BAR,
            MineDown.parse(value)
        );
    }

    /**
     * Reload the plugin configuration.
     */
    public void reload() {
        registerConfigs();
        reloadConfig();
    }
}
