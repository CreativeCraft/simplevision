package org.creativecraft.simplevision;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.MessageType;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.ChatMessageType;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.creativecraft.simplevision.commands.NightvisionCommand;
import org.creativecraft.simplevision.config.MessagesConfig;
import org.creativecraft.simplevision.config.UserDataConfig;
import org.creativecraft.simplevision.integrations.PlaceholderApi;
import org.creativecraft.simplevision.listener.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleVisionPlugin extends JavaPlugin {
    public static SimpleVisionPlugin plugin;
    private MessagesConfig messagesConfig;
    private UserDataConfig userDataConfig;
    private NightvisionManager nightvisionManager;

    /**
     * Enable the plugin.
     */
    @Override
    public void onEnable() {
        plugin = this;
        nightvisionManager = new NightvisionManager(plugin);

        registerConfig();
        registerMessagesConfig();
        registerUserDataConfig();
        registerListeners();
        registerSchedulers();
        registerCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderApi(this).register();
        }

        new MetricsLite(this, 14147);
    }

    /**
     * Load the plugin.
     */
    @Override
    public void onLoad() {
        //
    }

    /**
     * Disable the plugin.
     */
    @Override
    public void onDisable() {
        for (Player player : getNightvision().getPlayers()) {
            getNightvision().removeEffect(player);
        }

        getNightvision().clearPlayers();

        Bukkit.getScheduler().cancelTasks(plugin);
    }

    /**
     * Register the plugin commands.
     */
    public void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        CommandReplacements replacements = commandManager.getCommandReplacements();

        replacements.addReplacement("simplevision", getConfig().getString("command", "nightvision|nv"));

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
    public void registerConfig() {
        getConfig().addDefault("command", "nightvision|nv");

        getConfig().addDefault("action-bar.enabled", true);

        getConfig().addDefault("sounds.enabled", true);
        getConfig().addDefault("sounds.toggle-on", "ITEM_BOTTLE_FILL");
        getConfig().addDefault("sounds.toggle-off", "ITEM_BOTTLE_EMPTY");

        getConfig().addDefault("events.login.enabled", true);
        getConfig().addDefault("events.login.message", true);
        getConfig().addDefault("events.login.delay", 20);

        getConfig().addDefault("events.death.enabled", true);
        getConfig().addDefault("events.death.message", true);
        getConfig().addDefault("events.death.delay", 1);

        getConfig().addDefault("events.milk-bucket.enabled", true);
        getConfig().addDefault("events.milk-bucket.message", true);
        getConfig().addDefault("events.milk-bucket.delay", 1);

        getConfig().options().copyDefaults(true);

        saveConfig();
    }

    /**
     * Register the message config.
     */
    public void registerMessagesConfig() {
        messagesConfig = new MessagesConfig(this);
    }

    /**
     * Register the userdata config.
     */
    public void registerUserDataConfig() {
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
        if (getConfig().getBoolean("action-bar.enabled", true)) {
            Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (Player player : getNightvision().getPlayers()) {
                    plugin.sendActionMessage(player, plugin.localize("messages.toggle.action-bar"));
                }
            }, 20L, 1L);
        }
    }

    /**
     * Retrieve the userdata configuration.
     *
     * @return MessagesConfig
     */
    public FileConfiguration getUserDataConfig() {
        return userDataConfig.getUserData();
    }

    /**
     * Retrieve the nightvision instance.
     *
     * @return NightvisionManager
     */
    public NightvisionManager getNightvision() {
        return nightvisionManager;
    }

    /**
     * Retrieve the userdata instance.
     *
     * @return UserData
     */
    public UserDataConfig getUserData() {
        return userDataConfig;
    }

    /**
     * Retrieve a localized message.
     *
     * @param  key The locale key.
     * @return String
     */
    public String localize(String key) {
        String message = messagesConfig.getMessages().getString(key);

        return ChatColor.translateAlternateColorCodes(
            '&',
            message == null ? key + " not found." : message
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
        registerMessagesConfig();
        registerUserDataConfig();
        reloadConfig();
    }
}
