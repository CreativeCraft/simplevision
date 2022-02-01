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
import org.creativecraft.simplevision.Nightvision;
import org.creativecraft.simplevision.SimpleVisionPlugin;

public class EventListener implements Listener {
    private final SimpleVisionPlugin plugin;
    private final Nightvision nightvision;

    public EventListener(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
        this.nightvision = new Nightvision(plugin);
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
            plugin.getUserDataConfig().getBoolean("players." + player.getUniqueId().toString()) &&
            plugin.getConfig().getBoolean("nightvision.persist.login")
        ) {
            nightvision.enable(player);
        }
    }

    /**
     * Listen for the player quit event.
     *
     * @param event The player quit event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getConfig().getBoolean("nightvision.persist.login")) {
            nightvision.disable(event.getPlayer());
        }
    }

    /**
     * Listen for the player respawn event.
     *
     * @param event The player respawn event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getUserDataConfig().getBoolean("players." + player.getUniqueId().toString())) {
                if (plugin.getConfig().getBoolean("nightvision.persist.death")) {
                    nightvision.enable(player);
                    return;
                }

                nightvision.disable(player);
            }
        }, 20);
    }

    /**
     * Listen for the player item consume event.
     *
     * @param event The player item consume event.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketUse(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        plugin.getLogger().info(plugin.getConfig().getString("nightvision.persist.milkbucket"));

        if (
            !event.getItem().getType().equals(Material.MILK_BUCKET) ||
            !player.hasPotionEffect(PotionEffectType.NIGHT_VISION) ||
            !nightvision.isEnabled(player)
        ) {
            return;
        }

        if (plugin.getConfig().getBoolean("nightvision.persist.milkbucket")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                nightvision.enable(player);
            }, 1);

            return;
        }

        nightvision.disable(player);
    }
}
