package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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
	/place jigsaw kindled:tower/start down 7 ~64 ~ ~
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

	public static void addBunchOfClientParticles(Level level, ParticleOptions particle, int amount, RandomSource random, BlockPos pos) {
		for (int i = 0; i < amount; i++) {
			Kindled.ParticleOffset offset = getRandomOffset(random);
			level.addParticle(particle, pos.getX() + 0.5D + offset.getX(), pos.getY() + 0.5D + offset.getY(), pos.getZ() + 0.5D + offset.getZ(), getRandomSpeed(random), getRandomSpeed(random), getRandomSpeed(random));
		}
	}

	public static double getRandomSpeed(RandomSource random) {
		return (0.5D - random.nextFloat()) / 4;
	}

	public static ParticleOffset getRandomOffset(RandomSource random) {
		ParticleOffset po = new ParticleOffset();
		switch (randomDir(random)) {
			case DOWN -> po.setY(-0.5F);
			case UP -> po.setY(0.5F);
			case NORTH -> po.setZ(-0.5F);
			case SOUTH -> po.setZ(0.5F);
			case WEST -> po.setX(-0.5F);
			case EAST -> po.setX(0.5F);
		}
		po.randomizeOthers(random);
		return po;
	}

	public static Direction randomDir(RandomSource random) {
		return Direction.getRandom(random);
	}

	public static class ParticleOffset {
		private Float x;
		private Float y;
		private Float z;
		public float getX() {return x;}
		public void setX(float x) {this.x = x;}
		public float getY() {return y;}
		public void setY(float y) {this.y = y;}
		public float getZ() {return z;}
		public void setZ(float z) {this.z = z;}
		public void randomizeOthers(RandomSource random) {
			if (x == null) x = 1 - 2 * random.nextFloat();
			if (y == null) y = 1 - 2 * random.nextFloat();
			if (z == null) z = 1 - 2 * random.nextFloat();
		}
	}
}




