package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(Kindled.MODID)
public class Kindled {
	public static final String MODID = "kindled";
	@SuppressWarnings("unused")
	public static final Logger LGGR = LogUtils.getLogger();

	public Kindled() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ENTITY_REGISTRY.register(bus);
		BLOCK_REGISTRY.register(bus);
		ITEM_REGISTRY.register(bus);
		bus.addListener(Kindled::registerAttributes);
	}

	public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(MAGIC_PUMPKIN_ITEM.get());
		}
	};
	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(TAB);

	public static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
	public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public static final RegistryObject<EntityType<KindledEntity>> KINDLED_ENTITY = ENTITY_REGISTRY.register("kindled", () -> KindledEntity.ENTITY_TYPE);
	public static final RegistryObject<Block> MAGIC_PUMPKIN = BLOCK_REGISTRY.register("magic_pumpkin", () -> new Block(BlockBehaviour.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_ORANGE).sound(SoundType.WOOD)));
	public static final RegistryObject<Item> MAGIC_PUMPKIN_ITEM = ITEM_REGISTRY.register("magic_pumpkin", () -> new BlockItem(MAGIC_PUMPKIN.get(), ITEM_PROPERTIES));
	public static final RegistryObject<Item> KINDLED_SPAWN_EGG = ITEM_REGISTRY.register("kindled_spawn_egg", () -> new ForgeSpawnEggItem(KINDLED_ENTITY, 0xE38A1D, 0x7E3D0E, ITEM_PROPERTIES));
	public static final RegistryObject<Item> MAGIC_DUST_ITEM = ITEM_REGISTRY.register("magic_dust", () -> new Item(ITEM_PROPERTIES));

	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KINDLED_ENTITY.get(), KindledEntity.createAttributes());
	}
}




