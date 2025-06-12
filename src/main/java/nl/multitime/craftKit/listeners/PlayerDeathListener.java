package nl.multitime.craftKit.listeners;

import nl.multitime.craftKit.CraftKit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final CraftKit plugin;

    public PlayerDeathListener(CraftKit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        plugin.getDatabaseManager().updatePlayerStats(victim.getUniqueId(), 0, 1)
            .exceptionally(throwable -> {
                plugin.getLogger().severe("Error updating death stats for " + victim.getName() + ": " + throwable.getMessage());
                return null;
            });

        if (killer != null && killer != victim) {
            plugin.getDatabaseManager().updatePlayerStats(killer.getUniqueId(), 1, 0)
                .exceptionally(throwable -> {
                    plugin.getLogger().severe("Error updating kill stats for " + killer.getName() + ": " + throwable.getMessage());
                    return null;
                });

            killer.sendMessage("§a§l+1 Kill! §7You killed " + victim.getName());
        }

        victim.sendMessage("§c§lYou died! §7Use /kit to select a new kit when you respawn.");
    }
}