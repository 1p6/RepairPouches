package github._1p6.repair_pouches.recipes;

import java.util.ArrayList;
import java.util.List;

import github._1p6.repair_pouches.RepairPouches;
import github._1p6.repair_pouches.Util;
import github._1p6.repair_pouches.items.RepairPouch;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

public class PouchStoreRecipe extends CustomRecipe {

	@Override
	public boolean matches(CraftingContainer c, Level p_44003_) {
		ItemStack pouch = null;
		int pouchId = -1;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.getItem() instanceof RepairPouch) {
				pouch = st;
				pouchId = i;
				break;
			}
		}
		if(pouch == null) return false;
		RepairPouch item = ((RepairPouch) pouch.getItem());
		boolean hasMats = false;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(i == pouchId || st.isEmpty()) continue;
			if(item.getTierForMaterial(st) == null)
				return false;
			hasMats = true;
		}
		return hasMats;
	}

	@Override
	public ItemStack assemble(CraftingContainer c) {
		ItemStack pouch = null;
		List<ItemStack> mats = new ArrayList<>();
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.isEmpty()) continue;
			if(st.getItem() instanceof RepairPouch) {
				pouch = st;
			} else {
				mats.add(st);
			}
		}
		if(mats.size() == 1) {
			return updatePouch(pouch, mats.get(0), mats);
		} else {
			ItemStack mat = mats.get(0).copy();
			mat.setCount(1);
			return mat; // updating pouch happens in getRemainingItems
		}
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer c) {
		NonNullList<ItemStack> res = super.getRemainingItems(c);
		ItemStack pouch = null;
		int pouchIndex = 0;
		List<ItemStack> mats = new ArrayList<>();
		ItemStack initMat = null;
		for(int i = 0; i < c.getContainerSize(); i++) {
			ItemStack st = c.getItem(i);
			if(st.isEmpty()) continue;
			if(st.getItem() instanceof RepairPouch) {
				pouch = st;
				pouchIndex = i;
			} else {
				if(initMat != null) mats.add(st);
				else initMat = st;
			}
		}
		if(!mats.isEmpty()) {
			res.set(pouchIndex, updatePouch(pouch, initMat, mats));
		}
		return res;
	}
	
	public ItemStack updatePouch(ItemStack pouch, ItemStack initMat, List<ItemStack> mats) {
		pouch = pouch.copy();
		RepairPouch item = (RepairPouch) pouch.getItem();
		if(item.getTier(pouch) == null)
			item.setTier(pouch, item.getTierForMaterial(initMat));
		for (ItemStack mat : mats) {
			Tier matT = item.getTierForMaterial(mat);
			int amount = matT.getUses()/4;
			item.setStoredDurability(pouch, Util.saturatingAdd(amount, item.getStoredDurability(pouch)));
			if (matT.getUses() >= item.getTierUses(pouch))
				item.setStoredSharpness(pouch, Util.saturatingMul(
						Util.saturatingAdd(amount, item.getStoredSharpness(pouch)),
						item.SHARPNESS_BONUS_PERCENT)/100);
		}
		return pouch;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w*h >= 2;
	}
	
	public PouchStoreRecipe(ResourceLocation id) {
		super(id);
	}
	
	public static final RecipeSerializer<?> SERIALIZER = new SimpleRecipeSerializer<>(PouchStoreRecipe::new)
			.setRegistryName(RepairPouches.MODID, "pouch_store");

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
