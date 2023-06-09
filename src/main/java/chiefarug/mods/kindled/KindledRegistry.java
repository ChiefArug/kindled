package chiefarug.mods.kindled;

import chiefarug.mods.kindled.block.MagicPumpkinBlock;
import chiefarug.mods.kindled.entity.KindledBulletEntity;
import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = MOD, modid = MODID)
public class KindledRegistry {

	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties();


	private static final DeferredRegister<EntityType<?>> ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
	private static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

	public static final RegistryObject<EntityType<KindledEntity>> KINDLED_ENTITY = ENTITY_REGISTRY.register("kindled", () -> KindledEntity.ENTITY_TYPE);
	public static final RegistryObject<EntityType<KindledBulletEntity>> KINDLED_BULLET_ENTITY = ENTITY_REGISTRY.register("kindled_bullet", () -> KindledBulletEntity.ENTITY_TYPE);

	public static final RegistryObject<SoundEvent> KINDLED_HURT_SOUND = sound("kindled_hurt");
	public static final RegistryObject<SoundEvent> KINDLED_SHOOT_SOUND = sound("kindled_shoot");
	public static final RegistryObject<SoundEvent> KINDLED_POOF_SOUND = sound("kindled_poof");

	public static final RegistryObject<Block> MAGIC_PUMPKIN = BLOCK_REGISTRY.register("magic_pumpkin", () -> new MagicPumpkinBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).pushReaction(PushReaction.NORMAL).sound(SoundType.WOOD)));
	public static final RegistryItem MAGIC_PUMPKIN_ITEM = new RegistryItem(ITEM_REGISTRY.register("magic_pumpkin", () -> new BlockItem(MAGIC_PUMPKIN.get(), ITEM_PROPERTIES)));
	public static final RegistryItem KINDLED_SPAWN_EGG = new RegistryItem(ITEM_REGISTRY.register("kindled_spawn_egg", () -> new ForgeSpawnEggItem(KINDLED_ENTITY, 0xE38A1D, 0x7E3D0E, ITEM_PROPERTIES)));
	public static final RegistryItem MAGIC_DUST_ITEM = new RegistryItem(ITEM_REGISTRY.register("magic_dust", () -> new MagicDustItem(ITEM_PROPERTIES)));

	public static final ResourceKey<DamageType> POOF = ResourceKey.create(Registries.DAMAGE_TYPE, MODRL.withPath("poofed"));

	public static final DeferredRegister<CreativeModeTab> TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final RegistryObject<CreativeModeTab> TAB = TAB_REGISTRY.register(MODID, () -> new CreativeModeTab.Builder(CreativeModeTab.Row.BOTTOM, 1)
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS).icon(MAGIC_PUMPKIN_ITEM)
			.title(Component.translatable("itemGroup.kindled"))
			.displayItems((config, builder) -> {
				builder.accept(MAGIC_PUMPKIN_ITEM);
				builder.accept(MAGIC_DUST_ITEM);
				builder.accept(KINDLED_SPAWN_EGG);

				Kindled.candles.keySet().forEach(candle ->
						builder.accept(() -> candle)
				);
			}).build());

	@SubscribeEvent
	public static void extraCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(KINDLED_SPAWN_EGG);
		}
	}

	public static void register(IEventBus bus) {
		ENTITY_REGISTRY.register(bus);
		BLOCK_REGISTRY.register(bus);
		ITEM_REGISTRY.register(bus);
		SOUND_REGISTRY.register(bus);
		TAB_REGISTRY.register(bus);
	}

	private static RegistryObject<SoundEvent> sound(String name) {
		return SOUND_REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(MODRL.withPath(name)));
	}

	public static DamageSource poof(Entity entity) {
		return new DamageSource(entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(POOF), entity);
	}

	record RegistryItem(RegistryObject<Item> item) implements ItemLike, Supplier<ItemStack> {
		@NotNull
		@Override
		public Item asItem() {
			return item().get();
		}
		@NotNull
		@Override
		public ItemStack get() {
			return new ItemStack(asItem());
		}
	}
}
