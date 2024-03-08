package me.assailent.economicadditions.commands;

import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.hooks.VaultHook;
import me.assailent.economicadditions.utilities.Parsing;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EconomyCommand implements TabExecutor {

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

        if (args.length < 2) {
            return false;
        }

        String param = args[0];
        String playerName = args[1];
        String amountRaw = args.length == 3 ? args[2]: "";

        new BukkitRunnable() {

            @Override
            public void run() {
                OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("error-color") + parse.parse(lang.getString("target-hasnt-played-before"), playerName, "")));
                    return;
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!VaultHook.hasEconomy()) {
                            sender.sendMessage(ChatColor.RED + "Vault plugin not found or it did not find any compatible Economy plugin.");
                            return;
                        }

                        if ("bal".equals(param)) {
                            double balance = VaultHook.getBalance(target);
                            sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("balance"), playerName, VaultHook.formatCurrencySymbol(balance))));
                        } else if ("take".equals(param) || "give".equals(param)) {
                            double amount;
                            try {
                                amount = Double.parseDouble(amountRaw);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("error-color") + parse.parse(lang.getString("non-valid-number"), amountRaw, "")));
                                return;
                            }
                            if ("take".equals(param)) {
                                String errorMessage = VaultHook.take(target, amount);

                                if (errorMessage != null && !errorMessage.isEmpty())
                                    sender.sendMessage(ChatColor.RED + "Error: " + errorMessage);
                                else {
                                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("took-money"), playerName, VaultHook.formatCurrencySymbol(amount))));
                                }
                            } else if ("set".equals(param)) {
                                String errorMessage = VaultHook.set(target, amount);
                                if (errorMessage != null && !errorMessage.isEmpty())
                                    sender.sendMessage(ChatColor.RED + "Error: " + errorMessage);
                                else {
                                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("set-money"), playerName, VaultHook.formatCurrencySymbol(amount))));
                                }

                            } else {
                                String errorMessage = VaultHook.give(target, amount);

                                if (errorMessage != null && !errorMessage.isEmpty())
                                    sender.sendMessage(ChatColor.RED + "Error: " + errorMessage);
                                else {
                                    sender.sendMessage(MiniMessage.miniMessage().deserialize(prefix + lang.getString("success-color") + parse.parse(lang.getString("gave-money"), playerName, VaultHook.formatCurrencySymbol(amount))));
                                }
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Unknown parameter '" + param + "'. Usage: " + command.getUsage());
                        }
                    }
                }.runTask(EconomicAdditions.getPlugin());
            }
        }.runTaskAsynchronously(EconomicAdditions.getPlugin());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> actions = new ArrayList<>();
            actions.add("bal");
            actions.add("give");
            actions.add("take");
            return actions;
        }
        if (args[0].equals("give") || args[0].equals("take")) {
            if (args.length == 2) {
                List<String> playerNames = new ArrayList<>();
                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(players);
                for (int i =0; i < players.length; i++) {
                    playerNames.add(players[i].getName());
                }

                return playerNames;
            }
        }
        return null;
    }
}
