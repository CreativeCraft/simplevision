package org.creativecraft.simplevision.integrations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.creativecraft.simplevision.SimpleVisionPlugin;
import org.bukkit.entity.Player;

public class PlaceholderApi extends PlaceholderExpansion {
    private SimpleVisionPlugin plugin;
    private String identifier = "nightvision";

    public PlaceholderApi(SimpleVisionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if (s.equalsIgnoreCase("enabled")) {
            return plugin.getNightvision().hasPlayer(player) ?
                plugin.localize("messages.generic.enabled") :
                plugin.localize("messages.generic.disabled");
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
