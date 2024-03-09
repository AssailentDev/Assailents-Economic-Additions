package me.assailent.economicadditions;

import me.assailent.economicadditions.actionbar.ActionBar;
import me.assailent.economicadditions.commands.*;
import me.assailent.economicadditions.database.EconomyDatabase;
import me.assailent.economicadditions.events.ActionBarJoin;
import me.assailent.economicadditions.events.GuiListener;
import me.assailent.economicadditions.hooks.AssailEconomy;
import me.assailent.economicadditions.hooks.VaultHook;
import me.assailent.economicadditions.utilities.Parsing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public final class EconomicAdditions extends JavaPlugin {

    private EconomyDatabase economyDatabase;
    private VaultHook vaultHook;
    private static EconomicAdditions plugin;
    private static Parsing parser;

    private File langConfigFile;
    private FileConfiguration langConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        parser = new Parsing();
        plugin = this;
        saveResource("config.yml", false);
        saveDefaultConfig();
        createLangConfig();

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

        getServer().getPluginManager().registerEvents(new ActionBarJoin(), this);
        getCommand("toggleactionbar").setExecutor(new ActionBarCommand());

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && langConfig.getConfigurationSection("actionbar").getBoolean("enabled")) {
            getLogger().info("PlaceholderAPI hooked into");
            new ActionBar().ActionBarHandler();
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required!");
            getServer().getPluginManager().disablePlugin(this);
        }

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
    public static Parsing getParser() {return parser; }
    public EconomyDatabase getEconomyDatabase() {
        return this.economyDatabase;
    }
}
