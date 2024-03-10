package me.assailent.economicadditions.events;

import io.papermc.paper.event.player.AbstractChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.menus.EconomyMainMenu;
import me.assailent.economicadditions.utilities.GetKeys;
import me.assailent.economicadditions.utilities.NBTApi;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class GuiListener implements Listener {

    private EconomicAdditions plugin = EconomicAdditions.getPlugin();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        Inventory inventory = event.getClickedInventory();
        int slot = event.getSlot();
        ItemStack item = inventory.getItem(slot);
        if (item == null)
            return;
        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();

        for (int i = 0; i < GetKeys.getKeys().size(); i++) {
            if (NBTApi.hasNBT(item, GetKeys.getKeys().get(i))) {
                event.setCancelled(true);
                GetKeys.getKey(GetKeys.getKeys().get(i), player, NBTApi.getNBT(item, GetKeys.getKeys().get(i)));
            }
        }

    }

    @EventHandler
    public void onChatMessage(ChatEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (event.getPlayer().hasMetadata("economicadditions.economy.gui.pay.command")) {
            Integer payAmount;
            try {
                payAmount = Integer.valueOf(MiniMessage.miniMessage().serialize(event.originalMessage()));
                if (payAmount <= 0) {
                    event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>Cancelling Payment!"));
                    event.setCancelled(true);
                    event.getPlayer().removeMetadata("economicadditions.economy.gui.pay.command", plugin);
                }
                Player target = Bukkit.getPlayer(UUID.fromString(event.getPlayer().getMetadata("economicadditions.economy.gui.pay.command").get(0).asString()));
                event.getPlayer().performCommand("pay " + target.getName() + " " + payAmount);
                event.setCancelled(true);
                EconomyMainMenu.openInv(event.getPlayer());
                event.getPlayer().removeMetadata("economicadditions.economy.gui.pay.command", plugin);
            } catch (NumberFormatException e) {
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>Cancelling Payment!"));
                event.setCancelled(true);
                event.getPlayer().removeMetadata("economicadditions.economy.gui.pay.command", plugin);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

    }

}
