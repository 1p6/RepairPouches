package github._1p6.repair_pouches.recipes;

import github._1p6.repair_pouches.RepairPouches;
import github._1p6.repair_pouches.Util;
import github._1p6.repair_pouches.items.RepairPouch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;

public class PouchChangeTierRecipe extends UpgradeRecipe {

	@Override
	public boolean matches(Container c, Level p_44003_) {
		Item item = c.getItem(0).getItem();
		return item instanceof RepairPouch &&
				((RepairPouch) item).getTierForMaterial(c.getItem(1)) != null;
	}

	@Override
	public ItemStack assemble(Container c) {
		ItemStack pouch = c.getItem(0);
		ItemStack mat = c.getItem(1);
		RepairPouch item = (RepairPouch) pouch.getItem();
		Tier newTier = item.getTierForMaterial(mat);
		pouch = pouch.copy();
		int amount = newTier.getUses()/4;
		if(newTier.getUses() > item.getTierUses(pouch))
			item.setStoredSharpness(pouch, Util.saturatingMul(
					amount, item.SHARPNESS_BONUS_PERCENT)/100);
		else
			item.setStoredSharpness(pouch, Util.saturatingMul(
					Util.saturatingAdd(amount, item.getStoredSharpness(pouch)),
					item.SHARPNESS_BONUS_PERCENT)/100);
		item.setStoredDurability(pouch, Util.saturatingAdd(amount, item.getStoredDurability(pouch)));
		item.setTier(pouch, newTier);
		return pouch;
	}
	
	@Override
	public boolean isAdditionIngredient(ItemStack st) {
		return RepairPouch.ITEM.getTierForMaterial(st) != null;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w*h >= 2;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	@Override
	public boolean isIncomplete() {
		return false;
	}
	
	public PouchChangeTierRecipe(ResourceLocation id) {
		super(id, Ingredient.EMPTY, Ingredient.EMPTY, ItemStack.EMPTY);
	}

	public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(PouchChangeTierRecipe::new)
			.setRegistryName(RepairPouches.MODID, "pouch_change_tier");
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

}
