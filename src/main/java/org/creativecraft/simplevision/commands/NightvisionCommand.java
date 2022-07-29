package org.creativecraft.simplevision.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.HelpEntry;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import org.creativecraft.simplevision.SimpleVisionPlugin;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@CommandAlias("%simplevision")
@Description("Toggle nightvision to see in the dark.")
public class NightvisionCommand extends BaseCommand {
    private final SimpleVisionPlugin plugin;

    /**
     * Initialize the Nightvision command.
     *
     * @param plugin The plugin.
     */
    public NightvisionCommand(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Toggle nightvision mode for you or the specified player.
     *
     * @param sender The command sender.
     * @param target The targeted player.
     */
    @Default
    @Subcommand("toggle")
    @CommandPermission("simplevision.use")
    @Description("Toggle nightvision mode.")
    @Syntax("[player]")
    @CommandCompletion("@players")
    public void onToggle(CommandSender sender, @Optional String target) {
        if (target != null) {
            target = target.replaceAll("[^A-Za-z0-9_.]", "");
        }

        if (!(sender instanceof Player player)) {
            if (target == null) {
                plugin.sendMessage(sender, plugin.localize("messages.toggle.console"));
                return;
            }

            handleToggleTarget(sender, target);
            return;
        }

        if (target != null) {
            if (!player.hasPermission("simplevision.use.other")) {
                plugin.sendMessage(sender, plugin.localize("messages.toggle.no-permission"));
                return;
            }

            handleToggleTarget(sender, target);
            return;
        }

        if (!plugin.getNightvision().hasPlayer(player)) {
            plugin.getNightvision().enable(player);
            plugin.sendMessage(
                player,
                plugin.localize("messages.toggle.enable")
            );

            return;
        }

        plugin.getNightvision().disable(player);
        plugin.sendMessage(
            player,
            plugin.localize("messages.toggle.disable")
        );
    }

    /**
     * Handle the specified target's nightvision status.
     *
     * @param sender The command sender.
     * @param target The targeted player.
     */
    private void handleToggleTarget(CommandSender sender, String target) {
        Player player = plugin.getServer().getPlayer(target);

        if (player == null) {
            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.toggle.not-found")
                    .replace("{player}", target)
            );

            return;
        }

        if (!plugin.getNightvision().hasPlayer(player)) {
            plugin.getNightvision().enable(player);

            plugin.sendMessage(
                player,
                plugin.localize("messages.toggle.enable")
            );

            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.toggle.enable-other")
                    .replace("{player}", player.getName())
            );

            return;
        }

        plugin.getNightvision().disable(player);

        plugin.sendMessage(
            player,
            plugin.localize("messages.toggle.disable")
        );

        plugin.sendMessage(
            sender,
            plugin
                .localize("messages.toggle.disable-other")
                .replace("{player}", player.getName())
        );
    }

    /**
     * List players with nightvision active.
     *
     * @param sender The command sender.
     */
    @Subcommand("list")
    @CommandPermission("simplevision.list")
    @Description("List players with nightvision active.")
    public void onList(CommandSender sender) {
        ArrayList<String> players = new ArrayList<>();
        ArrayList<Player> playerList = plugin.getNightvision().getPlayers();

        if (playerList.isEmpty()) {
            plugin.sendMessage(sender, plugin.localize("messages.list.empty"));
            return;
        }

        plugin.sendRawMessage(sender, plugin.localize("messages.list.header"));

        playerList.forEach(player -> {
            if (player == null) {
                return;
            }

            players.add(player.getName());
        });

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
     * Retrieve the plugin help.
     *
     * @param sender The command sender.
     */
    @HelpCommand
    @Syntax("[page]")
    @Description("View the plugin help.")
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
     * Reload the plugin confirmation.
     *
     * @param sender The command sender.
     */
    @Subcommand("reload")
    @CommandPermission("simplevision.admin")
    @Description("Reload the plugin configuration.")
    public void onReload(CommandSender sender) {
        try {
            plugin.reload();
            plugin.sendMessage(sender, plugin.localize("messages.reload.success"));
        } catch (Exception e) {
            plugin.sendMessage(sender, plugin.localize("messages.reload.failed"));
        }
    }
}
