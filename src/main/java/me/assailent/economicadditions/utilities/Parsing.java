package me.assailent.economicadditions.utilities;

import me.assailent.economicadditions.EconomicAdditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public class Parsing {

    public Component parse(String input, @Nullable String parse, @Nullable String currency) {
        String[] split = input.split("%");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("parse"))
                sb.append(parse);
            else if (split[i].equals("currency"))
                sb.append(currency);
            else
                sb.append(split[i]);
        }
        return MiniMessage.miniMessage().deserialize(sb.toString());
    }

    private EconomicAdditions plugin = EconomicAdditions.getPlugin();
    private FileConfiguration lang = plugin.getLangConfig();
    public ConfigurationSection common = lang.getConfigurationSection("common");
    public ConfigurationSection economy = lang.getConfigurationSection("economy");
    public ConfigurationSection actionbar = lang.getConfigurationSection("actionbar");
    public ConfigurationSection stockdisplay = lang.getConfigurationSection("stockdisplay");
    public ConfigurationSection guimessages = lang.getConfigurationSection("gui").getConfigurationSection("economy").getConfigurationSection("messages");
    public ConfigurationSection payMenu = lang.getConfigurationSection("gui").getConfigurationSection("economy").getConfigurationSection("payMenu");
    public String prefix = common.getString("prefix");
    public String errorColor = common.getString("error-color");
    public String successColor = common.getString("success-color");
}
