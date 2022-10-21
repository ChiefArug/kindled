package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Kindled.MODID)
@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD, modid = Kindled.MODID)
public class Kindled {
	public static final String MODID = "kindled";
	@SuppressWarnings("unused")
	public static final Logger LGGR = LogUtils.getLogger();

	public Kindled() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		Registry.register(bus);
		bus.addListener(Kindled::registerAttributes);
	}



	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(Registry.KINDLED_ENTITY.get(), KindledEntity.createAttributes());
	}

	@SubscribeEvent
	static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(DispenserBehaviour::registerBehaviours);
	}


	/*
	//TODO: structure
	make sure all stair based jigsaws face the correct way
	figure out why kindled arent spawning
	increase jigsaw chain length







	 */
}




