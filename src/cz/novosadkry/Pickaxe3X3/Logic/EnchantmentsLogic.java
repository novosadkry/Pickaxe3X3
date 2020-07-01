package cz.novosadkry.Pickaxe3X3.Logic;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentsLogic {
    public static ItemStack getDropSilkTouch(Block block) {
        return new ItemStack(block.getType(), 1);
    }

    public static ItemStack getDropAutoSmelt(ItemStack item, Block block) {
        Iterator<Recipe> it = Bukkit.recipeIterator();
        ItemStack drop = block.getDrops(item).iterator().next();

        while (it.hasNext()) {
            Recipe recipe = it.next();

            if (recipe instanceof FurnaceRecipe)
            {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe)recipe;

                if (furnaceRecipe.getInput().getType() == drop.getType())
                    return furnaceRecipe.getResult();
            }
        }

        return null;
    }

    public static int getDropCountUnbreaking(int count, int level) {
        float random = ThreadLocalRandom.current().nextFloat();

        if (random > ((float)1 / (level + 1)))
            return 0;
        else
            return count;
    }
}
