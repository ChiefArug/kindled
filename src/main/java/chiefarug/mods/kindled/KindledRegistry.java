package chiefarug.mods.kindled;

import chiefarug.mods.kindled.block.MagicPumpkinBlock;
import chiefarug.mods.kindled.entity.KindledBulletEntity;
import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static chiefarug.mods.kindled.Kindled.MODID;
import static chiefarug.mods.kindled.Kindled.MODRL;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD, modid = MODID)
public class KindledRegistry {

	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties();
	public static final Material PUSHABLE_VEGETABLE = new Material.Builder(MaterialColor.PLANT).build();

	private static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
	private static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

	public static final RegistryObject<EntityType<KindledEntity>> KINDLED_ENTITY = ENTITY_REGISTRY.register("kindled", () -> KindledEntity.ENTITY_TYPE);
	public static final RegistryObject<EntityType<KindledBulletEntity>> KINDLED_BULLET_ENTITY = ENTITY_REGISTRY.register("kindled_bullet", () -> KindledBulletEntity.ENTITY_TYPE);

	public static final RegistryObject<SoundEvent> KINDLED_HURT_SOUND = sound("kindled_hurt");
	public static final RegistryObject<SoundEvent> KINDLED_SHOOT_SOUND = sound("kindled_shoot");
	public static final RegistryObject<SoundEvent> KINDLED_POOF_SOUND = sound("kindled_poof");

	public static final RegistryObject<Block> MAGIC_PUMPKIN = BLOCK_REGISTRY.register("magic_pumpkin", () -> new MagicPumpkinBlock(BlockBehaviour.Properties.of(PUSHABLE_VEGETABLE, MaterialColor.COLOR_ORANGE).sound(SoundType.WOOD)));
	public static final RegistryItem MAGIC_PUMPKIN_ITEM = new RegistryItem(ITEM_REGISTRY.register("magic_pumpkin", () -> new BlockItem(MAGIC_PUMPKIN.get(), ITEM_PROPERTIES)));
	public static final RegistryItem KINDLED_SPAWN_EGG = new RegistryItem(ITEM_REGISTRY.register("kindled_spawn_egg", () -> new ForgeSpawnEggItem(KINDLED_ENTITY, 0xE38A1D, 0x7E3D0E, ITEM_PROPERTIES)));
	public static final RegistryItem MAGIC_DUST_ITEM = new RegistryItem(ITEM_REGISTRY.register("magic_dust", () -> new MagicDustItem(ITEM_PROPERTIES)));

	public static final ResourceKey<DamageType> POOF = ResourceKey.create(Registries.DAMAGE_TYPE, MODRL.withPath("poofed"));


	@SubscribeEvent
	public static void creativeModTabEvent(CreativeModeTabEvent.Register event) {
		event.registerCreativeModeTab(MODRL, config -> {
			config.icon(() -> new ItemStack(MAGIC_PUMPKIN_ITEM.get()));
			config.displayItems((params, out) -> {
				out.accept(MAGIC_PUMPKIN_ITEM);
				out.accept(MAGIC_DUST_ITEM);
				out.accept(KINDLED_SPAWN_EGG);
			});
		});
	}

	public static void register(IEventBus bus) {
		ENTITY_REGISTRY.register(bus);
		BLOCK_REGISTRY.register(bus);
		ITEM_REGISTRY.register(bus);
		SOUND_REGISTRY.register(bus);
	}

	private static RegistryObject<SoundEvent> sound(String name) {
		return SOUND_REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, name)));
	}

	public static DamageSource poof(Entity entity) {
		return new DamageSource(entity.getLevel().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(POOF), entity);
	}

	record RegistryItem(RegistryObject<Item> item) implements ItemLike, Supplier<Item> {
		@NotNull
		@Override
		public Item asItem() {
			return get();
		}
		@NotNull
		@Override
		public Item get() {
			return item().get();
		}
	}
}
