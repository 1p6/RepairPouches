package github._1p6.repair_pouches.integration.jei;

import github._1p6.repair_pouches.RepairPouches;
import github._1p6.repair_pouches.items.RepairPouch;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RepairPouchesJEI implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(RepairPouches.MODID, "jei_plugin");
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {
		reg.addIngredientInfo(RepairPouch.ITEM.getDefaultInstance(), VanillaTypes.ITEM_STACK,
				new TranslatableComponent(RepairPouch.ITEM.getDescriptionId() + ".info"));
	}
}
