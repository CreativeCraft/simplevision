package org.creativecraft.nightvision.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.HelpEntry;
import co.aikar.commands.annotation.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.creativecraft.nightvision.Nightvision;
import org.creativecraft.nightvision.NightvisionPlugin;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.UUID;

@CommandAlias("%nightvision")
public class NightvisionCommand extends BaseCommand {
    private final NightvisionPlugin plugin;
    private final Nightvision nightvision;

    public NightvisionCommand(NightvisionPlugin plugin) {
        this.plugin = plugin;
        this.nightvision = new Nightvision(plugin);
    }

    /**
     * Retrieve the plugin help.
     *
     * @param sender The command sender.
     */
    @HelpCommand
    @Syntax("[page]")
    @Description("View the nightvision help.")
    public void onHelp(CommandSender sender, CommandHelp help) {
        plugin.sendRawMessage(sender, plugin.localize("messages.help.header"));

        for (HelpEntry entry : help.getHelpEntries()) {
            plugin.sendRawMessage(
                sender,
                plugin.localize("messages.help.format")
                    .replace("{command}", entry.getCommand())
                    .replace("{parameters}", entry.getParameterSyntax())
                    .replace("{description}", plugin.localize("messages." + entry.getCommand().split("\\s+")[1] + ".description"))
            );
        }

        plugin.sendRawMessage(sender, plugin.localize("messages.help.footer"));
    }

    /**
     * Toggle nightvision mode.
     *
     * @param sender The player.
     */
    @Default
    @Subcommand("toggle")
    @CommandPermission("nightvision.use")
    @Description("Toggle nightvision mode.")
    public void onToggle(Player sender) {
        boolean status = plugin.getUserDataConfig().getUserData().getBoolean("players." + sender.getUniqueId());

        if (!status) {
            nightvision.enable(sender);
            plugin.sendMessage(sender, plugin.localize("messages.toggle.on"));

            return;
        }

        nightvision.disable(sender);
        plugin.sendMessage(sender, plugin.localize("messages.toggle.off"));
    }

    /**
     * List players with nightvision active.
     *
     * @param sender The command sender.
     */
    @Subcommand("list")
    @CommandPermission("nightvision.admin")
    @Description("List players with nightvision active.")
    public void onList(CommandSender sender) {
        ConfigurationSection keys = plugin.getUserDataConfig().getUserData().getConfigurationSection("players");
        HashSet<String> players = new HashSet<String>();

        plugin.sendRawMessage(sender, plugin.localize("messages.list.header"));

        if (keys != null) {
            keys.getKeys(false).forEach(key -> {
                Player player = plugin.getServer().getPlayer(UUID.fromString(key));

                if (player == null) {
                    return;
                }

                players.add(player.getName());
            });
        }

        plugin.sendRawMessage(
            sender,
            plugin.localize("messages.list.format").replace(
                "{players}",
                players.isEmpty() ? "None" : String.join(plugin.localize("messages.list.delimiter"), players)
            )
        );

        plugin.sendRawMessage(sender, plugin.localize("messages.list.footer"));
    }

    /**
     * Reload the plugin confirmation.
     *
     * @param sender The command sender.
     */
    @Subcommand("reload")
    @CommandPermission("nightvision.admin")
    @Description("Reload the nightvision plugin configuration.")
    public void onReload(CommandSender sender) {
        try {
            plugin.reload();
            plugin.sendMessage(sender, plugin.localize("messages.reload.success"));
        } catch (Exception e) {
            plugin.sendMessage(sender, plugin.localize("messages.reload.failed"));
        }
    }
}
