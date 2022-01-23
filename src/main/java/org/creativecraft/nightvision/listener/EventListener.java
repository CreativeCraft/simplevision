package org.creativecraft.nightvision.listener;

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
import org.creativecraft.nightvision.Nightvision;
import org.creativecraft.nightvision.NightvisionPlugin;

public class EventListener implements Listener {
    private final NightvisionPlugin plugin;
    private final Nightvision nightvision;

    public EventListener(NightvisionPlugin plugin) {
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

        if (plugin.getUserDataConfig().getUserData().getBoolean("players." + player.getUniqueId().toString())) {
            nightvision.enable(player);
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
            if (plugin.getUserDataConfig().getUserData().getBoolean("players." + player.getUniqueId().toString())) {
                nightvision.enable(player);
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

        if (
            !event.getItem().getType().equals(Material.MILK_BUCKET) ||
            !player.hasPotionEffect(PotionEffectType.NIGHT_VISION) ||
            !nightvision.isEnabled(player)
        ) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            nightvision.enable(player);
        }, 1);
    }
}
