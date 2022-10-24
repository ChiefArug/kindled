package chiefarug.mods.kindled;

import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DispenserBehaviour {

	public static void registerBehaviours() {
		DispenserBlock.registerBehavior(Registry.MAGIC_DUST_ITEM.get(), DispenserBehaviour::dispenseMagicDust);
		Kindled.candles.keySet().forEach(candle -> DispenserBlock.registerBehavior(candle, DispenserBehaviour::dispenseCandle));
	}

	public static @NotNull ItemStack dispenseCandle(BlockSource source, ItemStack stack) {
		Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
		BlockPos pos = source.getPos().relative(dir);
		Level level = source.getLevel();
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() == Registry.MAGIC_PUMPKIN.get()) {
			level.destroyBlock(pos, false);

			KindledEntity kindled = new KindledEntity(level, Kindled.candles.get(stack.getItem()), dir);
			kindled.setPos(pos.getX(), pos.getY(), pos.getZ());
			level.addFreshEntity(kindled);

			stack.shrink(1);
		}
		return stack;
	}

	public static @NotNull ItemStack dispenseMagicDust(BlockSource source, ItemStack stack) {
		BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		Level level = source.getLevel();
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() == Blocks.PUMPKIN) {
			level.setBlock(pos, Registry.MAGIC_PUMPKIN.get().defaultBlockState(), 3);
			MagicDustItem.transformParticles(source.getLevel(), pos);
			stack.shrink(1);
		}
		return stack;
	}
}
