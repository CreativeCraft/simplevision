package org.creativecraft.nightvision;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Nightvision {
    private final NightvisionPlugin plugin;

    public Nightvision(NightvisionPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Enable the specified player's nightvision.
     *
     * @param player The player.
     */
    public void enable(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1L, 1L);
        player.addPotionEffect(
            new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                Integer.MAX_VALUE,
                0,
                true,
                false,
                false
            )
        );

        plugin.getUserDataConfig().getUserData().set("players." + player.getUniqueId(), true);
        plugin.getUserDataConfig().saveUserData();
    }

/**
 * Disable the specified player's nightvision.
 *
 * @param player The player.
 */
public void disable(Player player) {
    player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, 1L, 1L);
    player.removePotionEffect(PotionEffectType.NIGHT_VISION);

    plugin.getUserDataConfig().getUserData().set("players." + player.getUniqueId(), null);
    plugin.getUserDataConfig().saveUserData();
}

    /**
     * Determine if the player has nightvision active.
     *
     * @param player The player.
     */
    public boolean isEnabled(Player player) {
        return plugin.getUserDataConfig().getUserData().getBoolean("players." + player.getUniqueId());
    }
}
