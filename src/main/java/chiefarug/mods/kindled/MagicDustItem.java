package chiefarug.mods.kindled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static chiefarug.mods.kindled.Kindled.getRandomSpeed;

public class MagicDustItem extends Item {
	public MagicDustItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		Level level = context.getLevel();
		MutableBlockPos pos = context.getClickedPos().mutable();
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() == Blocks.PUMPKIN) {
			level.setBlock(pos, Registry.MAGIC_PUMPKIN.get().defaultBlockState(), 3);
			context.getItemInHand().shrink(1);
			if (level.isClientSide()) {
				transformParticles(level, pos);
			}
			return InteractionResult.CONSUME;
		}


		return super.useOn(context);
	}

	public static void transformParticles(Level level, BlockPos pos) {
		RandomSource rand = level.getRandom();
		if (level.isClientSide()) {
			Kindled.addBunchOfClientParticles(level, ParticleTypes.CRIT, 50, rand, pos);
		} else if (level instanceof ServerLevel serverLevel){
			serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5D, pos.getY() , pos.getZ() + 0.5D, 50, 0.5, 0.9, 0.5, getRandomSpeed(rand));
		}
	}
}
