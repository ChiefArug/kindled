package chiefarug.mods.kindled;

import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class DispenserBehaviour {

	public static void registerBehaviours() {
		DispenserBlock.registerBehavior(KindledRegistry.MAGIC_DUST_ITEM.get(), DispenserBehaviour::dispenseMagicDust);
		Kindled.candles.keySet().forEach(candle -> DispenserBlock.registerBehavior(candle, DispenserBehaviour::dispenseCandle));
	}

	public static @NotNull ItemStack dispenseCandle(BlockSource source, ItemStack stack) {
		InWorldTransforms.magicPumpkinToKindled(
				source.getLevel(),
				source.getPos(),
				stack
		);
		return stack;
	}

	public static @NotNull ItemStack dispenseMagicDust(BlockSource source, ItemStack stack) {
		InWorldTransforms.pumpkinToMagicPumpkin(
				source.getLevel(),
				source.getPos(),
				stack,
				source.getBlockState().getValue(DispenserBlock.FACING)
		);
		return stack;
	}
}
