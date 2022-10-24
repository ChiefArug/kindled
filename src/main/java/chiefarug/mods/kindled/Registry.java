package chiefarug.mods.kindled;

import chiefarug.mods.kindled.block.MagicPumpkinBlock;
import chiefarug.mods.kindled.entity.KindledBulletEntity;
import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static chiefarug.mods.kindled.Kindled.MODID;

public class Registry {

	public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
		@Override
		public @NotNull
		ItemStack makeIcon() {
			return new ItemStack(MAGIC_PUMPKIN_ITEM.get());
		}
	};
	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(TAB);
	public static final Material PUSHABLE_VEGETABLE = new Material.Builder(MaterialColor.PLANT).build();

	public static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
	public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

	public static final RegistryObject<EntityType<KindledEntity>> KINDLED_ENTITY = ENTITY_REGISTRY.register("kindled", () -> KindledEntity.ENTITY_TYPE);
	public static final RegistryObject<EntityType<KindledBulletEntity>> KINDLED_BULLET_ENTITY = ENTITY_REGISTRY.register("kindled_bullet", () -> KindledBulletEntity.ENTITY_TYPE);

	public static final RegistryObject<SoundEvent> KINDLED_HURT_SOUND = sound("kindled_hurt");
	public static final RegistryObject<SoundEvent> KINDLED_SHOOT_SOUND = sound("kindled_shoot");
	public static final RegistryObject<SoundEvent> KINDLED_POOF_SOUND = sound("kindled_poof");

	public static final RegistryObject<Block> MAGIC_PUMPKIN = BLOCK_REGISTRY.register("magic_pumpkin", () -> new MagicPumpkinBlock(BlockBehaviour.Properties.of(PUSHABLE_VEGETABLE, MaterialColor.COLOR_ORANGE).sound(SoundType.WOOD)));
	public static final RegistryObject<Item> MAGIC_PUMPKIN_ITEM = ITEM_REGISTRY.register("magic_pumpkin", () -> new BlockItem(MAGIC_PUMPKIN.get(), ITEM_PROPERTIES));
	public static final RegistryObject<Item> KINDLED_SPAWN_EGG = ITEM_REGISTRY.register("kindled_spawn_egg", () -> new ForgeSpawnEggItem(KINDLED_ENTITY, 0xE38A1D, 0x7E3D0E, ITEM_PROPERTIES));
	public static final RegistryObject<Item> MAGIC_DUST_ITEM = ITEM_REGISTRY.register("magic_dust", () -> new MagicDustItem(ITEM_PROPERTIES));

	public static void register(IEventBus bus) {
		ENTITY_REGISTRY.register(bus);
		BLOCK_REGISTRY.register(bus);
		ITEM_REGISTRY.register(bus);
		SOUND_REGISTRY.register(bus);
	}

	private static RegistryObject<SoundEvent> sound(String name) {
		return SOUND_REGISTRY.register(name, () -> new SoundEvent(new ResourceLocation(MODID, name)));
	}
}
