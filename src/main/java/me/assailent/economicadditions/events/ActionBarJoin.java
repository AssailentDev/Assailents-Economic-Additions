package me.assailent.economicadditions.events;

import me.assailent.economicadditions.EconomicAdditions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ActionBarJoin implements Listener {
    private EconomicAdditions plugin = EconomicAdditions.getPlugin();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore() || !event.getPlayer().hasMetadata("economicadditions.actionbar.toggled")) {
            event.getPlayer().setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, true));
        }
        event.getPlayer().setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, true));
    }
}
