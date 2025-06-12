package nl.multitime.craftKit.models;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Kit {
    private final String name;
    private final Material icon;
    private final List<String> description;
    private final ItemStack[] items;
    private final ItemStack[] armor;

    public Kit(String name, Material icon, List<String> description, ItemStack[] items, ItemStack[] armor) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.items = items;
        this.armor = armor;
    }

    public void giveToPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(items);
        player.getInventory().setArmorContents(armor);
        player.updateInventory();
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getDescription() {
        return description;
    }

    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6" + name);
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }
}