package nl.multitime.craftKit;

import nl.multitime.craftKit.commands.KitCommand;
import nl.multitime.craftKit.commands.StatsCommand;
import nl.multitime.craftKit.database.DatabaseManager;
import nl.multitime.craftKit.listeners.PlayerDeathListener;
import nl.multitime.craftKit.managers.KitManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftKit extends JavaPlugin {

    private DatabaseManager databaseManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.initialize();

        this.kitManager = new KitManager();

        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kitstats").setExecutor(new StatsCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

        getLogger().info("CraftKit has been enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("CraftKit has been disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }
}
