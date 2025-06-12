package nl.multitime.craftKit.managers;

import nl.multitime.craftKit.models.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private final Map<String, Kit> kits;

    public KitManager() {
        this.kits = new HashMap<>();
        loadKits();
    }

    private void loadKits() {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        ItemStack[] warriorItems = new ItemStack[36];
        warriorItems[0] = sword;
        warriorItems[1] = new ItemStack(Material.COOKED_BEEF, 16);
        warriorItems[2] = new ItemStack(Material.GOLDEN_APPLE, 2);

        ItemStack[] warriorArmor = {
            new ItemStack(Material.IRON_BOOTS),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_HELMET)
        };

        Kit warrior = new Kit(
            "Warrior",
            Material.IRON_SWORD,
            Arrays.asList("§7A balanced fighter", "§7with iron armor and sword"),
            warriorItems,
            warriorArmor
        );

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemStack[] archerItems = new ItemStack[36];
        archerItems[0] = new ItemStack(Material.STONE_SWORD);
        archerItems[1] = bow;
        archerItems[2] = new ItemStack(Material.ARROW, 1);
        archerItems[3] = new ItemStack(Material.COOKED_BEEF, 16);
        archerItems[4] = new ItemStack(Material.GOLDEN_APPLE, 1);

        ItemStack[] archerArmor = {
            new ItemStack(Material.LEATHER_BOOTS),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.CHAINMAIL_CHESTPLATE),
            new ItemStack(Material.LEATHER_HELMET)
        };

        Kit archer = new Kit(
            "Archer",
            Material.BOW,
            Arrays.asList("§7Long range specialist", "§7with bow and light armor"),
            archerItems,
            archerArmor
        );

        kits.put("warrior", warrior);
        kits.put("archer", archer);
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public Map<String, Kit> getAllKits() {
        return new HashMap<>(kits);
    }
}