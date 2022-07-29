package org.creativecraft.simplevision.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import org.creativecraft.simplevision.SimpleVisionPlugin;

public class EventListener implements Listener {
    private final SimpleVisionPlugin plugin;

    public EventListener(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listen for the player join event.
     *
     * @param event The player join event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (
            !plugin.getConfig().getBoolean("events.login.enabled") ||
            !plugin.getUserData().hasPlayer(player)

        ) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getNightvision().enable(player);

            if (plugin.getConfig().getBoolean("events.login.message")) {
                plugin.sendMessage(player, plugin.localize("messages.events.login"));
            }
        }, Math.max(1L, plugin.getConfig().getLong("events.login.delay", 20L)));
    }

    /**
     * Listen for the player quit event.
     *
     * @param event The player quit event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getNightvision().hasPlayer(player)) {
            return;
        }

        if (!plugin.getConfig().getBoolean("events.login.enabled")) {
            plugin.getNightvision().disable(event.getPlayer());
        }

        plugin.getNightvision().removePlayer(player);
    }

    /**
     * Listen for the player respawn event.
     *
     * @param event The player respawn event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getNightvision().hasPlayer(player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getConfig().getBoolean("events.death.enabled")) {
                plugin.getNightvision().addEffect(player);

                if (plugin.getConfig().getBoolean("events.death.message")) {
                    plugin.sendMessage(player, plugin.localize("messages.events.death"));
                }

                return;
            }

            plugin.getNightvision().disable(player);
        }, Math.max(1L, plugin.getConfig().getLong("events.death.delay", 1L)));
    }

    /**
     * Listen for the player item consume event.
     *
     * @param event The player item consume event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketUse(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        if (
            !plugin.getNightvision().hasPlayer(player) ||
            !event.getItem().getType().equals(Material.MILK_BUCKET) ||
            !player.hasPotionEffect(PotionEffectType.NIGHT_VISION)
        ) {
            return;
        }

        if (plugin.getConfig().getBoolean("events.milk-bucket.enabled")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getNightvision().addEffect(player);

                if (plugin.getConfig().getBoolean("events.milk-bucket.message")) {
                    plugin.sendMessage(player, plugin.localize("messages.events.milk-bucket"));
                }
            }, Math.max(1L, plugin.getConfig().getLong("events.milk-bucket.delay", 1L)));

            return;
        }

        plugin.getNightvision().disable(player);
    }
}
