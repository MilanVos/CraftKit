package nl.multitime.craftKit.models;

import java.util.UUID;

public class PlayerStats {
    private UUID playerId;
    private int kills;
    private int deaths;

    public PlayerStats(UUID playerId, int kills, int deaths) {
        this.playerId = playerId;
        this.kills = kills;
        this.deaths = deaths;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public double getKDRatio() {
        if (deaths == 0) {
            return kills;
        }
        return (double) kills / deaths;
    }
}