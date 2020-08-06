package cz.novosadkry.Pickaxe3X3.enchantments;

import cz.novosadkry.Pickaxe3X3.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AreaEnchantment extends Enchantment {
    private static AreaEnchantment enchant;

    public static AreaEnchantment getInstance() {
        if (enchant == null) {
            enchant = new AreaEnchantment(
                    new NamespacedKey(Main.getPlugin(Main.class), Main.mainConfig.enchantKey)
            );
        }

        return enchant;
    }

    public static void register() {
        AreaEnchantment enchant = getInstance();

        if (Enchantment.getByKey(enchant.getKey()) != null)
            return;

        try {
            Field acceptingNewField = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNewField.setAccessible(true);
            acceptingNewField.set(null, true);

            Enchantment.registerEnchantment(getInstance());
            Enchantment.stopAcceptingRegistrations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister() {
        AreaEnchantment enchant = getInstance();

        if (Enchantment.getByKey(enchant.getKey()) == null)
            return;

        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byKeyField.setAccessible(true);
            byNameField.setAccessible(true);

            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);

            byKey.remove(enchant.getKey());
            byName.remove(enchant.getKey().getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AreaEnchantment(NamespacedKey key) {
        super(key);
    }

    @Override
    public String getName() {
        return getKey().getKey();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        if (itemStack.getType().toString().contains("PICKAXE"))
            return true;

        return false;
    }
}
