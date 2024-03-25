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

public class PayCommand implements TabExecutor {

    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static Parsing parsing = new Parsing();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(parsing.common.getString("sender-not-a-player"));
            return true;
        }

        if (args.length != 2) {
            return false;
        }



        new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (!target.hasPlayedBefore() && !target.isOnline()) {
                    sender.sendMessage(parsing.parse(parsing.prefix + parsing.errorColor + parsing.common.getString("target-hasnt-played-before"), args[0], ""));
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch(NumberFormatException e) {
                    sender.sendMessage(parsing.parse(parsing.prefix + parsing.errorColor + parsing.common.getString("non-valid-number"), args[1], ""));
                    return;
                }

                if(Double.parseDouble(args[1])>VaultHook.getBalance((OfflinePlayer)sender)) {
                    sender.sendMessage(parsing.parse(parsing.prefix + parsing.errorColor + parsing.economy.getString("not-enough-money"), null, null));
                    return;
                }
                sender.sendMessage(parsing.parse(parsing.prefix + parsing.successColor + parsing.economy.getString("paid-player"), target.getName(), VaultHook.formatCurrencySymbol(amount)));
                if (target.isOnline()) {
                    Bukkit.getPlayer(target.getUniqueId()).sendMessage(parsing.parse(parsing.prefix + parsing.successColor + parsing.economy.getString("recieved-money"), sender.getName(), VaultHook.formatCurrencySymbol(amount)));
                }
                VaultHook.take((OfflinePlayer) sender, amount);
                VaultHook.give(target, amount);
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
        } else if (args.length == 2) {
            List<String> amounts = new ArrayList<>();
            amounts.add("10");
            amounts.add("50");
            amounts.add("100");
            amounts.add("1000");
        }
        return null;
    }
}
