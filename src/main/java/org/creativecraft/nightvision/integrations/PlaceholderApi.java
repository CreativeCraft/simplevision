package org.creativecraft.nightvision.integrations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.creativecraft.nightvision.Nightvision;
import org.creativecraft.nightvision.NightvisionPlugin;
import org.bukkit.entity.Player;

public class PlaceholderApi extends PlaceholderExpansion {
    private final Nightvision nightvision;
    private NightvisionPlugin plugin;
    private String identifier = "nightvision";

    public PlaceholderApi(NightvisionPlugin plugin) {
        this.plugin = plugin;
        this.nightvision = new Nightvision(plugin);
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if (s.equalsIgnoreCase("active")) {
            return nightvision.isEnabled(player) ? "Yes" : "No";
        }

        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
