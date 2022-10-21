package chiefarug.mods.kindled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DispenserBehaviour {

	public static void registerBehaviours() {
		DispenserBlock.registerBehavior(Registry.MAGIC_DUST_ITEM.get(), DispenserBehaviour::dispenseMagicDust);
	}

	public static ItemStack dispenseCandle(BlockSource source, ItemStack stack) {
		return stack;
	}

	public static @NotNull ItemStack dispenseMagicDust(BlockSource source, ItemStack stack) {
		 BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		 Level level = source.getLevel();
		 BlockState state = level.getBlockState(pos);
		 if (state.getBlock() == Registry.MAGIC_PUMPKIN.get()) {
			 level.setBlock(pos, Registry.MAGIC_PUMPKIN.get().defaultBlockState(), 3);
		 }
		 stack.shrink(1);
		return stack;
	}
}
