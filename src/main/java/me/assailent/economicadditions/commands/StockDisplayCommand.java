package me.assailent.economicadditions.commands;

import me.assailent.economicadditions.EconomicAdditions;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class StockDisplayCommand implements CommandExecutor {

    private static EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static FileConfiguration config = plugin.getConfig();
    private static ConfigurationSection lang = plugin.getLangConfig().getConfigurationSection("stockdisplay");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasMetadata("economicadditions.stockdisplay.toggled") || player.getMetadata("economicadditions.stockdisplay.toggled").get(0).asBoolean()) {
            player.setMetadata("economicadditions.stockdisplay.toggled", new FixedMetadataValue(plugin, false));
            player.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("prefix") + lang.getString("toggle-off-text")));
        } else {
            player.setMetadata("economicadditions.stockdisplay.toggled", new FixedMetadataValue(plugin, true));
            player.sendMessage(MiniMessage.miniMessage().deserialize(config.getString("prefix") + lang.getString("toggle-on-text")));
        }
        return true;
    }
}
