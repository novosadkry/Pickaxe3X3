package cz.novosadkry.Pickaxe3X3.enchantments;

import cz.novosadkry.Pickaxe3X3.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class AreaEnchantment extends Enchantment {
    private static AreaEnchantment enchant;
    private static NamespacedKey namespacedKey;

    static {
        namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), Main.mainConfig.enchantKey);
    }

    public static AreaEnchantment getInstance() {
        if (enchant == null)
            enchant = new AreaEnchantment(namespacedKey);

        return enchant;
    }

    public static void register() {
        if (Enchantment.getByKey(namespacedKey) != null)
            return;

        try {
            Field acceptingNewField = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNewField.setAccessible(true);
            acceptingNewField.set(null, true);

            Enchantment.registerEnchantment(getInstance());

            acceptingNewField.set(null, false);
            acceptingNewField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AreaEnchantment(NamespacedKey key) {
        super(key);
    }

    @Override
    public String getName() {
        return "Area";
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
