package me.assailent.economicadditions.database;

import me.assailent.economicadditions.EconomicAdditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;

public class EconomyDatabase {

    private final Connection connection;

    private ConfigurationSection economy = EconomicAdditions.getPlugin().getLangConfig().getConfigurationSection("common");

    public EconomyDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path); // Needed for every database
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" + "uuid TEXT PRIMARY KEY, " +
                              "balance INTEGER NOT NULL DEFAULT " + economy.getString("starting-money") + ")");
        }
    }

    public void closeConnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(OfflinePlayer p) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid) VALUES (?)")){
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(OfflinePlayer player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updatePlayerBalance(OfflinePlayer player, double amount) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET balance = ? WHERE uuid = ?")) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public double getPlayerBalance(OfflinePlayer player) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                return Double.parseDouble(economy.getString("starting-money"));
            }
        }
    }

}
