package github._1p6.repair_pouches.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(bus = Bus.MOD)
public final class Items {
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> ev) {
		ev.getRegistry().registerAll(RepairPouch.ITEM);
	}
}
