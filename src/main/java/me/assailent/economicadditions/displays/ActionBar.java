package me.assailent.economicadditions.displays;

import me.assailent.economicadditions.EconomicAdditions;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionBar {
    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private ConfigurationSection lang = plugin.getLangConfig().getConfigurationSection("actionbar");
    private int delay = lang.getInt("update");

    public void ActionBarHandler() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.hasMetadata("economicadditions.actionbar.toggled") & player.getMetadata("economicadditions.actionbar.toggled").get(0).asBoolean() || !player.hasMetadata("economicadditions.actionbar.toggled")) {
                        String actionBarText = lang.getString("text");
                        actionBarText = PlaceholderAPI.setPlaceholders(player, actionBarText);
                        player.sendActionBar(MiniMessage.miniMessage().deserialize(actionBarText));
                    }
                }
            }
        }, 0, delay * 20L);
    }
}
