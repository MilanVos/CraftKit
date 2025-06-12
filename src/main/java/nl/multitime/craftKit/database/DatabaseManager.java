package nl.multitime.craftKit.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.multitime.craftKit.CraftKit;
import nl.multitime.craftKit.models.PlayerStats;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final CraftKit plugin;
    private final Gson gson;
    private final File dataFile;
    private Map<UUID, PlayerStats> playerStats;

    public DatabaseManager(CraftKit plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        this.dataFile = new File(plugin.getDataFolder(), "playerStats.json");
        this.playerStats = new HashMap<>();
    }

    public void initialize() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<UUID, PlayerStats>>(){}.getType();
            Map<UUID, PlayerStats> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                this.playerStats = loaded;
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load player stats: " + e.getMessage());
        }
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            gson.toJson(playerStats, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player stats: " + e.getMessage());
        }
    }

    public CompletableFuture<PlayerStats> getPlayerStats(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            return playerStats.getOrDefault(playerId, new PlayerStats(playerId, 0, 0));
        });
    }

    public CompletableFuture<Void> updatePlayerStats(UUID playerId, int kills, int deaths) {
        return CompletableFuture.runAsync(() -> {
            PlayerStats stats = playerStats.getOrDefault(playerId, new PlayerStats(playerId, 0, 0));
            stats.setKills(stats.getKills() + kills);
            stats.setDeaths(stats.getDeaths() + deaths);
            playerStats.put(playerId, stats);
            saveData();
        });
    }

    public void close() {
        saveData();
    }
}