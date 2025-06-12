package nl.multitime.craftKit.commands;

import nl.multitime.craftKit.CraftKit;
import nl.multitime.craftKit.gui.KitSelectionGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private final CraftKit plugin;
    private final KitSelectionGUI kitGUI;

    public KitCommand(CraftKit plugin) {
        this.plugin = plugin;
        this.kitGUI = new KitSelectionGUI(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craftkit.kit")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        kitGUI.openKitMenu(player);
        return true;
    }
}