package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Kindled.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Kindled.MODID)
public class Kindled {
	public static final String MODID = "kindled";
	@SuppressWarnings("unused")
	public static final Logger LGGR = LogUtils.getLogger();
	public static final ResourceLocation MODRL = new ResourceLocation(MODID, MODID);

	public Kindled() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		KindledRegistry.register(bus);
		bus.addListener(Kindled::registerAttributes);
	}

	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(KindledRegistry.KINDLED_ENTITY.get(), KindledEntity.createAttributes());
	}

	@SubscribeEvent
	static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(DispenserBehaviour::registerBehaviours);
	}

	@SuppressWarnings("SpellCheckingInspection")
	/*
	//TODO: structure
	make sure all stair based jigsaws face the correct way
	/place jigsaw kindled:haunt/start down 7 ~64 ~ ~
	F9FFFE,WHITE
	F9801D,ORANGE
	C74EBD,MAGENTA
	3AB3DA,LIGHT_BLUE
	FED83D,YELLOW
	80C71F,LIGHT_GREEN
	F38BAA,PINK
	474F52,GRAY
	9D9D97,LIGHT_GRAY
	169C9C,CYAN
	8932B8,PURPLE
	3C44AA,BLUE
	835432,BROWN
	5E7C16,GREEN
	B02E26,RED
	1D1D21,BLACK





	 */
	public static final Map<Item, DyeColor> candles = new HashMap<>();
	static {
		candles.put(Items.CANDLE, null);
		candles.put(Items.WHITE_CANDLE, DyeColor.WHITE);
		candles.put(Items.ORANGE_CANDLE, DyeColor.ORANGE);
		candles.put(Items.MAGENTA_CANDLE, DyeColor.MAGENTA);
		candles.put(Items.LIGHT_BLUE_CANDLE, DyeColor.LIGHT_BLUE);
		candles.put(Items.YELLOW_CANDLE, DyeColor.YELLOW);
		candles.put(Items.LIME_CANDLE, DyeColor.LIME);
		candles.put(Items.PINK_CANDLE, DyeColor.PINK);
		candles.put(Items.GRAY_CANDLE, DyeColor.GRAY);
		candles.put(Items.LIGHT_GRAY_CANDLE, DyeColor.LIGHT_GRAY);
		candles.put(Items.CYAN_CANDLE, DyeColor.CYAN);
		candles.put(Items.PURPLE_CANDLE, DyeColor.PURPLE);
		candles.put(Items.BLUE_CANDLE, DyeColor.BLUE);
		candles.put(Items.BROWN_CANDLE, DyeColor.BROWN);
		candles.put(Items.GREEN_CANDLE, DyeColor.GREEN);
		candles.put(Items.RED_CANDLE, DyeColor.RED);
		candles.put(Items.BLACK_CANDLE, DyeColor.BLACK);
	}

}




