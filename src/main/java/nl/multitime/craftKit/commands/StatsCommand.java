package nl.multitime.craftKit.commands;

import nl.multitime.craftKit.CraftKit;
import nl.multitime.craftKit.models.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    private final CraftKit plugin;

    public StatsCommand(CraftKit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("craftkit.stats")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cConsole must specify a player name!");
                return true;
            }

            Player player = (Player) sender;
            showStats(sender, player.getUniqueId(), player.getName());
        } else {
            if (!sender.hasPermission("craftkit.stats.others")) {
                sender.sendMessage("§cYou don't have permission to view other players' stats!");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);

            if (target == null) {
                // Try to get offline player
                @SuppressWarnings("deprecation")
                org.bukkit.OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);

                if (!offlinePlayer.hasPlayedBefore()) {
                    sender.sendMessage("§cPlayer '" + targetName + "' has never played on this server!");
                    return true;
                }

                showStats(sender, offlinePlayer.getUniqueId(), offlinePlayer.getName());
            } else {
                showStats(sender, target.getUniqueId(), target.getName());
            }
        }

        return true;
    }

    private void showStats(CommandSender sender, UUID playerId, String playerName) {
        plugin.getDatabaseManager().getPlayerStats(playerId).thenAccept(stats -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§6§l=== Stats for " + playerName + " ===");
                sender.sendMessage("§e§lKills: §f" + stats.getKills());
                sender.sendMessage("§c§lDeaths: §f" + stats.getDeaths());
                sender.sendMessage("§b§lK/D Ratio: §f" + String.format("%.2f", stats.getKDRatio()));
                sender.sendMessage("§6§l========================");
            });
        }).exceptionally(throwable -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§cError retrieving stats for " + playerName + "!");
            });
            plugin.getLogger().severe("Error retrieving stats: " + throwable.getMessage());
            return null;
        });
    }
}