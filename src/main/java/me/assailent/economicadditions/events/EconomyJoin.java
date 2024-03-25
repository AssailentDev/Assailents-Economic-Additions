package me.assailent.economicadditions.events;

import me.assailent.economicadditions.EconomicAdditions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.sql.SQLException;

public class EconomyJoin implements Listener {
    private EconomicAdditions plugin = EconomicAdditions.getPlugin();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore() || !event.getPlayer().hasMetadata("economicadditions.actionbar.toggled")) {
            event.getPlayer().setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, true));
        }
        event.getPlayer().setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, true));

        try {
            if (!(plugin.getEconomyDatabase().playerExists(event.getPlayer()))) {
                plugin.getEconomyDatabase().addPlayer(event.getPlayer());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
