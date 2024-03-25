package me.assailent.economicadditions.commands;

import me.assailent.economicadditions.EconomicAdditions;
import me.assailent.economicadditions.utilities.Parsing;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ActionBarCommand implements CommandExecutor {

    private EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private static Parsing parsing = new Parsing();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(parsing.common.getString("sender-not-a-player"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasMetadata("economicadditions.actionbar.toggled") || player.getMetadata("economicadditions.actionbar.toggled").get(0).asBoolean()) {
            player.setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, false));
            player.sendMessage(parsing.parse(parsing.prefix + parsing.errorColor + parsing.actionbar.getString("toggle-off-text"), null, null));
        } else {
            player.setMetadata("economicadditions.actionbar.toggled", new FixedMetadataValue(plugin, true));
            player.sendMessage(parsing.parse(parsing.prefix + parsing.successColor + parsing.actionbar.getString("toggle-on-text"), null, null));
        }
        return true;
    }
}
