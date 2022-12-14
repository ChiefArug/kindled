package chiefarug.mods.kindled.block;

import chiefarug.mods.kindled.InWorldTransforms;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class MagicPumpkinBlock extends HorizontalDirectionalBlock {
	public MagicPumpkinBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
	return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		if (player instanceof ServerPlayer) // Make sure advancements try trigger
			CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);

		return InWorldTransforms.magicPumpkinToKindled(
				level,
				pos,
				stack,
				!player.getAbilities().instabuild
		).orElseGet(() -> super.use(state, level, pos, player, hand, hitResult));
	}
}
