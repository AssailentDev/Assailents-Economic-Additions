package me.assailent.economicadditions;

import me.assailent.economicadditions.displays.ActionBar;
import me.assailent.economicadditions.commands.*;
import me.assailent.economicadditions.database.EconomyDatabase;
import me.assailent.economicadditions.events.EconomyJoin;
import me.assailent.economicadditions.events.GuiListener;
import me.assailent.economicadditions.hooks.AssailEconomy;
import me.assailent.economicadditions.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class EconomicAdditions extends JavaPlugin {

    private EconomyDatabase economyDatabase;
    private VaultHook vaultHook;
    private static EconomicAdditions plugin;

    private File langConfigFile;
    private FileConfiguration langConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        createLangConfig();

        try {

            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            economyDatabase = new EconomyDatabase(getDataFolder().getAbsolutePath() + "/balances.db");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to connect to the database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            AssailEconomy.register();
        }

        getCommand("economy").setExecutor(new EconomyCommand());
        getCommand("economy").setTabCompleter(new EconomyCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("pay").setTabCompleter(new PayCommand());
        getCommand("bal").setExecutor(new BalanceCommand());
        getCommand("bal").setTabCompleter(new BalanceCommand());

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getCommand("economygui").setExecutor(new EconomyGuiCommand());

        getCommand("stockdisplay").setExecutor(new StockDisplayCommand());

        getServer().getPluginManager().registerEvents(new EconomyJoin(), this);
        getCommand("toggleactionbar").setExecutor(new ActionBarCommand());

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && langConfig.getConfigurationSection("actionbar").getBoolean("enabled")) {
            getLogger().info("PlaceholderAPI hooked into");
            new ActionBar().ActionBarHandler();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().dispatchCommand(getServer().getConsoleSender(), "kick @a");
        try {
            economyDatabase.closeConnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getLangConfig() {
        return langConfig;
    }

    private void createLangConfig() {
        langConfigFile = new File(getDataFolder(), "lang.yml");
        if (!langConfigFile.exists()) {
            langConfigFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        langConfig = new YamlConfiguration();
        try {
            langConfig.load(langConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static EconomicAdditions getPlugin() { return plugin; }
    public EconomyDatabase getEconomyDatabase() {
        return this.economyDatabase;
    }
}
