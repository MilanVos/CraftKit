package nl.multitime.craftKit.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nl.multitime.craftKit.CraftKit;
import nl.multitime.craftKit.models.PlayerStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQLManager {

    private final CraftKit plugin;
    private HikariDataSource dataSource;

    public MySQLManager(CraftKit plugin) {
        this.plugin = plugin;
    }

    public void initialize(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC");
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        this.dataSource = new HikariDataSource(config);

        createTables();
    }

    private void createTables() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS player_stats (
                player_id VARCHAR(36) PRIMARY KEY,
                kills INT DEFAULT 0,
                deaths INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
            plugin.getLogger().info("Database tables created successfully!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create database tables: " + e.getMessage());
        }
    }

    public CompletableFuture<PlayerStats> getPlayerStats(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            String selectSQL = "SELECT kills, deaths FROM player_stats WHERE player_id = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(selectSQL)) {

                statement.setString(1, playerId.toString());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int kills = resultSet.getInt("kills");
                    int deaths = resultSet.getInt("deaths");
                    return new PlayerStats(playerId, kills, deaths);
                } else {
                    return new PlayerStats(playerId, 0, 0);
                }

            } catch (SQLException e) {
                plugin.getLogger().severe("Error retrieving player stats: " + e.getMessage());
                return new PlayerStats(playerId, 0, 0);
            }
        });
    }

    public CompletableFuture<Void> updatePlayerStats(UUID playerId, int killsToAdd, int deathsToAdd) {
        return CompletableFuture.runAsync(() -> {
            String upsertSQL = """
                INSERT INTO player_stats (player_id, kills, deaths) 
                VALUES (?, ?, ?) 
                ON DUPLICATE KEY UPDATE 
                kills = kills + VALUES(kills), 
                deaths = deaths + VALUES(deaths)
                """;

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(upsertSQL)) {

                statement.setString(1, playerId.toString());
                statement.setInt(2, killsToAdd);
                statement.setInt(3, deathsToAdd);
                statement.executeUpdate();

            } catch (SQLException e) {
                plugin.getLogger().severe("Error updating player stats: " + e.getMessage());
            }
        });
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}