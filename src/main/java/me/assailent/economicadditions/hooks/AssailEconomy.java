package me.assailent.economicadditions.hooks;

import me.assailent.economicadditions.EconomicAdditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssailEconomy extends AbstractEconomy {

    private static ConfigurationSection stockDisplay = EconomicAdditions.getPlugin().getLangConfig().getConfigurationSection("stockdisplay");

    private AssailEconomy() {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "EconomicAdditions";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getLangConfig().getConfigurationSection("economy").getString("currency-plural");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getLangConfig().getConfigurationSection("economy").getString("currency-singular");
    }

    @Override
    public boolean hasAccount(String s) {
        return this.hasAccountByName(s);
    }

    private boolean hasAccountByName(String playerName) {
        try {
            return plugin.getEconomyDatabase().playerExists(plugin.getServer().getPlayer(playerName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return this.hasAccountByName(s);
    }

    @Override
    public double getBalance(String s) {
        return this.getByName(s);
    }

    private double getByName(String s) {
        try {
            return plugin.getEconomyDatabase().getPlayerBalance(plugin.getServer().getOfflinePlayer(s));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getBalance(String s, String s1) {
        return this.getByName(s);
    }

    @Override
    public boolean has(String s, double v) {
        return this.hasByName(s, v);
    }

    private boolean hasByName(String s, double v) {
        try {
            return plugin.getEconomyDatabase().getPlayerBalance(plugin.getServer().getOfflinePlayer(s)) >= v;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return this.hasByName(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return this.withdrawPlayer(s, null, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String s1, double amount) {
        if (amount < 0)
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");

        if (!has(playerName, amount)) {
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        try {
            plugin.getEconomyDatabase().updatePlayerBalance(plugin.getServer().getPlayer(playerName), plugin.getEconomyDatabase().getPlayerBalance(plugin.getServer().getPlayer(playerName)) - amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (target.isOnline() && stockDisplay.getInt("min-amount") < amount && stockDisplay.getBoolean("enabled")) {
            Player player = (Player) target;
            if (!player.hasMetadata("economicadditions.stockdisplay.toggled") || player.getMetadata("economicadditions.stockdisplay.toggled").get(0).asBoolean()) {
                new BukkitRunnable() {
                    float i = (float) (amount * 0.8);

                    @Override
                    public void run() {
                        if (i > (amount * 5)) {
                            cancel();
                        }
                        Title.Times times;
                        if (i == amount) {
                            times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1));
                            Title title = Title.title(MiniMessage.miniMessage().deserialize(stockDisplay.getString("neg-prefix") + i + stockDisplay.getString("neg-suffix")), Component.empty(), times);
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 0);
                        } else if (i < amount) {
                            times = Title.Times.times(Duration.ZERO, Duration.ofMillis(75), Duration.ZERO);
                            Title title = Title.title(MiniMessage.miniMessage().deserialize(stockDisplay.getString("neg-prefix") + i + stockDisplay.getString("neg-suffix")), Component.empty(), times);
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 0.1F);
                        }
                        i += amount * 0.002;
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }

        return new EconomyResponse(amount, this.getByName(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return this.depositPlayer(s, null, v);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String s1, double amount) {
        if (amount < 0)
            return new EconomyResponse(0, this.getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");

        try {
            plugin.getEconomyDatabase().updatePlayerBalance(plugin.getServer().getPlayer(playerName), plugin.getEconomyDatabase().getPlayerBalance(plugin.getServer().getPlayer(playerName)) + amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

        if (target.isOnline() && stockDisplay.getInt("min-amount") < amount && stockDisplay.getBoolean("enabled")) {
            Player player = (Player) target;
            if (!player.hasMetadata("economicadditions.stockdisplay.toggled") || player.getMetadata("economicadditions.stockdisplay.toggled").get(0).asBoolean()) {
                new BukkitRunnable() {
                    float i = (float) (amount * 0.8);

                    @Override
                    public void run() {
                        if (i > (amount * 5)) {
                            cancel();
                        }
                        Title.Times times;
                        if (i == amount) {
                            times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1));
                            Title title = Title.title(MiniMessage.miniMessage().deserialize(stockDisplay.getString("pos-prefix") + i + stockDisplay.getString("pos-suffix")), Component.empty(), times);
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 0);
                        } else if (i < amount) {
                            times = Title.Times.times(Duration.ZERO, Duration.ofMillis(75), Duration.ZERO);
                            Title title = Title.title(MiniMessage.miniMessage().deserialize(stockDisplay.getString("pos-prefix") + i + stockDisplay.getString("pos-suffix")), Component.empty(), times);
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10, 0.9F);
                        }
                        i += amount * 0.002;
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }

        return new EconomyResponse(amount, this.getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, this.getBalance(s1), EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    public static void register() {
        Bukkit.getServicesManager().register(Economy.class, new AssailEconomy(), EconomicAdditions.getPlugin(), ServicePriority.Normal);
    }

    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
}
