package me.assailent.economicadditions.commands;

import me.assailent.economicadditions.menus.EconomyMainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EconomyGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player!");
            return true;
        }
        Player player = (Player) sender;
        player.openInventory(EconomyMainMenu.openInv(player));

        return true;
    }
}
