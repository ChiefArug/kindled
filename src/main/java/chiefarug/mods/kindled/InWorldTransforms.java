package chiefarug.mods.kindled;

import chiefarug.mods.kindled.block.MagicPumpkinBlock;
import chiefarug.mods.kindled.entity.KindledEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class InWorldTransforms {

	public static Optional<InteractionResult> pumpkinToMagicPumpkin(Level level, BlockPos pos, ItemStack item, Direction facing) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() == Blocks.PUMPKIN && item.getItem() == KindledRegistry.MAGIC_DUST_ITEM.get()) {
			level.setBlock(pos, KindledRegistry.MAGIC_PUMPKIN.get().defaultBlockState().setValue(MagicPumpkinBlock.FACING, facing), 3);

			item.shrink(1);
			if (level instanceof ServerLevel sl) {
				transformParticles(sl, pos);
			}

			return Optional.of(InteractionResult.CONSUME);
		}
		return Optional.empty();
	}

	public static Optional<InteractionResult> magicPumpkinToKindled(Level level, BlockPos pos, ItemStack item) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() == KindledRegistry.MAGIC_PUMPKIN.get() && Kindled.candles.containsKey(item.getItem())) {
			level.destroyBlock(pos, false);

			KindledEntity kindled = new KindledEntity(level, Kindled.candles.get(item.getItem()), state.getValue(MagicPumpkinBlock.FACING));
			kindled.setPos(pos.getX(), pos.getY(), pos.getZ());
			level.addFreshEntity(kindled);

			item.shrink(1);
			if (level instanceof ServerLevel sl) {
				transformParticles(sl, pos);
			}

			return Optional.of(InteractionResult.CONSUME);
		}
		return Optional.empty();
	}

	private static void transformParticles(ServerLevel level, BlockPos pos) {
		level.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5D, pos.getY() , pos.getZ() + 0.5D, 50, 0.5, 0.9, 0.5, 0.2);
	}
}
