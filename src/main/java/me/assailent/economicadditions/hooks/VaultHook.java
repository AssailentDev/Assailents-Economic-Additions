package me.assailent.economicadditions.hooks;

import me.assailent.economicadditions.EconomicAdditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

public class VaultHook {

    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static ConfigurationSection stockDisplay = plugin.getLangConfig().getConfigurationSection("stockdisplay");
    private static Economy econ = null;
    //private static Permission perms = null;

    private VaultHook() {}

    private static void setupEconomy() {
        final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp != null)
            econ = rsp.getProvider();
    }

    //private static boolean setupPermissions() {
    //    RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
    //    perms = rsp.getProvider();
    //    return perms != null;
    //}

    public static boolean hasEconomy() {
        return econ != null;
    }

    public static double getBalance(OfflinePlayer target) {
        if (!hasEconomy()) {
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        }

        return econ.getBalance(target);
    }

    public static String take(OfflinePlayer target, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found call hasEconomy() to check it first.");
        return econ.withdrawPlayer(target, amount).errorMessage;
    }

    public static String give(OfflinePlayer target, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found call hasEconomy() to check it first.");
        return econ.depositPlayer(target, amount).errorMessage;
    }

    public static String formatCurrencySymbol(double amount) {
        if (!hasEconomy()) {
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        }
        return amount + " " + (((int) amount) == 1 ? econ.currencyNameSingular() : econ.currencyNamePlural());
    }

    static {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
            //setupPermissions();
        }
    }

}
