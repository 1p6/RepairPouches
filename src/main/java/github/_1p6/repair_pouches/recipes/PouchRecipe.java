package github._1p6.repair_pouches.recipes;

import com.google.gson.JsonObject;

import github._1p6.repair_pouches.RepairPouches;
import github._1p6.repair_pouches.Util;
import github._1p6.repair_pouches.items.RepairPouch;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class PouchRecipe extends ShapedRecipe {

	public int swordSlot = -1;
	
	public PouchRecipe(ResourceLocation id, String p_44154_, int swordSlot, int p_44155_, int p_44156_,
			NonNullList<Ingredient> igs, ItemStack res) {
		super(id, p_44154_, p_44155_, p_44156_, igs, res);
		this.swordSlot = swordSlot;
		
		if(!(res.getItem() instanceof RepairPouch))
			throw new Error("Repair Pouch recipe, " + id + " does not result in a Repair Pouch!");
		
		if(swordSlot < 0 || swordSlot >= 9)
			throw new Error("Repair Pouch recipe, " + id + " has an out of bounds swordSlot! (It must be between 0 and 8 inclusive.)");
		
		@SuppressWarnings("deprecation")
		Ingredient sword = Ingredient.of(Registry.ITEM.stream().parallel()
				.filter(item -> item instanceof SwordItem).map(Item::getDefaultInstance).filter(ItemStack::isRepairable));
		igs.set(swordSlot, sword);
	}
	
	@Override
	public ItemStack assemble(CraftingContainer c) {
		ItemStack res =  super.assemble(c);
		RepairPouch item = (RepairPouch) res.getItem();
		ItemStack sword = c.getItem(swordSlot);
		Tier t = item.getToolTier(sword);
		int amount = t.getUses() - sword.getDamageValue();
		item.setTier(res, t);
		item.setStoredDurability(res, amount);
		item.setStoredSharpness(res, Util.saturatingMul(
				amount,
				item.SHARPNESS_BONUS_PERCENT)/100);
		return res;
	}

	public static final RecipeSerializer<?> SERIALIZER = (new ShapedRecipe.Serializer() {
		
		@Override
		public ShapedRecipe fromJson(ResourceLocation id, JsonObject obj) {
			int swordSlot;
			try {
				swordSlot = obj.get("swordSlot").getAsInt();
			} catch(Throwable t) {
				throw new Error("Missing swordSlot in recipe " + id, t);
			}
			ShapedRecipe res = super.fromJson(id, obj);
			return new PouchRecipe(id, res.getGroup(), swordSlot, res.getWidth(), res.getHeight(), res.getIngredients(), res.getResultItem());
		}
		@Override
		public ShapedRecipe fromNetwork(ResourceLocation id, net.minecraft.network.FriendlyByteBuf buf) {
			int swordSlot = buf.readVarInt();
			ShapedRecipe res = super.fromNetwork(id, buf);
			return new PouchRecipe(id, res.getGroup(), swordSlot, res.getWidth(), res.getHeight(), res.getIngredients(), res.getResultItem());
		};
		
		@Override
		public void toNetwork(net.minecraft.network.FriendlyByteBuf buf, ShapedRecipe re) {
			buf.writeVarInt(((PouchRecipe) re).swordSlot);
			super.toNetwork(buf, re);
		};
		
	}).setRegistryName(RepairPouches.MODID, "pouch");
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
