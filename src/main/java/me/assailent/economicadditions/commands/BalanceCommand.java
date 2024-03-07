package me.assailent.economicadditions.commands;

import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.hooks.VaultHook;
import me.assailent.economicadditions.utilities.Parsing;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements TabExecutor {

    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static ConfigurationSection lang = plugin.getLangConfig().getConfigurationSection("economy");
    private static Parsing parse = EconomicAdditions.getParser();
    private static String prefix = plugin.getConfig().getString("prefix");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getString("sender-not-a-player"));
            return true;
        }

        if (args.length > 1) {
            return false;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (args.length == 0) {
                    double balance = VaultHook.getBalance((OfflinePlayer) sender);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("balance"), sender.getName(), VaultHook.formatCurrencySymbol(balance))));
                    return;
                } else {
                    if (!sender.hasPermission("EconomyAdditions.balance.others")) {
                        sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("error-color") + lang.getString("no-permission")));
                    }
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("error-color") + parse.parse(lang.getString("target-hasnt-played-before"), args[0], "")));
                    return;
                }
                if (!target.isOnline()) {
                    double balance = VaultHook.getBalance(target);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("balance"), target.getName(), VaultHook.formatCurrencySymbol(balance))));
                    return;
                }
                double balance = VaultHook.getBalance(target);
                sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("balance"), target.getName(), VaultHook.formatCurrencySymbol(balance))));
                return;
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i =0; i < players.length; i++) {
                playerNames.add(players[i].getName());
            }

            return playerNames;
        }
        return null;
    }
}
