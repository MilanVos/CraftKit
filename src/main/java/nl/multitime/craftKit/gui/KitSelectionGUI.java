package nl.multitime.craftKit.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import nl.multitime.craftKit.CraftKit;
import nl.multitime.craftKit.models.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KitSelectionGUI {

    private final CraftKit plugin;

    public KitSelectionGUI(CraftKit plugin) {
        this.plugin = plugin;
    }

    public void openKitMenu(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("§6§lSelect a Kit"))
                .rows(3)
                .disableAllInteractions()
                .create();

        int slot = 10;
        for (Kit kit : plugin.getKitManager().getAllKits().values()) {
            GuiItem kitItem = ItemBuilder.from(kit.getDisplayItem())
                    .asGuiItem(event -> {
                        kit.giveToPlayer(player);
                        player.sendMessage("§a§lKit Selected! §7You have received the " + kit.getName() + " kit!");
                        player.closeInventory();
                    });

            gui.setItem(slot, kitItem);
            slot += 2;
        }

        GuiItem closeItem = ItemBuilder.from(Material.BARRIER)
                .name(Component.text("§c§lClose"))
                .lore(Component.text("§7Click to close this menu"))
                .asGuiItem(event -> {
                    player.closeInventory();
                });

        gui.setItem(22, closeItem);

        gui.open(player);
    }
}