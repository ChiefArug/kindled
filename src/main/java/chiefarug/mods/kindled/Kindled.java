package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Kindled.MODID)
public class Kindled {
	public static final String MODID = "kindled";
	@SuppressWarnings("unused")
	public static final Logger LGGR = LogUtils.getLogger();

	public Kindled() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ENTITY_REGISTRY.register(bus);
		bus.addListener(Kindled::registerAttributes);
	}

	public static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

	public static final RegistryObject<EntityType<KindledEntity>> KINDLED_ENTITY = ENTITY_REGISTRY.register("kindled", () -> KindledEntity.ENTITY_TYPE);

	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KINDLED_ENTITY.get(), KindledEntity.createAttributes());
	}
}




