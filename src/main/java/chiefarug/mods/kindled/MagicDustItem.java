package chiefarug.mods.kindled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
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
			for (int i = 0; i < 50; i++) {
				ParticleOffset offset = getRandomOffset(rand);
				level.addParticle(ParticleTypes.CRIT, pos.getX() + 0.5D + offset.getX(), pos.getY() + 0.5D + offset.getY(), pos.getZ() + 0.5D + offset.getZ(), getRandomSpeed(rand), getRandomSpeed(rand), getRandomSpeed(rand));
			}
		} else if (level instanceof ServerLevel serverLevel){
			serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5D, pos.getY() , pos.getZ() + 0.5D, 50, 0.5, 0.9, 0.5, getRandomSpeed(rand));
		}
	}

	private static double getRandomSpeed(RandomSource random) {
		return (0.5D - random.nextFloat()) / 4;
	}

	private static ParticleOffset getRandomOffset(RandomSource random) {
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

	private static Direction randomDir(RandomSource random) {
		return Direction.getRandom(random);
	}

	private static class ParticleOffset {
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
