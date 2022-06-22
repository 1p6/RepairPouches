package github._1p6.repair_pouches.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD)
public final class Recipes {
	@SubscribeEvent
	public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> ev) {
		ev.getRegistry().registerAll(PouchStoreRecipe.SERIALIZER,
				PouchRepairRecipe.SERIALIZER,
				PouchChangeTierRecipe.SERIALIZER,
				PouchRecipe.SERIALIZER);
	}
}
