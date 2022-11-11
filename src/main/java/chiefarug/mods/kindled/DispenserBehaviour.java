package chiefarug.mods.kindled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class DispenserBehaviour {

	public static void registerBehaviours() {
		DispenserBlock.registerBehavior(KindledRegistry.MAGIC_DUST_ITEM.get(), DispenserBehaviour::dispenseMagicDust);
		Kindled.candles.keySet().forEach(candle -> DispenserBlock.registerBehavior(candle, DispenserBehaviour::dispenseCandle));
	}

	public static @NotNull ItemStack dispenseCandle(BlockSource source, ItemStack stack) {
		InWorldTransforms.magicPumpkinToKindled(
				source.getLevel(),
				posInFacing(source.getPos(), source.getBlockState()),
				stack,
				true
		);
		return stack;
	}

	public static @NotNull ItemStack dispenseMagicDust(BlockSource source, ItemStack stack) {
		InWorldTransforms.pumpkinToMagicPumpkin(
				source.getLevel(),
				posInFacing(source.getPos(), source.getBlockState()),
				stack,
				source.getBlockState().getValue(DispenserBlock.FACING),
				true
		);
		return stack;
	}

	private static BlockPos posInFacing(BlockPos pos, BlockState state) {
		return pos.relative(state.getValue(BlockStateProperties.FACING));
	}
}
