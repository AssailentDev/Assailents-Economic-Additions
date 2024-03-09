package me.assailent.economicadditions.utilities;

import me.assailent.economicadditions.menus.EconomyMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GetKeys {

    private static ArrayList<String> keys = new ArrayList<String>();
    public static void addKey(String key) {
        if (keys.contains(key)) {
            return;
        }
        keys.add(key);
    }

    public static ArrayList<String> getKeys() {
        return keys;
    }

    public static void getKey(String key, Player player, @Nullable String value) {
        switch(key) {
            case "economicadditions.economy.gui.main.pay":
                EconomyMainMenu.onPayClick(player);
                break;
            case "economicadditions.economy.gui.main.bal":
                EconomyMainMenu.onBalClick(player);
                break;
            case "economicadditions.economy.gui.pay.next":
                EconomyMainMenu.onPayNextClick(player, Integer.valueOf(value));
                break;
            case "economicadditions.economy.gui.pay.prev":
                EconomyMainMenu.onPayPrevClick(player, Integer.valueOf(value));
                break;
            case "economicadditions.economy.gui.pay.head":
                EconomyMainMenu.onPayHeadClick(player, value);
                break;
            case "economicadditions.economy.gui.bal.next":
                EconomyMainMenu.onBalNextClick(player, Integer.valueOf(value));
                break;
            case "economicadditions.economy.gui.bal.prev":
                EconomyMainMenu.onBalPrevClick(player, Integer.valueOf(value));
                break;
            case "economicadditions.item.empty":
                break;
        }
    }
}
