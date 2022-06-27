package github._1p6.repair_pouches.recipes;

import github._1p6.repair_pouches.RepairPouches;
import github._1p6.repair_pouches.items.RepairPouch;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

public class PouchRepairRecipe extends CustomRecipe {

	public PouchRepairRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer c, Level p_44003_) {
		ItemStack pouch = null, tool = null;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.isEmpty()) continue;
			if(st.getItem() instanceof RepairPouch) {
				if(pouch != null) return false;
				else pouch = st;
			} else {
				if(tool != null) return false;
				else tool = st;
			}
		}
		if(pouch == null || tool == null) return false;
		RepairPouch item = (RepairPouch) pouch.getItem();
		if(item.getToolTier(tool) == null) return false;
		int dmg = tool.getDamageValue();
		int dur = item.getStoredDurability(pouch);
		int sharp = item.getStoredSharpness(pouch);
		return dmg > 0 && dur > 0 && sharp > 0 && item.getTierUses(pouch) >= item.getToolTier(tool).getUses();
	}

	@Override
	public ItemStack assemble(CraftingContainer c) {
		ItemStack pouch = null, tool = null;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.isEmpty()) continue;
			if(st.getItem() instanceof RepairPouch)
				pouch = st;
			else tool = st;
		}
		RepairPouch item = (RepairPouch) pouch.getItem();
		int dmg = tool.getDamageValue();
		int dur = item.getStoredDurability(pouch);
		int sharp = item.getStoredSharpness(pouch);
		int amount = Math.min(dmg, Math.min(dur, sharp));
		tool = tool.copy();
		tool.setDamageValue(dmg - amount);
		return tool;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer c) {
		NonNullList<ItemStack> res = NonNullList.withSize(c.getContainerSize(), ItemStack.EMPTY);
		ItemStack pouch = null, tool = null;
		int batIndex = 0;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.isEmpty()) continue;
			if(st.getItem() instanceof RepairPouch) {
				pouch = st;
				batIndex = i;
			} else tool = st;
		}
		RepairPouch item = (RepairPouch) pouch.getItem();
		int dmg = tool.getDamageValue();
		int dur = item.getStoredDurability(pouch);
		int sharp = item.getStoredSharpness(pouch);
		int amount = Math.min(dmg, Math.min(dur, sharp));
		pouch = pouch.copy();
		item.setStoredDurability(pouch, dur - amount);
		item.setStoredSharpness(pouch, sharp - amount);
		res.set(batIndex, pouch);
		return res;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w*h >= 2;
	}

	
	public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(PouchRepairRecipe::new)
			.setRegistryName(RepairPouches.MODID, "pouch_repair");
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

}
