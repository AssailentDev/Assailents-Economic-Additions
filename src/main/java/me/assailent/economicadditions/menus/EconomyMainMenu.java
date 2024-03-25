package me.assailent.economicadditions.menus;

import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.utilities.GetKeys;
import me.assailent.economicadditions.utilities.Parsing;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import me.assailent.economicadditions.utilities.ItemStacks;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class EconomyMainMenu {

    private static ConfigurationSection mainMenu = EconomicAdditions.getPlugin().getLangConfig().getConfigurationSection("gui").getConfigurationSection("economy").getConfigurationSection("mainMenu");
    private static ConfigurationSection payMenu = EconomicAdditions.getPlugin().getLangConfig().getConfigurationSection("gui").getConfigurationSection("economy").getConfigurationSection("payMenu");
    private static ConfigurationSection balMenu = EconomicAdditions.getPlugin().getLangConfig().getConfigurationSection("gui").getConfigurationSection("economy").getConfigurationSection("balMenu");
    private static ConfigurationSection lang = EconomicAdditions.getPlugin().getLangConfig();
    private static Parsing parsing = new Parsing();
    private static ConfigurationSection config = EconomicAdditions.getPlugin().getConfig();

    public static void onPayClick(Player player) {
        player.openInventory(openPayInv(player, 0));
    }

    public static void onBalClick(Player player) {
        player.openInventory(openBalInv(player, 0));
    }

    public static Inventory openInv(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, mainMenu.getString("Title"));
        ItemStack pay;
        ItemStack bal;
        ItemStack stats;
        ItemStack empty = ItemStacks.createItemStack(mainMenu.getConfigurationSection("emptyItem"), "economicadditions.item.empty", "true", null);

        GetKeys.addKey("economicadditions.economy.gui.main.pay");
        GetKeys.addKey("economicadditions.economy.gui.main.stats");
        GetKeys.addKey("economicadditions.economy.gui.main.bal");
        GetKeys.addKey("economicadditions.item.empty");

        if (mainMenu.getConfigurationSection("payItem") != null) {
            pay = ItemStacks.createItemStack(mainMenu.getConfigurationSection("payItem"), "economicadditions.economy.gui.main.pay", "true", null);
        } else {
            pay = empty;
        }
        if (mainMenu.getConfigurationSection("balItem") != null) {
            bal = ItemStacks.createItemStack(mainMenu.getConfigurationSection("balItem"), "economicadditions.economy.gui.main.bal", "true", null);
        } else {
            bal = empty;
        }
        if (mainMenu.getConfigurationSection("statsItem") != null) {
            stats = ItemStacks.createItemStack(mainMenu.getConfigurationSection("statsItem"), "economicadditions.economy.gui.main.stats", "true", player);
        } else {
            stats = empty;
        }

        inventory.setItem(11, bal);
        inventory.setItem(13, stats);
        inventory.setItem(15, pay);

        for (int i = 0; i < 27; i++) {
            if (i == 11 || i == 13 || i == 15)
                continue;
            inventory.setItem(i, empty);
        }

        return inventory;
    }

    public static void onPayNextClick(Player player, int page) {
        player.openInventory(openPayInv(player, page + 1));
    }

    public static void onPayPrevClick(Player player, int page) {
        player.openInventory(openPayInv(player, page - 1));
    }

    public static void onPayHeadClick(Player player, String uuidString) {
        player.closeInventory();
        player.sendMessage(parsing.parse(parsing.prefix + parsing.successColor +  parsing.payMenu.getString("pay-message"), null, null));
        player.setMetadata("economicadditions.economy.gui.pay.command", new FixedMetadataValue(EconomicAdditions.getPlugin(), uuidString));
    }

    public static void onBalNextClick(Player player, int page) {
        player.openInventory(openBalInv(player, page + 1));
    }

    public static void onBalPrevClick(Player player, int page) {
        player.openInventory(openBalInv(player, page - 1));
    }

    public static Inventory openBalInv(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 4, balMenu.getString("Title"));
        ItemStack next;
        ItemStack prev;
        int pages = ((int) (Math.ceil(Bukkit.getOnlinePlayers().size() - 1 / 27)) / 2) - 1;

        GetKeys.addKey("economicadditions.economy.gui.bal.next");
        GetKeys.addKey("economicadditions.economy.gui.bal.prev");
        GetKeys.addKey("economicadditions.item.empty");

        if (page < pages) {
            next = ItemStacks.createItemStack(balMenu.getConfigurationSection("nextFullItem"), "economicadditions.economy.gui.bal.next", String.valueOf(page), null);
        } else {
            next = ItemStacks.createItemStack(balMenu.getConfigurationSection("nextEmptyItem"), "economicadditions.item.empty", String.valueOf(page), null);
        }

        if (page > 0) {
            prev = ItemStacks.createItemStack(balMenu.getConfigurationSection("prevFullItem"), "economicadditions.economy.gui.bal.prev", String.valueOf(page), null);
        } else {
            prev = ItemStacks.createItemStack(balMenu.getConfigurationSection("prevEmptyItem"), "economicadditions.item.empty", String.valueOf(page), null);
        }

        int i = 0;
        int starts = pages * 27;
        int ends = (page * 27) + 27;
        int slotsUsed = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (i > ends) {
                break;
            } else if (i < starts) {
                continue;
            }
            if (p == player) {
                continue;
            }
            inventory.setItem(slotsUsed, ItemStacks.createItemStack(balMenu.getConfigurationSection("playerHead"), "economicadditions.item.empty", "true", p));
            slotsUsed++;
        }
        for (int iterator = slotsUsed; iterator < (9 * 4); iterator++) {
            if (iterator == 28 || iterator == 34) {
                continue;
            }
            inventory.setItem(iterator, ItemStacks.createItemStack(balMenu.getConfigurationSection("emptyItem"), "economicadditions.item.empty",  "true", null));
        }

        inventory.setItem(28, prev);
        inventory.setItem(34, next);

        return inventory;
    }

    public static Inventory openPayInv(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(player, 9 * 4, payMenu.getString("Title"));
        ItemStack next;
        ItemStack prev;
        int pages = ((int) (Math.ceil(Bukkit.getOnlinePlayers().size() - 1 / 27)) / 2) - 1;

        GetKeys.addKey("economicadditions.economy.gui.pay.next");
        GetKeys.addKey("economicadditions.economy.gui.pay.prev");
        GetKeys.addKey("economicadditions.economy.gui.pay.head");
        GetKeys.addKey("economicadditions.item.empty");

        if (page < pages) {
            next = ItemStacks.createItemStack(payMenu.getConfigurationSection("nextFullItem"), "economicadditions.economy.gui.pay.next", String.valueOf(page), null);
        } else {
            next = ItemStacks.createItemStack(payMenu.getConfigurationSection("nextEmptyItem"), "economicadditions.item.empty", "true", null);
        }

        if (page > 0) {
            prev = ItemStacks.createItemStack(payMenu.getConfigurationSection("prevFullItem"), "economicadditions.economy.gui.pay.prev", String.valueOf(page), null);
        } else {
            prev = ItemStacks.createItemStack(payMenu.getConfigurationSection("prevEmptyItem"), "economicadditions.item.empty", "true", null);
        }

        int i = 0;
        int starts = pages * 27;
        int ends = (pages * 27) + 27;
        int slotsUsed = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (i > ends) {
                break;
            } else if (i < starts) {
                continue;
            }
            if (p == player) {
                continue;
            }
            inventory.setItem(slotsUsed, ItemStacks.createItemStack(payMenu.getConfigurationSection("playerHead"), "economicadditions.economy.gui.pay.head", p.getUniqueId().toString(), p));
            slotsUsed++;
        }
        for (int iterator = slotsUsed; iterator < (9 * 4); iterator++) {
            if (iterator == 28 || iterator == 34) {
                continue;
            }
            inventory.setItem(iterator, ItemStacks.createItemStack(payMenu.getConfigurationSection("emptyItem"), "economicadditions.item.empty",  "true", null));
        }

        inventory.setItem(28, prev);
        inventory.setItem(34, next);

        return inventory;
    }

}
