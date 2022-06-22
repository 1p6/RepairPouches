package github._1p6.repair_pouches.items;

import java.util.List;

import github._1p6.repair_pouches.RepairPouches;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;

public class RepairPouch extends Item {
	
	public static final RepairPouch ITEM = (RepairPouch) new RepairPouch(new Properties().stacksTo(1)
			.tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(RepairPouches.MODID, "repair_pouch");
	public static final String NBT_DURABILITY = "1p6.repair_pouches.durability";
	public static final String NBT_SHARPNESS = "1p6.repair_pouches.sharpness";
	public static final String NBT_TIER = "1p6.repair_pouches.tier";
	public int SHARPNESS_BONUS_PERCENT = 101;

	public RepairPouch(Properties p_41383_) {
		super(p_41383_);
	}
	
	public ItemStack of(int durability, int sharpness, Tier tier) {
		ItemStack s = new ItemStack(this);
		setStoredDurability(s, durability);
		setStoredSharpness(s, sharpness);
		setTier(s, tier);
		return s;
	}
	
	public Tier getTierForMaterial(ItemStack material) {
		if(material.isEmpty()) return null;
		for(Tier t : TierSortingRegistry.getSortedTiers()) {
			if(t.getRepairIngredient().test(material)) return t;
		}
		return null;
	}
	
	public Tier getToolTier(ItemStack tool) {
		if(tool.isEmpty()) return null;
		Item toolItem = tool.getItem();
		if(!toolItem.isRepairable(tool)) return null;
		if(toolItem instanceof TieredItem) return ((TieredItem) toolItem).getTier();
		try {
			return getTierForMaterial(((ArmorItem) toolItem).getMaterial().getRepairIngredient().getItems()[0]);
		} catch(Throwable t) {}
		return Tiers.WOOD;
	}
	
	@Override
	public Component getName(ItemStack st) {
		String descId = getDescriptionId();
		Component c;
		try {
			c = getTier(st).getRepairIngredient().getItems()[0].getHoverName();
		} catch(Throwable t) {
			try {
				String s = st.getTag().getString(NBT_TIER);
				if(s == "") throw null;
				c = new TextComponent(s);
			} catch(Throwable t2) {
				c = new TranslatableComponent(descId + ".no_tier");
			}
		}
		return new TranslatableComponent(descId, c);
	}
	
	@Override
	public void appendHoverText(ItemStack st, Level p_41422_, List<Component> texts, TooltipFlag p_41424_) {
		texts.add(new TextComponent(getStoredDurability(st) + " / " + getStoredSharpness(st)));
	}
	
	public int getStoredDurability(ItemStack item) {
		return item.hasTag() ? item.getTag().getInt(NBT_DURABILITY) : 0;
	}
	
	public int getStoredSharpness(ItemStack item) {
		return item.hasTag() ? item.getTag().getInt(NBT_SHARPNESS) : 0;
	}
	
	public void setStoredDurability(ItemStack item, int d) {
		item.getOrCreateTag().putInt(NBT_DURABILITY, d);
	}
	
	public void setStoredSharpness(ItemStack item, int s) {
		item.getOrCreateTag().putInt(NBT_SHARPNESS, s);
	}
	
	public Tier getTier(ItemStack item) {
		try {
			return TierSortingRegistry.byName(new ResourceLocation(item.getTag().getString(NBT_TIER)));
		} catch(Throwable t) {
			return null;
		}
	}
	
	public int getTierUses(ItemStack item) {
		Tier t = getTier(item);
		return t == null ? 0 : t.getUses();
	}
	
	public void setTier(ItemStack item, Tier t) {
		item.getOrCreateTag().putString(NBT_TIER, TierSortingRegistry.getName(t).toString());
	}
}
