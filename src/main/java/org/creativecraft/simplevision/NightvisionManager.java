package org.creativecraft.simplevision;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class NightvisionManager {
    private final SimpleVisionPlugin plugin;
    private final ArrayList<Player> players = new ArrayList<>();

    public NightvisionManager(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Enable the specified player's nightvision.
     *
     * @param player The player.
     */
    public void enable(Player player) {
        addEffect(player);
        addPlayer(player);

        if (!plugin.getUserData().hasPlayer(player)) {
            plugin.getUserData().setPlayer(player, true);
        }
    }

    /**
     * Disable the specified player's nightvision.
     *
     * @param player The player.
     */
    public void disable(Player player) {
        removeEffect(player);
        removePlayer(player);

        if (plugin.getUserData().hasPlayer(player)) {
            plugin.getUserData().setPlayer(player, null);
        }
    }

    /**
     * Retrieve the current players with nightvision.
     *
     * @return ArrayList
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Add the specified player to the nightvision list.
     *
     * @param player The player.
     */
    public void addPlayer(Player player) {
        if (players.contains(player)) {
            return;
        }

        players.add(player);
    }

    /**
     * Remove the specified player from the nightvision list.
     *
     * @param player The player.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Determine if the player has nightvision active.
     *
     * @param player The player.
     */
    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    /**
     * Remove all players from the nightvision list.
     */
    public void clearPlayers() {
        players.clear();
    }

    /**
     * Add the nightvision potion effects to the specified player.
     *
     * @param player The player.
     */
    public void addEffect(Player player) {
        if (plugin.getConfig().getBoolean("sounds.enabled")) {
            player.playSound(
                player.getLocation(),
                Sound.valueOf(plugin.getConfig().getString("sounds.toggle-on")),
                1L,
                1L
            );
        }

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
    }

    /**
     * Remove the nightvision potion effects from the specified player.
     *
     * @param player The player.
     */
    public void removeEffect(Player player) {
        if (plugin.getConfig().getBoolean("sounds.enabled")) {
            player.playSound(
                player.getLocation(),
                Sound.valueOf(plugin.getConfig().getString("sounds.toggle-off")),
                1L,
                1L
            );
        }

        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
