package me.assailent.economicadditions.displays;

import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.utilities.Parsing;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionBar {
    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static Parsing parsing = new Parsing();
    private int delay = parsing.actionbar.getInt("update");

    public void ActionBarHandler() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.hasMetadata("economicadditions.actionbar.toggled") & player.getMetadata("economicadditions.actionbar.toggled").get(0).asBoolean() || !player.hasMetadata("economicadditions.actionbar.toggled")) {
                        String actionBarText = parsing.actionbar.getString("text");
                        actionBarText = PlaceholderAPI.setPlaceholders(player, actionBarText);
                        player.sendActionBar(parsing.parse(actionBarText, null, null));
                    }
                }
            }
        }, 0, delay * 20L);
    }
}
